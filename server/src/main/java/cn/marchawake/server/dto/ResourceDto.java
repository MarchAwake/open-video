package cn.marchawake.server.dto;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "资源传输对象")
public class ResourceDto {


    /**
    * 资源编号
    */
    private String id;


    /**
    * 资源名称|菜单或按钮
    */
    private String name;


    /**
    * 页面|路由
    */
    private String page;


    /**
    * 请求|接口
    */
    private String request;


    /**
    * 资源对应的父ID
    */
    private String parent;

    /**
     * 资源列表
     */
    private List<ResourceDto> children;
}