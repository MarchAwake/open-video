package cn.marchawake.server.constant;

/**
 * <h1>响应类型枚举</h1>
 *
 * @author MarchAwake
 * @date 2020/08/12
 */
public enum ResponseTypeEnum {

    /**
     * 响应成功
     * 用在响应数据到前端
     */
    SUCCESS("1111","响应成功"),

    /**
     * 响应失败
     * 用在响应数据到前端
     */
    FAILURE("-1111","响应失败"),

    /**
     * 响应失败，服务超时
     * 用在响应数据到前端
     */
    ERROR("0000","非常抱歉,服务超时,请稍后重试"),

    /**
     * 响应失败，请求参数异常
     * 用在响应数据到前端
     */
    PARAM_EXCEPTION("-2222", "请求参数异常");

    /**
     * 响应状态代号
     */
    private final String code;

    /**
     * 响应状态描述
     */
    private final String desc;

    ResponseTypeEnum(String code, String desc) {

        this.code = code;
        this.desc = desc;
    }

    public String getCODE() {
        return code;
    }

    public String getDESC() {
        return desc;
    }
}
