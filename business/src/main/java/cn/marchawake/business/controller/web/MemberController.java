package cn.marchawake.business.controller.web;

import cn.marchawake.server.dto.MemberDto;
import cn.marchawake.server.dto.ResponseDto;
import cn.marchawake.server.enums.EmailboxUseEnum;
import cn.marchawake.server.service.EmailboxService;
import cn.marchawake.server.service.MemberService;
import cn.marchawake.server.utils.ValidatorUtil;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;


@RestController("webMemberController")
@RequestMapping("/web/member")
@Slf4j
@Api(tags = "web-会员操作的相关接口")
public class MemberController {

    public static final String BUSINESS_NAME = "会员";

    private final MemberService memberService;

    private final RedisTemplate<String, String> redisTemplate;

    private final EmailboxService emailboxService;

    @Autowired
    public MemberController(MemberService memberService,
                            RedisTemplate<String, String> redisTemplate,
                            EmailboxService emailboxService) {
        this.memberService = memberService;
        this.redisTemplate = redisTemplate;
        this.emailboxService = emailboxService;
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        this.redisTemplate.setKeySerializer(stringRedisSerializer);
        this.redisTemplate.setValueSerializer(stringRedisSerializer);
    }

    /**
     * 保存，id有值时更新，无值时新增
     */
    @PostMapping("/register")
    public ResponseDto register(@RequestBody MemberDto memberDto) {

        // 参数校验
        ValidatorUtil.require(memberDto.getEmail(), "邮箱");
        ValidatorUtil.length(memberDto.getEmail(), "邮箱", 10, 30);
        ValidatorUtil.require(memberDto.getPassword(), "密码");
        ValidatorUtil.length(memberDto.getName(), "昵称", 1, 50);
        ValidatorUtil.length(memberDto.getPhoto(), "头像url", 1, 200);

        // 密码加密
        memberDto.setPassword(DigestUtils.md5DigestAsHex(memberDto.getPassword().getBytes()));

        // 校验邮箱验证码
        ResponseDto responseDto = emailboxService.validCode(memberDto, EmailboxUseEnum.REGISTER);
        if (responseDto.isSuccess()) {
            // 保存注册信息
            memberService.save(memberDto);
            return ResponseDto.success(memberDto);
        }else {

            return responseDto;
        }

    }

    /**
     * 登录
     */
    @PostMapping("/login")
    public ResponseDto login(@RequestBody MemberDto memberDto) {

        ValidatorUtil.require(memberDto.getEmail(), "登陆邮箱");
        ValidatorUtil.require(memberDto.getPassword(), "登陆密码");

        // 校验 redis 和前端的验证码
        String redisImageCode = redisTemplate.opsForValue().get(memberDto.getImageCodeToken());
        log.info("From Redis ImageCode：{}", redisImageCode);

        if (StringUtils.isEmpty(redisImageCode)) {
            return ResponseDto.failure("验证码已过期");
        }

        if (!redisImageCode.toLowerCase().equals(memberDto.getImageCode().toLowerCase())) {

            return ResponseDto.failure("验证码错误");
        }

        // 验证通过,移除验证码
        redisTemplate.delete(memberDto.getImageCodeToken());

        return memberService.login(memberDto, redisTemplate);
    }

    /**
     * 退出登录
     */
    @GetMapping("/logout/{token}")
    public ResponseDto logout(@PathVariable String token) {

        if (!StringUtils.isEmpty(token)) {
            redisTemplate.delete(token);
        }

        return ResponseDto.success();
    }

    /**
     * 校验邮箱是否存在
     * 存在: data=true, 不存在: data=false
     */
    @GetMapping("/check-email-exist/{email}")
    public ResponseDto isEmailExist(@PathVariable String email) {

        ValidatorUtil.require(email, "邮箱");
        log.info("校验邮箱是否存在");
        return memberService.isEmailExist(email);
    }


    /**
     * 重置密码
     */
    @PostMapping("/reset-password")
    public ResponseDto resetPassword(@RequestBody MemberDto memberDto) {

        log.info("校验邮箱验证码");
        ResponseDto responseDto = emailboxService.validCode(memberDto, EmailboxUseEnum.FORGET);
        if (responseDto.isSuccess()) {
            log.info("重置密码");
            return memberService.resetPassword(memberDto);
        } else {
            return responseDto;
        }
    }
}
