package cn.marchawake.server.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * <h1>分页查询数据传输对象</h1>
 *
 * @author March
 * @date 2020/7/8
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "分页对象", description = "分页对象，参数：page(当前页码)，size(每页显示条数)，total(数据总条数)，list(查询得到的数据)")
public class PageDto<T> {

    /** 当前页码 */
    @ApiModelProperty(name = "page", value = "当前页码")
    protected int page;

    /** 每页显示条数 */
    @ApiModelProperty(name = "size", value = "每页显示条数")
    protected int size;

    /** 数据总条数 */
    @ApiModelProperty(name = "total", value = "数据总条数")
    protected long total;

    /** 查询得到的数据 */
    @ApiModelProperty(name = "list", value = "查询得到的数据")
    protected List<T> list;
}
