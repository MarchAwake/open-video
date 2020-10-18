package cn.marchawake.system.controller.admin;

import cn.marchawake.server.dto.RoleUserDto;
import cn.marchawake.server.dto.PageDto;
import cn.marchawake.server.dto.ResponseDto;
import cn.marchawake.server.service.RoleUserService;
import cn.marchawake.server.utils.ValidatorUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * <h1>用户角色关联表请求控制器</h1>
 *
 * @author March
 * @date 2020/7/8
 */
@RequestMapping("/admin/roleUser")
@RestController
@Slf4j
public class RoleUserController {

    /** 用户角色关联表服务接口 */
    private final RoleUserService service;

    /** 用户角色关联表相关业务 */
    public static final String BUSINESS_NAME = "用户角色关联表";

    @Autowired
    public RoleUserController(RoleUserService service) {
        this.service = service;
    }

    /**
     * <h2>获取用户角色关联表列表</h2>
     * @param pageDto {@link PageDto}
     * @return {@link PageDto}
     */
    @PostMapping("/list")
    public ResponseDto list(@RequestBody PageDto<RoleUserDto> pageDto) {
        
        return service.list(pageDto);
    }

    /**
     * <h2>保存或修改用户角色关联表</h2>
     * @param roleUserDto {@link RoleUserDto}
     * @return {@link RoleUserDto}
     */
    @PostMapping("/save")
    public ResponseDto save(@RequestBody RoleUserDto roleUserDto) {
        // 保存校验
        ValidatorUtil.require(roleUserDto.getUserId(), "用户编号");
        ValidatorUtil.require(roleUserDto.getRoleId(), "角色编号");
        return service.save(roleUserDto);
    }

    /**
     * <h2>根据用户角色关联表 ID 删除用户角色关联表</h2>
     * @param id
     * @return {@link ResponseDto}
     */
    @DeleteMapping("/delete/{id}")
    public ResponseDto delete(@PathVariable String id) {
        ValidatorUtil.require(id, "用户角色关联表ID");
        ValidatorUtil.length(id, "用户角色关联表ID", 1, 8);
        return service.delete(id);
    }

}
