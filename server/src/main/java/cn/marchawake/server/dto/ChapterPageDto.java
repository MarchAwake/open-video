package cn.marchawake.server.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <h1>增加大章页面关联课程ID</h1>
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "大章分页传输对象", description = "大章分页传输对象")
public class ChapterPageDto extends PageDto<ChapterDto> {

    @ApiModelProperty(name = "课程ID", value = "courseId", notes = "查询大章的依据", required = true)
    private String courseId;

}
