package cn.marchawake.business.controller.admin;

import cn.marchawake.server.dto.CourseContentFileDto;
import cn.marchawake.server.dto.PageDto;
import cn.marchawake.server.dto.ResponseDto;
import cn.marchawake.server.service.CourseContentFileService;
import cn.marchawake.server.utils.ValidatorUtil;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * <h1>课程内容文件请求控制器</h1>
 *
 * @author March
 * @date 2020/7/8
 */
@RequestMapping("/admin/course-content-file")
@RestController
@Slf4j
@Api(tags = "admin-课程内容文件操作的相关接口")
public class CourseContentFileController {

    /** 课程内容文件服务接口 */
    private final CourseContentFileService service;

    /** 业务名称 */
    public static final String BUSINESS_NAME = "课程内容文件表";

    @Autowired
    public CourseContentFileController(CourseContentFileService service) {
        this.service = service;
    }

    /**
     * 依据课程ID获取课程内容文件列表
     */
    @GetMapping("/list/{courseId}")
    public ResponseDto list(@PathVariable String courseId) {

        ValidatorUtil.require(courseId, "课程ID");
        return service.list(courseId);
    }


    /**
     * 保存|修改课程内容文件
     */
    @PostMapping("/save")
    public ResponseDto save(@RequestBody CourseContentFileDto courseContentFileDto) {

        ValidatorUtil.require(courseContentFileDto.getCourseId(), "课程ID");
        ValidatorUtil.length(courseContentFileDto.getUrl(), "地址", 1, 200);
        ValidatorUtil.length(courseContentFileDto.getName(), "文件名	", 1, 100);
        return service.save(courseContentFileDto);
    }


    /**
     * 根据课程内容文件ID删除单个课程内容文件
     */
    @DeleteMapping("/delete/{id}")
    public ResponseDto delete(@PathVariable String id) {

        ValidatorUtil.require(id, "课程内容文件ID");
        ValidatorUtil.length(id, "课程内容文件ID", 8, 8);
        return service.delete(id);
    }

}
