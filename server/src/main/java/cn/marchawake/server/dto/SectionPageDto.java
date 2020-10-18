package cn.marchawake.server.dto;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <h1>课程页面传输对象</h1>
 *
 * @author MarchAwake
 * @data 2020/08/10
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "小节分页传输对象")
public class SectionPageDto extends PageDto<SectionDto> {

    /**
     * 课程ID
     */
    private String courseId;

    /**
     * 大章ID
     */
    private String chapterId;
}
