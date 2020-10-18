package cn.marchawake.server.constant;

/**
 * 后台管理登录状态常量
 */
public enum SystemModuleServiceStateEnum {

    /**
     * 用户不存在
     * 使用在 User 服务层
     */
    LOGIN_NAME_NOT_EXISTS(100, "用户名或密码错误"),


    /**
     * 登陆密码错误
     * 使用在 User 服务层
     */
    LOGIN_PASSWORD_ERROR(200, "用户名或密码错误"),

    /**
     * 验证码错误
     * 使用在 User 服务层
     */
    VERIFICATION_CODE_ERROR(300, "验证码错误"),

    /**
     * 用户已存在
     * 使用在 User 服务层
     */
    USER_ALREADY_EXISTED(400, "用户已存在");

    /**
     * 状态代号
     */
    private final int code;

    /**
     * 状态描述
     */
    private final String desc;

    SystemModuleServiceStateEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    /**
     * 获取状态代号
     */
    public int getCode() {
        return code;
    }

    /**
     * 获取状态描述
     */
    public String getDesc() {
        return desc;
    }
}
