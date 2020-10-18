package cn.marchawake.server.dto;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "用户传输对象")
public class UserDto {


    /**
    * id
    */
    private String id;


    /**
    * 登录名
    */
    private String loginName;


    /**
    * 用户密码
    */
    private String password;


    /**
    * 昵称
    */
    private String nickname;


    /**
    * 手机号码
    */
    private String phone;


    /**
    * 头像图片地址
    */
    private String imageUrl;

    /**
     * 发送给前端的令牌
     */
    private String token;

    /**
    * 验证码
    */
    private String imageCode;

    /**
    * 验证码对应redis中验证码的key
    */
    private String imageCodeToken;

    /**
     * 所有资源，用于前端界面控制
     */
    private List<ResourceDto> resourceDtoList;

    /**
     * 所有资源中的请求，用于后端接口拦截
     */
    private HashSet<String> requests;

}