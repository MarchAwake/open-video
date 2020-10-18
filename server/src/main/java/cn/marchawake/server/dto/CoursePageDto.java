package cn.marchawake.server.dto;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "课程分页传输对象")
public class CoursePageDto extends PageDto<CourseDto>{

    private String status;

    private String categoryId;

}
