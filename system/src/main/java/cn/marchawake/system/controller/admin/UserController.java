package cn.marchawake.system.controller.admin;

import cn.marchawake.server.dto.UserDto;
import cn.marchawake.server.dto.PageDto;
import cn.marchawake.server.dto.ResponseDto;
import cn.marchawake.server.service.UserService;
import cn.marchawake.server.utils.ValidatorUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;


/**
 * <h1>用户表请求控制器</h1>
 *
 * @author March
 * @date 2020/7/8
 */
@RequestMapping("/admin/user")
@RestController
@Slf4j
public class UserController {

    private final UserService service;
    private final RedisTemplate<String, String> redisTemplate;

    /** 业务名称 */
    public static final String BUSINESS_NAME = "用户表";

    @Autowired
    public UserController(UserService service, RedisTemplate<String, String> redisTemplate) {
        this.service = service;
        this.redisTemplate = redisTemplate;
        // 设置序列化标准
        this.redisTemplate.setValueSerializer(new StringRedisSerializer());
        this.redisTemplate.setKeySerializer(new StringRedisSerializer());
    }

    /**
     * 获取用户表列表
     */
    @PostMapping("/list")
    public ResponseDto list(@RequestBody PageDto<UserDto> pageDto) {
        
        return service.list(pageDto);
    }

    /**
     * 保存|修改用户
     */
    @PostMapping("/save")
    public ResponseDto save(@RequestBody UserDto userDto) {

        userDto.setPassword(DigestUtils.md5DigestAsHex(userDto.getPassword().getBytes()));
        ValidatorUtil.require(userDto.getLoginName(), "登录名");
        ValidatorUtil.length(userDto.getLoginName(), "登录名", 1, 20);
        ValidatorUtil.require(userDto.getPassword(), "用户密码");
        ValidatorUtil.length(userDto.getPassword(), "用户密码", 1, 100);
        ValidatorUtil.length(userDto.getNickname(), "昵称", 1, 20);
        ValidatorUtil.length(userDto.getPhone(), "手机号码", 1, 11);
        ValidatorUtil.length(userDto.getImageUrl(), "头像图片地址", 1, 100);
        return service.save(userDto);
    }

    @PostMapping("/save-password")
    public ResponseDto savePassword(@RequestBody UserDto userDto) {

        userDto.setPassword(DigestUtils.md5DigestAsHex(userDto.getPassword().getBytes()));
        ValidatorUtil.require(userDto.getPassword(), "用户密码");
        ValidatorUtil.length(userDto.getPassword(), "用户密码", 1, 100);
        return service.savePassword(userDto);
    }

    /**
     * 根据用户表 ID 删除用户表
     */
    @DeleteMapping("/delete/{id}")
    public ResponseDto delete(@PathVariable String id) {
        ValidatorUtil.require(id, "用户表ID");
        ValidatorUtil.length(id, "用户表ID", 1, 8);
        return service.delete(id);
    }


    /**
     * 登录
     */
    @PostMapping("/login")
    public ResponseDto login(@RequestBody UserDto userDto, HttpServletRequest request) {

        ValidatorUtil.require(userDto.getLoginName(), "登录名");
        ValidatorUtil.length(userDto.getLoginName(), "登录名", 1, 20);
        ValidatorUtil.require(userDto.getPassword(), "用户密码");
        ValidatorUtil.length(userDto.getPassword(), "用户密码", 1, 100);
        userDto.setPassword(DigestUtils.md5DigestAsHex(userDto.getPassword().getBytes()));
        String QRCodeBack = redisTemplate.opsForValue().get(userDto.getImageCodeToken());
        String QRCodeFront = userDto.getImageCode();

        if (StringUtils.isEmpty(QRCodeBack)) {

            return ResponseDto.failure("验证码已过期");
        }

        if (!QRCodeBack.equalsIgnoreCase(QRCodeFront)) {

            return ResponseDto.failure("验证码错误，请重新输入");
        } else {
            redisTemplate.delete(userDto.getImageCodeToken());
        }

        return service.login(userDto, redisTemplate);
    }

    /**
     * 退出登录
     */
    @PostMapping("/logout/{token}")
    public ResponseDto logout(@PathVariable String token) {

        redisTemplate.delete(token);
        return ResponseDto.success();
    }
}
