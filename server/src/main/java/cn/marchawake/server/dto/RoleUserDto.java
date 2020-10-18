package cn.marchawake.server.dto;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "角色用户传输对象")
public class RoleUserDto {


    /**
    * 用户角色编号
    */
    private String id;


    /**
    * 用户编号
    */
    private String userId;


    /**
    * 角色编号
    */
    private String roleId;


    public String getId() {
    return id;
    }

    public void setId(String id) {
    this.id = id;
    }

    public String getUserId() {
    return userId;
    }

    public void setUserId(String userId) {
    this.userId = userId;
    }

    public String getRoleId() {
    return roleId;
    }

    public void setRoleId(String roleId) {
    this.roleId = roleId;
    }


}