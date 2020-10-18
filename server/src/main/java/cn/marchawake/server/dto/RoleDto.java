package cn.marchawake.server.dto;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "角色传输对象")
public class RoleDto {


    /**
    * 角色编号
    */
    private String id;


    /**
    * 角色名称
    */
    private String name;


    /**
    * 角色描述
    */
    private String desc;


    /**
     * 资源ID集合
     */
    private List<String> resourceIdList;


    /**
     * 用户ID集合
     */
    private List<String> userIdList;
}