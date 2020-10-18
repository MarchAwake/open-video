package cn.marchawake.server.dto;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "登录会员传输对象")
public class LoginMemberDto {

    /** 会员编号 */
    private String id;

    /** 会员密码 */
    private String password;

    /** 会员的邮箱 */
    private String email;

    /** 会员名 */
    private String name;

    /** 会员的头像地址 */
    private String photo;

    /** 存放会员信息到redis的key */
    private String token;
}
