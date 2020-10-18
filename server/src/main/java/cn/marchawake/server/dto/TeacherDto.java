package cn.marchawake.server.dto;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "讲师传输对象")
public class TeacherDto {


    /**
    * id
    */
    private String id;


    /**
    * 姓名
    */
    private String name;


    /**
    * 昵称
    */
    private String nickname;


    /**
    * 头像
    */
    private String image;


    /**
    * 职位
    */
    private String position;


    /**
    * 座右铭
    */
    private String motto;


    /**
    * 简介
    */
    private String intro;

}