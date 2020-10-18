package cn.marchawake.server.dto;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "邮箱传输对象")
public class EmailboxDto {


    /**
    * 邮箱验证编号
    */
    private String id;


    /**
    * 会员邮箱
    */
    private String email;


    /**
    * 验证码
    */
    private String code;


    /**
    * 用途|枚举[EmailboxUseEnum]:REGISTER["R","注册"],FORGET("F","忘记密码")
    */
    private String use;


    /**
    * 被创建的时间
    */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createdAt;


    /**
    * 状态|枚举[EmailboxStatusEnum]:USED("U", "已使用"), NOT_USED("N","未被使用")
    */
    private String status;


    public String getId() {
    return id;
    }

    public void setId(String id) {
    this.id = id;
    }

    public String getEmail() {
    return email;
    }

    public void setEmail(String email) {
    this.email = email;
    }

    public String getCode() {
    return code;
    }

    public void setCode(String code) {
    this.code = code;
    }

    public String getUse() {
    return use;
    }

    public void setUse(String use) {
    this.use = use;
    }

    public Date getCreatedAt() {
    return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
    this.createdAt = createdAt;
    }

    public String getStatus() {
    return status;
    }

    public void setStatus(String status) {
    this.status = status;
    }


}