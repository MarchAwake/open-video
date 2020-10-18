package cn.marchawake.server.dto;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <h1>排序数据对象定义</h1>
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "排序传输对象")
public class SortDto {

    /**
     * ID
     */
    private String id;

    /**
     * 当前排序
     */
    private int oldSort;

    /**
     * 新排序
     */
    private int newSort;
}
