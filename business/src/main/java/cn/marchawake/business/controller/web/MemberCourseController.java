package cn.marchawake.business.controller.web;

import cn.marchawake.server.dto.MemberCourseDto;
import cn.marchawake.server.dto.ResponseDto;
import cn.marchawake.server.service.MemberCourseService;
import cn.marchawake.server.utils.ValidatorUtil;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;

@RestController("webMemberCourseController")
@RequestMapping("/web/member-course")
@Api(tags = "web-会员课程操作的相关接口")
public class MemberCourseController {

    public static final String BUSINESS_NAME = "会员课程报名";

    @Resource
    private MemberCourseService memberCourseService;

    /**
     * 判断报名状态,进行数据封装返回
     */
    @PostMapping("/enroll")
    public ResponseDto enroll(@RequestBody MemberCourseDto memberCourseDto) {
        // 保存校验
        ValidatorUtil.require(memberCourseDto.getMemberId(), "会员id");
        ValidatorUtil.require(memberCourseDto.getCourseId(), "课程id");

        return memberCourseService.enroll(memberCourseDto);
    }

    /**
     * 获取报名信息
     */
    @PostMapping("/get-enroll")
    public ResponseDto getEnroll(@RequestBody MemberCourseDto memberCourseDto) {

        ValidatorUtil.require(memberCourseDto.getCourseId(), "课程ID");
        ValidatorUtil.length(memberCourseDto.getCourseId(), "课程ID", 8 ,8);
        ValidatorUtil.require(memberCourseDto.getMemberId(), "会员ID");
        ValidatorUtil.length(memberCourseDto.getMemberId(), "课程ID", 8 ,8);

        return memberCourseService.getEnroll(memberCourseDto);
    }
}
