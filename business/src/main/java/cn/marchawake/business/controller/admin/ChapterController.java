package cn.marchawake.business.controller.admin;

import cn.marchawake.server.dto.ChapterDto;
import cn.marchawake.server.dto.ChapterPageDto;
import cn.marchawake.server.dto.ResponseDto;
import cn.marchawake.server.service.ChapterService;
import cn.marchawake.server.utils.ValidatorUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * 大章请求控制器
 *
 * @author March
 * @date 2020/7/8
 */
@RequestMapping("/admin/chapter")
@RestController
@Slf4j
@Api(tags = "admin-大章操作的相关接口")
public class ChapterController {

    /** 大章服务接口 */
    private final ChapterService service;

    /** 业务名称 */
    public static final String BUSINESS_NAME = "大章表";

    @Autowired
    public ChapterController(ChapterService service) {
        this.service = service;
    }

    /**
     * 依据课程ID获取大章分页列表
     */
    @PostMapping("/list")
    @ApiOperation(value = "/list", notes = "查询大章,要求传入JSON格式参数：当前页码, 每页显示条数, 课程ID")
    public ResponseDto list(@RequestBody ChapterPageDto chapterPageDto) {

        ValidatorUtil.require(chapterPageDto.getCourseId(), "课程ID");
        return service.list(chapterPageDto);
    }

    /**
     * 依据课程ID保存|修改大章
     */
    @PostMapping("/save")
    @ApiOperation(value = "/save", notes = "新增或者修改大章,要求传入JSON格式参数：大章ID(新增不用传入), 课程ID, 大章名称")
    public ResponseDto save(@RequestBody ChapterDto chapterDto) {

        ValidatorUtil.require(chapterDto.getName(), "大章名称");
        ValidatorUtil.require(chapterDto.getCourseId(), "课程ID");
        ValidatorUtil.length(chapterDto.getCourseId(), "课程ID", 8, 8);

        return service.save(chapterDto);
    }

    /**
     * 根据大章ID删除大章
     */
    @DeleteMapping("/delete/{id}")
    @ApiOperation(value = "/delete/{id}", notes = "依据大章ID删除大章, 大章ID要求为8位的字符串")
    public ResponseDto delete(@PathVariable @ApiParam(name = "大章ID", required = true) String id) {

        ValidatorUtil.require(id, "大章ID");
        ValidatorUtil.length(id, "大章ID", 8, 8);
        return service.delete(id);
    }

}
