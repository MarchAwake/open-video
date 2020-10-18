package cn.marchawake.system.controller.admin;

import cn.marchawake.server.dto.PageDto;
import cn.marchawake.server.dto.ResponseDto;
import cn.marchawake.server.dto.RoleDto;
import cn.marchawake.server.service.RoleService;
import cn.marchawake.server.utils.ValidatorUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * <h1>角色表请求控制器</h1>
 *
 * @author March
 * @date 2020/7/8
 */
@RequestMapping("/admin/role")
@RestController
@Slf4j
public class RoleController {

    /**
     * 角色表服务接口
     */
    private final RoleService service;

    /**
     * 业务名称
     */
    public static final String BUSINESS_NAME = "角色表";

    @Autowired
    public RoleController(RoleService service) {
        this.service = service;
    }

    /**
     * 获取角色表列表
     */
    @PostMapping("/list")
    public ResponseDto list(@RequestBody PageDto<RoleDto> pageDto) {
        
        return service.list(pageDto);
    }

    /**
     * 保存或修改角色表
     */
    @PostMapping("/save")
    public ResponseDto save(@RequestBody RoleDto roleDto) {
        // 保存校验
        ValidatorUtil.require(roleDto.getName(), "角色名称");
        ValidatorUtil.length(roleDto.getName(), "角色名称", 1, 50);
        ValidatorUtil.require(roleDto.getDesc(), "角色描述");
        ValidatorUtil.length(roleDto.getDesc(), "角色描述", 1, 100);
        return service.save(roleDto);
    }


    /**
     * 根据角色表 ID 删除角色表
     */
    @DeleteMapping("/delete/{id}")
    public ResponseDto delete(@PathVariable String id) {
        ValidatorUtil.require(id, "角色表ID");
        ValidatorUtil.length(id, "角色表ID", 1, 8);
        return service.delete(id);
    }


    /**
     * 保存角色的资源
     */
    @PostMapping("/save-role-resource")
    public ResponseDto saveRoleResource(@RequestBody RoleDto roleDto) {

        return service.saveRoleResource(roleDto);
    }


    /**
     * 加载角色的资源
     */
    @GetMapping("/load-role-resource/{roleId}")
    public ResponseDto loadRoleResource(@PathVariable String roleId) {

        return service.loadRoleResource(roleId);
    }

    /**
     * 保存角色的用户
     */
    @PostMapping("/save-role-user")
    public ResponseDto saveRoleUser(@RequestBody RoleDto roleDto) {

        return service.saveRoleUser(roleDto);
    }

    /**
     * 加载角色的用户
     */
    @GetMapping("/load-role-user/{roleId}")
    public ResponseDto loadRoleUser(@PathVariable String roleId) {

        return service.loadRoleUser(roleId);
    }
}
