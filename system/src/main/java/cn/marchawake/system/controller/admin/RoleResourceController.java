package cn.marchawake.system.controller.admin;

import cn.marchawake.server.dto.RoleResourceDto;
import cn.marchawake.server.dto.PageDto;
import cn.marchawake.server.dto.ResponseDto;
import cn.marchawake.server.service.RoleResourceService;
import cn.marchawake.server.utils.ValidatorUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * <h1>角色资源关联表请求控制器</h1>
 *
 * @author March
 * @date 2020/7/8
 */
@RequestMapping("/admin/role-resource")
@RestController
@Slf4j
public class RoleResourceController {

    /** 角色资源关联表服务接口 */
    private final RoleResourceService service;

    /** 角色资源关联表相关业务 */
    public static final String BUSINESS_NAME = "角色资源关联表";

    @Autowired
    public RoleResourceController(RoleResourceService service) {
        this.service = service;
    }

    /**
     * <h2>获取角色资源关联表列表</h2>
     * @param pageDto {@link PageDto}
     * @return {@link PageDto}
     */
    @PostMapping("/list")
    public ResponseDto list(@RequestBody PageDto<RoleResourceDto> pageDto) {
        
        return service.list(pageDto);
    }

    /**
     * <h2>保存或修改角色资源关联表</h2>
     * @param roleResourceDto {@link RoleResourceDto}
     * @return {@link RoleResourceDto}
     */
    @PostMapping("/save")
    public ResponseDto save(@RequestBody RoleResourceDto roleResourceDto) {
        // 保存校验
        ValidatorUtil.require(roleResourceDto.getRoleId(), "角色编号");
        ValidatorUtil.require(roleResourceDto.getResourceId(), "资源编号");
        return service.save(roleResourceDto);
    }

    /**
     * <h2>根据角色资源关联表 ID 删除角色资源关联表</h2>
     * @param id
     * @return {@link ResponseDto}
     */
    @DeleteMapping("/delete/{id}")
    public ResponseDto delete(@PathVariable String id) {
        ValidatorUtil.require(id, "角色资源关联表ID");
        ValidatorUtil.length(id, "角色资源关联表ID", 1, 8);
        return service.delete(id);
    }

}
