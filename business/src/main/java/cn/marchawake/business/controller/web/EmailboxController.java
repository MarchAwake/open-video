package cn.marchawake.business.controller.web;

import cn.marchawake.server.dto.EmailboxDto;
import cn.marchawake.server.dto.ResponseDto;
import cn.marchawake.server.service.EmailboxService;
import cn.marchawake.server.utils.ValidatorUtil;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController("webEmailboxController")
@RequestMapping("/web/emailbox")
@Slf4j
@Api(tags = "web-邮箱验证操作的相关接口")
public class EmailboxController {

    public static final String BUSINESS_NAME = "邮箱验证码";

    private final EmailboxService service;

    @Autowired
    public EmailboxController(EmailboxService service) {
        this.service = service;
    }


    @PostMapping("/send")
    public ResponseDto send(@RequestBody EmailboxDto emailboxDto) {

        ValidatorUtil.require(emailboxDto.getEmail(), "邮箱");
        ValidatorUtil.require(emailboxDto.getUse(), "验证用途");
        log.info("发送邮箱验证码到: {}", JSON.toJSONString(emailboxDto));

        return service.sendCode(emailboxDto);
    }
}
