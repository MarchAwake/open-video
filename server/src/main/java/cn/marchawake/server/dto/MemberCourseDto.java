package cn.marchawake.server.dto;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "会员课程传输对象")
public class MemberCourseDto {


    /**
    * 会员课程复合编号
    */
    private String id;


    /**
    * 课程编号
    */
    private String courseId;


    /**
    * 会员编号
    */
    private String memberId;



}