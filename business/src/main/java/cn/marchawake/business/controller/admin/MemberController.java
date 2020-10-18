package cn.marchawake.business.controller.admin;

import cn.marchawake.server.dto.MemberDto;
import cn.marchawake.server.dto.PageDto;
import cn.marchawake.server.dto.ResponseDto;
import cn.marchawake.server.service.MemberService;
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
@RequestMapping("/admin/member")
@RestController
@Slf4j
@Api(tags = "admin-会员操作的相关接口")
public class MemberController {

    /** 服务接口 */
    private final MemberService service;

    /** 相关业务 */
    public static final String BUSINESS_NAME = "会员表";

    @Autowired
    public MemberController(MemberService service) {
        this.service = service;
    }

    /** 获取列表 */
    @PostMapping("/list")
    public ResponseDto list(@RequestBody PageDto<MemberDto> pageDto) {

        ValidatorUtil.require(pageDto, "分页参数");
        return service.list(pageDto);
    }

}
