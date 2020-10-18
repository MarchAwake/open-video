package cn.marchawake.business.controller;

import cn.marchawake.server.constant.ResponseTypeEnum;
import cn.marchawake.server.dto.ResponseDto;
import cn.marchawake.server.exception.ValidatorException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 控制器层异常捕获处理器，捕获由控制层抛出的 ValidatorException 异常
 * 统一返回请求参数异常
 *
 * @author March
 * @date 2020/09/10
 */

@RestControllerAdvice
@Slf4j
public class ControllerExceptionHandler {

    /**
     * 捕获 ValidatorException 异常
     * 返回 请求参数异常
     */
    @ExceptionHandler(value = ValidatorException.class)
    public ResponseDto validatorExceptionHandler(ValidatorException e) {
        log.warn(e.getMessage());
        return ResponseDto.failure(ResponseTypeEnum.PARAM_EXCEPTION.getDESC());
    }

}
