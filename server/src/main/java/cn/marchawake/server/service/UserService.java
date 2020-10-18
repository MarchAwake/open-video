package cn.marchawake.server.service;

import cn.marchawake.server.constant.SystemModuleServiceStateEnum;
import cn.marchawake.server.dto.*;
import cn.marchawake.server.entity.User;
import cn.marchawake.server.entity.UserExample;
import cn.marchawake.server.mapper.UserMapper;
import cn.marchawake.server.mapper.custom.CustomUserMapper;
import cn.marchawake.server.utils.CopyUtil;
import cn.marchawake.server.utils.UuidUtil;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.HashSet;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * <h1>User 表服务接口实现定义</h1>
 *
 * @author March
 * @date 2020/7/2
 */
@Service
@Slf4j
public class UserService {

    /** 用户表持久层接口 */
    private final UserMapper userMapper;

    /** 自定义用户表持久层接口 */
    private final CustomUserMapper customUserMapper;

    @Autowired
    public UserService(UserMapper userMapper,
                       CustomUserMapper customUserMapper) {
        this.userMapper = userMapper;
        this.customUserMapper = customUserMapper;
    }

    public ResponseDto list(PageDto<UserDto> pageDto) {

        PageHelper.startPage(pageDto.getPage(),pageDto.getSize());
        UserExample userExample = new UserExample();
        List<User> userList = userMapper.selectByExample(userExample);
        pageDto.setTotal(new PageInfo<>(userList).getTotal());
        pageDto.setList(CopyUtil.copyList(userList, UserDto.class));
        return ResponseDto.success(pageDto);
    }


    @Transactional(rollbackFor = Exception.class)
    public ResponseDto save(UserDto userDto) {

        User user = CopyUtil.copy(userDto, User.class);
        if (StringUtils.isEmpty(userDto.getId())) {
            return this.insert(user);
        }else {
            return this.update(user);
        }
    }


    private ResponseDto update(User user) {

        user.setPassword(null);
        return userMapper.updateByPrimaryKeySelective(user) == 1 ?
                ResponseDto.success() : ResponseDto.error();
    }


    private ResponseDto insert(User user) {

        user.setId(UuidUtil.getShortUuid());
        User userDb = this.findUserByLoginName(user.getLoginName());
        if (userDb != null) {
            return ResponseDto.failure(SystemModuleServiceStateEnum.USER_ALREADY_EXISTED.getDesc());
        }
        return userMapper.insert(user) == 1 ?
                ResponseDto.success() : ResponseDto.error();
    }



    @Transactional(rollbackFor = Exception.class)
    public ResponseDto delete(String id) {

        return userMapper.deleteByPrimaryKey(id) == 1 ?
            ResponseDto.success() : ResponseDto.error();
    }

    /**
     * 根据用户名查找用户
     */
    public User findUserByLoginName(String loginName) {

        UserExample example = new UserExample();
        UserExample.Criteria criteria = example.createCriteria();
        criteria.andLoginNameEqualTo(loginName);
        List<User> userList = userMapper.selectByExample(example);

        return CollectionUtils.isEmpty(userList) ? null : userList.get(0);
    }



    /**
     * 保存密码
     */
    public ResponseDto savePassword(UserDto userDto) {

        User user = new User();
        user.setId(userDto.getId());
        user.setPassword(userDto.getPassword());

        return userMapper.updateByPrimaryKeySelective(user) == 1 ?
                ResponseDto.success() : ResponseDto.error();
    }



    /**
     * 登录校验
     */
    public ResponseDto login(UserDto userDto, RedisTemplate<String, String> redisTemplate) {

        User userDB = findUserByLoginName(userDto.getLoginName());

        if (userDB == null) {

            return ResponseDto.failure(SystemModuleServiceStateEnum.LOGIN_NAME_NOT_EXISTS.getDesc());
        }

        if (!userDto.getPassword().equals(userDB.getPassword())) {

            return ResponseDto.failure(SystemModuleServiceStateEnum.LOGIN_PASSWORD_ERROR.getDesc());
        }

        userDto = CopyUtil.copy(userDB, UserDto.class);
        userDto.setToken(UuidUtil.getUuid());
        setAuth(userDto);
        redisTemplate.opsForValue().set(userDto.getToken(), JSON.toJSONString(userDto), 3600, TimeUnit.SECONDS);

        return ResponseDto.success(userDto);
    }



    /**
     * 为登录用户读取权限
     */
    private void setAuth(UserDto userDto) {

        List<String> roleIdList = customUserMapper.findRoleIds(userDto.getId());
        log.warn("roleIdList:{}", roleIdList);
        if (CollectionUtils.isEmpty(roleIdList)) {
            return;
        }

        List<String> resourceIds = customUserMapper.findResourceIds(roleIdList);
        log.warn("resourceIds:{}", resourceIds);
        if (CollectionUtils.isEmpty(resourceIds)) {
            return;
        }

        List<ResourceDto> resourceDtoList = customUserMapper.findResources(resourceIds);
        log.warn("resourceDtoList:{}", resourceDtoList);
        if (CollectionUtils.isEmpty(resourceDtoList)) {
            return;
        }

        userDto.setResourceDtoList(resourceDtoList);

        // 整理所有有权限的请求，用于接口拦截
        HashSet<String> requestSet = new HashSet<>();
        for (ResourceDto resourceDto : resourceDtoList) {

            String arrayString = resourceDto.getRequest();
            List<String> requestList = JSON.parseArray(arrayString, String.class);

            if (CollectionUtils.isEmpty(requestList)) {
                continue;
            }

            requestSet.addAll(requestList);
        }

        log.info("有权限的请求：{}", requestSet);
        userDto.setRequests(requestSet);
    }

}
