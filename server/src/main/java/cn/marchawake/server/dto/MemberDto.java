package cn.marchawake.server.dto;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "会员传输对象")
public class MemberDto {


    /**
    * 会员编号
    */
    private String id;


    /**
    * 会员手机号
    */
    private String mobile;


    /**
    * 密码
    */
    private String password;


    /**
    * 昵称
    */
    private String name;


    /**
    * 头像url
    */
    private String photo;


    /**
    * 注册时间
    */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date registerTime;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 邮箱验证码
     */
    private String emailCode;

    /**
     * redis 中验证码的 key
     */
    private String imageCodeToken;

    /**
     * 前端页面的验证码
     */
    private String imageCode;

}