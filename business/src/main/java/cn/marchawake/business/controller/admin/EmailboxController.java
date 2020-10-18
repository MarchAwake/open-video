package cn.marchawake.business.controller.admin;

import cn.marchawake.server.dto.EmailboxDto;
import cn.marchawake.server.dto.PageDto;
import cn.marchawake.server.dto.ResponseDto;
import cn.marchawake.server.service.EmailboxService;
import cn.marchawake.server.utils.ValidatorUtil;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * <h1>请求控制器</h1>
 *
 * @author March
 * @date 2020/7/8
 */
@RequestMapping("/admin/emailbox")
@RestController
@Slf4j
@Api(tags = "admin-邮箱验证操作的相关接口")
public class EmailboxController {

    /** 服务接口 */
    private final EmailboxService service;


    /** 业务名称 */
    public static final String BUSINESS_NAME = "邮箱验证";


    @Autowired
    public EmailboxController(EmailboxService service) {
        this.service = service;
    }


    /** 获取列表 */
    @PostMapping("/list")
    public ResponseDto list(@RequestBody PageDto<EmailboxDto> pageDto) {

        ValidatorUtil.require(pageDto, "分页参数");
        return service.list(pageDto);
    }

}
