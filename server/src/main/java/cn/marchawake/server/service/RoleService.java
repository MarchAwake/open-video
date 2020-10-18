package cn.marchawake.server.service;

import cn.marchawake.server.dto.RoleDto;
import cn.marchawake.server.dto.PageDto;
import cn.marchawake.server.dto.ResponseDto;
import cn.marchawake.server.entity.*;
import cn.marchawake.server.mapper.RoleMapper;
import cn.marchawake.server.mapper.RoleResourceMapper;
import cn.marchawake.server.mapper.RoleUserMapper;
import cn.marchawake.server.utils.CopyUtil;
import cn.marchawake.server.utils.UuidUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.validation.Valid;
import java.util.LinkedList;
import java.util.List;

/**
 * <h1>Role 表服务接口实现定义</h1>
 *
 * @author March
 * @date 2020/7/2
 */
@Service
@Slf4j
public class RoleService {

    /** 角色表持久化接口 */
    private final RoleMapper roleMapper;

    /** 角色资源表持久化接口 */
    private final RoleResourceMapper roleResourceMapper;

    /** 角色用户表持久化接口 */
    private final RoleUserMapper roleUserMapper;

    @Autowired
    public RoleService(RoleMapper roleMapper,
                       RoleResourceMapper roleResourceMapper,
                       RoleUserMapper roleUserMapper) {
        this.roleMapper = roleMapper;
        this.roleResourceMapper = roleResourceMapper;
        this.roleUserMapper = roleUserMapper;
    }

    public ResponseDto list(PageDto<RoleDto> pageDto) {

        /** 分页查询 */
        PageHelper.startPage(pageDto.getPage(),pageDto.getSize());

        RoleExample roleExample = new RoleExample();
        List<Role> roleList = roleMapper.selectByExample(roleExample);

        /** 设置 PageDto 总条数 */
        pageDto.setTotal(new PageInfo<>(roleList).getTotal());

        /** 将数据封装到 pageDto */
        pageDto.setList(CopyUtil.copyList(roleList, RoleDto.class));

        /** 将数据存放到 ResponseDto 并返回*/
        return ResponseDto.success(pageDto);
    }

    @Transactional(rollbackFor = Exception.class)
    public ResponseDto save(RoleDto roleDto) {

        Role role = CopyUtil.copy(roleDto, Role.class);
        if (StringUtils.isEmpty(roleDto.getId())) {
            return this.insert(role);
        }else {
            return this.update(role);
        }
    }

    private ResponseDto update(Role role) {

        return roleMapper.updateByPrimaryKey(role) == 1 ?
                ResponseDto.success() : ResponseDto.error();
    }

    private ResponseDto insert(Role role) {

        role.setId(UuidUtil.getShortUuid());
        return roleMapper.insert(role) == 1 ?
                ResponseDto.success() : ResponseDto.error();
    }

    @Transactional(rollbackFor = Exception.class)
    public ResponseDto delete(String id) {

        return roleMapper.deleteByPrimaryKey(id) == 1 ?
            ResponseDto.success() : ResponseDto.error();
    }

    /**
     * 依据角色ID保存角色的资源
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseDto saveRoleResource(RoleDto roleDto) {

        String id = roleDto.getId();
        RoleResourceExample example = new RoleResourceExample();
        example.createCriteria().andRoleIdEqualTo(id);
        roleResourceMapper.deleteByExample(example);

        List<String> resourceIdList = roleDto.getResourceIdList();
        if (CollectionUtils.isEmpty(resourceIdList)) {
            return ResponseDto.success();
        }


        RoleResource roleResource = new RoleResource();
        roleResource.setRoleId(id);
        for (String resourceId : resourceIdList) {
            roleResource.setId(UuidUtil.getShortUuid());
            roleResource.setResourceId(resourceId);
            roleResourceMapper.insert(roleResource);
        }

        return ResponseDto.success();
    }


    /**
     * 依据角色ID加载角色拥有的资源
     */
    public ResponseDto loadRoleResource(String roleId) {

        RoleResourceExample example = new RoleResourceExample();
        example.createCriteria().andRoleIdEqualTo(roleId);
        List<RoleResource> roleResourceList = roleResourceMapper.selectByExample(example);

        if (CollectionUtils.isEmpty(roleResourceList)) {
            return ResponseDto.success();
        }

        List<String> resourceIdList = new LinkedList<>();
        for (RoleResource roleResource : roleResourceList) {
            resourceIdList.add(roleResource.getResourceId());
        }


        return ResponseDto.success(resourceIdList);
    }


    /**
     * 依据角色ID保存角色的用户
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseDto saveRoleUser(RoleDto roleDto) {

        String id = roleDto.getId();
        RoleUserExample example = new RoleUserExample();
        example.createCriteria().andRoleIdEqualTo(id);
        roleUserMapper.deleteByExample(example);

        List<String> userIdList = roleDto.getUserIdList();
        if (CollectionUtils.isEmpty(userIdList)) {
            return ResponseDto.success();
        }

        RoleUser roleUser = new RoleUser();
        roleUser.setRoleId(id);
        for (String userId : userIdList) {
            roleUser.setId(UuidUtil.getShortUuid());
            roleUser.setUserId(userId);
            roleUserMapper.insert(roleUser);
        }

        return ResponseDto.success();
    }


    /**
     * 依据角色ID加载角色的用户
     */
    public ResponseDto loadRoleUser(String roleId) {

        RoleUserExample example = new RoleUserExample();
        example.createCriteria().andRoleIdEqualTo(roleId);
        List<RoleUser> roleUserList = roleUserMapper.selectByExample(example);

        if (CollectionUtils.isEmpty(roleUserList)) {
            return ResponseDto.success();
        }

        List<String> userIdList = new LinkedList<>();
        for (RoleUser roleUser : roleUserList) {
            userIdList.add(roleUser.getUserId());
        }
        return ResponseDto.success(userIdList);
    }
}
