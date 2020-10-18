package cn.marchawake.business.controller.web;

import cn.marchawake.server.dto.CourseDto;
import cn.marchawake.server.dto.CoursePageDto;
import cn.marchawake.server.dto.PageDto;
import cn.marchawake.server.dto.ResponseDto;
import cn.marchawake.server.enums.CourseStatusEnum;
import cn.marchawake.server.service.CourseService;
import cn.marchawake.server.utils.ValidatorUtil;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController("webCourseController")
@Slf4j
@RequestMapping("/web/course")
@Api(tags = "web-课程操作的相关接口")
public class CourseController {

    /** 业务名称 */
    public static final String BUSINESS_NAME = "获取课程";

    private final CourseService service;

    @Autowired
    public CourseController(CourseService service) {
        this.service = service;
    }


    @GetMapping("/new-list")
    public ResponseDto getNewCourseList() {

        PageDto<CourseDto> pageDto = new PageDto<>();
        pageDto.setPage(1);
        pageDto.setSize(3);

        return service.getNewCourseList(pageDto);
    }


    /**
     * 列表查询
     */
    @PostMapping("/list")
    public ResponseDto list(@RequestBody CoursePageDto pageDto) {

        ValidatorUtil.require(pageDto, "分页参数");
        return service.list(pageDto);
    }

    @GetMapping("/find/{id}")
    public ResponseDto findCourse(@PathVariable String id) {

        ValidatorUtil.require(id, "课程ID");
        ValidatorUtil.length(id, "课程ID", 8, 8);
        return service.findCourse(id);
    }
}
