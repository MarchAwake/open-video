package cn.marchawake.server.dto;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "课程内容文件传输对象")
public class CourseContentFileDto {


    /**
    * id
    */
    private String id;


    /**
    * 课程ID
    */
    private String courseId;


    /**
    * 地址
    */
    private String url;


    /**
    * 文件名	
    */
    private String name;


    /**
    * 文件大小|字节b
    */
    private Integer size;

}