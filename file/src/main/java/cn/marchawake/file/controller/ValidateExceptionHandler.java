package cn.marchawake.file.controller;

import cn.marchawake.server.constant.ResponseTypeEnum;
import cn.marchawake.server.dto.ResponseDto;
import cn.marchawake.server.exception.ValidatorException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 校验异常处理器，统一响应请求参数异常
 */
@RestControllerAdvice
@Slf4j
public class ValidateExceptionHandler {

    @ExceptionHandler(ValidatorException.class)
    public ResponseDto validatorExceptionHandler(ValidatorException e) {

        log.warn(e.getMessage());
        return ResponseDto.failure(ResponseTypeEnum.PARAM_EXCEPTION.getDESC());
    }
}
