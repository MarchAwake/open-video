package cn.marchawake.business.controller.admin;

import cn.marchawake.server.dto.SectionDto;
import cn.marchawake.server.dto.ResponseDto;
import cn.marchawake.server.dto.SectionPageDto;
import cn.marchawake.server.service.SectionService;
import cn.marchawake.server.utils.ValidatorUtil;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * 小节业务请求控制器
 *
 * @author March
 * @date 2020/7/8
 */
@RequestMapping("/admin/section")
@RestController
@Slf4j
@Api(tags = "admin-小节操作的相关接口")
public class SectionController {

    /** 小节服务接口 */
    private final SectionService service;

    /** 业务名称 */
    public static final String BUSINESS_NAME = "小节表";

    @Autowired
    public SectionController(SectionService service) {
        this.service = service;
    }


    /**
     * 获取小节分页列表
     */
    @PostMapping("/list")
    public ResponseDto list(@RequestBody SectionPageDto sectionPageDto) {

        ValidatorUtil.require(sectionPageDto.getCourseId(), "课程ID");
        ValidatorUtil.length(sectionPageDto.getCourseId(), "小节ID", 8, 8);
        ValidatorUtil.require(sectionPageDto.getChapterId(), "大章ID");
        ValidatorUtil.length(sectionPageDto.getChapterId(), "小节ID", 8, 8);
        return service.list(sectionPageDto);
    }


    /**
     * 保存|修改小节
     */
    @PostMapping("/save")
    public ResponseDto save(@RequestBody SectionDto sectionDto) {

        ValidatorUtil.require(sectionDto.getTitle(), "小节标题");
        ValidatorUtil.length(sectionDto.getTitle(), "小节标题", 1, 50);
        ValidatorUtil.length(sectionDto.getVideo(), "视频地址", 1, 200);
        log.info("保存的小节： {}", JSON.toJSONString(sectionDto));
        return service.save(sectionDto);
    }

    @GetMapping("/find-section-by-vod/{vod}")
    public ResponseDto findSectionByVod(@PathVariable String vod) {

        return service.findSectionByVod(vod);
    }


    /**
     * 依据小节ID删除小节
     */
    @DeleteMapping("/delete/{id}")
    public ResponseDto delete(@PathVariable String id) {

        ValidatorUtil.require(id, "小节ID");
        ValidatorUtil.length(id, "小节ID", 8, 8);
        return service.delete(id);
    }

}
