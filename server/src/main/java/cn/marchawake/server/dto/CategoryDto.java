package cn.marchawake.server.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 分类数据对象定义
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "CategoryDto", description = "分类传输对象")
public class CategoryDto {

    /**
    * ID
    */
    @ApiModelProperty(hidden = true)
    private String id;


    /**
    * 父ID
    */
    @ApiModelProperty(hidden = true)
    private String parent;


    /**
    * 名称
    */
    @ApiModelProperty(value = "分类名称", name = "name", example = "前端开发",required = true)
    private String name;


    /**
    * 顺序
    */
    @ApiModelProperty(value = "分类序号", name = "sort", example = "100")
    private Integer sort;


}