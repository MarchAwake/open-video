package cn.marchawake.server.dto;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "课程内容传输对象")
public class CourseContentDto {

    /**
    * 课程ID
    */
    private String id;

    /**
    * 课程内容
    */
    private String content;

    /**
     * 课程内容更新时间
     */
    private Date updateTime;


}