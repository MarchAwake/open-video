package cn.marchawake.server.dto;

import cn.marchawake.server.constant.ResponseTypeEnum;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <h1>通用响应数据传输对象</h1>
 *
 * @author March
 * @date 2020/7/10
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "通用响应传输对象")
public class ResponseDto {

    /**
     * 业务上的成功或失败
     */
    private boolean success;

    /**
     * 响应的代号
     */
    private String code;

    /**
     * 响应的描述信息，与响应的代号对应
     */
    private String message;

    /**
     * 响应的数据
     */
    private Object data;

    /**
     * <h2>成功响应，并携带数据</h2>
     * @return {@link ResponseDto}
     */
    public static ResponseDto success(Object data) {
        return new ResponseDto(true,
                ResponseTypeEnum.SUCCESS.getCODE(),
                ResponseTypeEnum.SUCCESS.getDESC(),
                data);
    }

    /**
     * 成功空响应(代表成功，但没有数据返回)
     * @return {@link ResponseDto}
     */
    public static ResponseDto success() {
        return new ResponseDto(true,
                ResponseTypeEnum.SUCCESS.getCODE(),
                ResponseTypeEnum.SUCCESS.getDESC(),
                null);
    }

    /**
     * <h2>错误空响应</h2>
     * @return {@link ResponseDto}
     */
    public static ResponseDto failure() {
        return new ResponseDto(false,
                ResponseTypeEnum.FAILURE.getCODE(),
                ResponseTypeEnum.FAILURE.getDESC(),
                null);
    }

    /**
     * <h2>错误空响应，可以自定义响应失败描述信息</h2>
     * @param errorMsg 错误描述信息
     * @return {@link ResponseDto}
     */
    public static ResponseDto failure(String errorMsg) {
        return new ResponseDto(false,
                ResponseTypeEnum.FAILURE.getCODE(),
                errorMsg,null);
    }

    /**
     * <h2>服务器错误响应</h2>
     * @return {@link ResponseDto}
     */
    public static ResponseDto error() {
        return new ResponseDto(false,
                ResponseTypeEnum.ERROR.getCODE(),
                ResponseTypeEnum.ERROR.getDESC(),
                null);
    }


}
