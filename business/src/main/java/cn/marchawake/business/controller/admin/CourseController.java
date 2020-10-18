package cn.marchawake.business.controller.admin;

import cn.marchawake.server.dto.*;
import cn.marchawake.server.service.CourseService;
import cn.marchawake.server.utils.ValidatorUtil;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * 课程表请求控制器
 *
 * @author March
 * @date 2020/7/8
 */
@RequestMapping("/admin/course")
@RestController
@Slf4j
@Api(tags = "admin-课程操作的相关接口")
public class CourseController {

    private final CourseService service;

    /** 业务名称 */
    public static final String BUSINESS_NAME = "课程表";

    @Autowired
    public CourseController(CourseService service) {
        this.service = service;
    }

    /**
     * 获取课程表列表
     */
    @PostMapping("/list")
    public ResponseDto list(@RequestBody CoursePageDto pageDto) {
        ValidatorUtil.require(pageDto, "分页参数");
        return service.list(pageDto);
    }

    /**
     * 保存|修改课程
     */
    @PostMapping("/save")
    public ResponseDto save(@RequestBody CourseDto courseDto) {

        ValidatorUtil.require(courseDto.getName(), "名称");
        ValidatorUtil.length(courseDto.getName(), "名称", 1, 50);
        ValidatorUtil.require(courseDto.getSummary(), "概述");
        ValidatorUtil.length(courseDto.getSummary(), "概述", 1, 2000);

        return service.save(courseDto);
    }

    /**
     * 删除课程
     */
    @DeleteMapping("/delete/{id}")
    public ResponseDto delete(@PathVariable String id) {

        ValidatorUtil.require(id, "课程表ID");
        ValidatorUtil.length(id, "课程表ID", 8, 8);

        return service.delete(id);
    }

    /**
     * 依据课程ID获取课程关联分类的列表
     */
    @GetMapping("/get-course-category-list/{id}")
    public ResponseDto getCourseCategoryList(@PathVariable String id) {

        ValidatorUtil.require(id, "课程ID");
        ValidatorUtil.length(id, "课程ID",8,8);

        return service.findCourseCategoryListById(id);
    }

    /**
     * 依据课程ID保存课程内容
     */
    @PostMapping("/save-course-content")
    public ResponseDto saveCourseContent(@RequestBody CourseContentDto courseContentDto) {

        ValidatorUtil.require(courseContentDto.getId(), "课程ID");
        ValidatorUtil.length(courseContentDto.getId(), "课程ID",8,8);
        ValidatorUtil.require(courseContentDto.getContent(), "课程内容");

        return service.saveCourseContentById(courseContentDto);
    }

    /**
     * 依据课程ID获取课程内容
     */
    @GetMapping("/get-course-content/{id}")
    public ResponseDto getCourseContent(@PathVariable String id) {

        ValidatorUtil.require(id, "课程ID");
        ValidatorUtil.length(id, "课程ID",8,8);

        return service.findCourseContentById(id);
    }

    @PostMapping("/sort")
    public ResponseDto sort(@RequestBody SortDto sortDto) {

        ValidatorUtil.require(sortDto.getId(), "课程ID");
        ValidatorUtil.length(sortDto.getId(), "课程ID",8,8);
        ValidatorUtil.require(sortDto.getOldSort(), "旧排序");
        ValidatorUtil.require(sortDto.getNewSort(), "旧排序");
        log.info("更新排序");
        return service.sort(sortDto);
    }
}
