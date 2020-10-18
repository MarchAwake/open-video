package cn.marchawake.server.dto;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <h1>数据传输对象</h1>
 *
 * @author march
 * @date 2020年7月8日11
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "大章传输对象")
public class ChapterDto {

    /** 大章 id */
    private String id;

    /** 大章课程 id */
    private String courseId;

    /** 大章名称 */
    private String name;

}