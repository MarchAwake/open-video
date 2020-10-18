package cn.marchawake.business.controller.admin;

import cn.marchawake.server.dto.TeacherDto;
import cn.marchawake.server.dto.PageDto;
import cn.marchawake.server.dto.ResponseDto;
import cn.marchawake.server.service.TeacherService;
import cn.marchawake.server.utils.ValidatorUtil;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * 讲师业务请求控制器
 *
 * @author March
 * @date 2020/7/8
 */
@RequestMapping("/admin/teacher")
@RestController
@Slf4j
@Api(tags = "admin-讲师操作的相关接口")
public class TeacherController {

    /** 服务接口 */
    private final TeacherService service;

    /** 相关业务 */
    public static final String BUSINESS_NAME = "讲师表";

    @Autowired
    public TeacherController(TeacherService service) {
        this.service = service;
    }


    /**
     * 获取讲师分页列表
     */
    @PostMapping("/list")
    public ResponseDto list(@RequestBody PageDto<TeacherDto> pageDto) {

        ValidatorUtil.require(pageDto, "分页参数");
        return service.list(pageDto);
    }


    /**
     * 获取所有讲师列表
     */
    @GetMapping("/all")
    public ResponseDto all() {

        return service.all();
    }



    /**
     *保存|修改讲师
     */
    @PostMapping("/save")
    public ResponseDto save(@RequestBody TeacherDto teacherDto) {

        ValidatorUtil.require(teacherDto.getName(), "姓名");
        ValidatorUtil.length(teacherDto.getName(), "姓名", 1, 50);
        ValidatorUtil.length(teacherDto.getNickname(), "昵称", 1, 50);
        ValidatorUtil.length(teacherDto.getImage(), "头像", 1, 100);
        ValidatorUtil.length(teacherDto.getPosition(), "职位", 1, 50);
        ValidatorUtil.length(teacherDto.getMotto(), "座右铭", 1, 50);
        ValidatorUtil.length(teacherDto.getIntro(), "简介", 1, 500);
        return service.save(teacherDto);
    }


    /**
     * 依据讲师ID删除讲师
     */
    @DeleteMapping("/delete/{id}")
    public ResponseDto delete(@PathVariable String id) {

        ValidatorUtil.require(id, "ID");
        ValidatorUtil.length(id, "ID", 8, 8);
        return service.delete(id);
    }

}
