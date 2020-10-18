package cn.marchawake.server.dto;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "角色资源传输对象")
public class RoleResourceDto {


    /**
    * 角色资源编号
    */
    private String id;


    /**
    * 角色编号
    */
    private String roleId;


    /**
    * 资源编号
    */
    private String resourceId;


    public String getId() {
    return id;
    }

    public void setId(String id) {
    this.id = id;
    }

    public String getRoleId() {
    return roleId;
    }

    public void setRoleId(String roleId) {
    this.roleId = roleId;
    }

    public String getResourceId() {
    return resourceId;
    }

    public void setResourceId(String resourceId) {
    this.resourceId = resourceId;
    }


}