package cn.marchawake.server.constant;

/**
 * 登录状态枚举
 *
 * @author MarchAwake
 * @date 2020/09/09
 */
public enum  BusinessModuleServiceStateEnum {

    /**
     * 邮箱未注册
     * 使用在Business的注册流程
     */
    EMAIL_NOT_REGISTER(127,"该邮箱还未注册"),

    /**
     * 邮箱已被注册
     * 使用在Business的注册流程
     */
    EMAIL_ALREADY_REGISTERED(-127,"邮箱已被注册"),

    /**
     * 邮箱验证码错误
     * 使用在Business的注册|重置密码流程
     */
    EMAIL_CODE_ERROR(-126,"邮箱验证码错误"),

    /**
     * 邮箱验证码不存在或已过期
     * 使用在Business的注册|重置密码流程
     */
    EMAIL_CODE_EXPIRED(-125, "邮箱验证码不存在或已过期，请重新发送"),

    /**
     * 邮箱验证码请求过于频繁
     * 使用在Business的注册|重置密码流程
     */
    EMAIL_CODE_TOO_FREQUENT(128,"邮箱验证码请求过于频繁"),

    /**
     * 登录邮箱不存在
     * 使用在Business服务的登录流程
     */
    LOGIN_EMAIL_NOT_EXIST(101,"邮箱不存在或密码错误"),


    /**
     * 登录密码错误
     * 使用在Business服务的登录流程
     */
    LOGIN_PASSWORD_ERROR(102,"邮箱不存在或密码错误");

    /**
     * 状态代号
     */
    private final int code;

    /**
     * 状态描述
     */
    private final String desc;

    BusinessModuleServiceStateEnum(int code, String desc) {

        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
