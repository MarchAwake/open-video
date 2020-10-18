package cn.marchawake.server.dto;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <h1>课程分类传输对象</h1>
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "课程分类传输对象")
public class CourseCategoryDto {

    /**
    * id
    */
    private String id;


    /**
    * 课程|course.id
    */
    private String courseId;


    /**
    * 分类|course.id
    */
    private String categoryId;

}