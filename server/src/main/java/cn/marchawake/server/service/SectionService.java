package cn.marchawake.server.service;

import cn.marchawake.server.constant.ResponseTypeEnum;
import cn.marchawake.server.dto.SectionDto;
import cn.marchawake.server.dto.ResponseDto;
import cn.marchawake.server.dto.SectionPageDto;
import cn.marchawake.server.entity.Section;
import cn.marchawake.server.entity.SectionExample;
import cn.marchawake.server.mapper.SectionMapper;
import cn.marchawake.server.utils.CopyUtil;
import cn.marchawake.server.utils.UuidUtil;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import java.util.Date;
import java.util.List;

/**
 * <h1>Section 表服务接口实现定义</h1>
 *
 * @author March
 * @date 2020/7/2
 */
@Service
@Slf4j
public class SectionService {

    /** 数据访问接口 */
    private final SectionMapper sectionMapper;


    @Autowired
    public SectionService(SectionMapper sectionMapper) {
        this.sectionMapper = sectionMapper;
    }

    public ResponseDto list(SectionPageDto sectionPageDto) {

        /** 分页查询 */
        PageHelper.startPage(sectionPageDto.getPage(),sectionPageDto.getSize());

        SectionExample sectionExample = new SectionExample();
        SectionExample.Criteria criteria = sectionExample.createCriteria();

        if (!StringUtils.isEmpty(sectionPageDto.getCourseId())) {
            criteria.andCourseIdEqualTo(sectionPageDto.getCourseId());
        }
        if (!StringUtils.isEmpty(sectionPageDto.getChapterId())) {
            criteria.andChapterIdEqualTo(sectionPageDto.getChapterId());
        }

        sectionExample.setOrderByClause("sort asc");
        List<Section> sectionList = null;
        try {
            sectionList = sectionMapper.selectByExample(sectionExample);
        } catch (Exception e) {
            log.error(ResponseTypeEnum.ERROR.getDESC() + ": {}", JSON.toJSONString(e));
            return ResponseDto.error();
        }

        /** 设置 PageDto 总条数 */
        sectionPageDto.setTotal(new PageInfo<>(sectionList).getTotal());

        /** 将数据封装到 pageDto */
        sectionPageDto.setList(CopyUtil.copyList(sectionList, SectionDto.class));

        /** 将数据存放到 ResponseDto 并返回*/
        return ResponseDto.success(sectionPageDto);
    }

    @Transactional(rollbackFor = Exception.class)
    public ResponseDto save(SectionDto sectionDto) {
        Section section = CopyUtil.copy(sectionDto, Section.class);
        if (StringUtils.isEmpty(sectionDto.getId())) {
            return this.insert(section);
        }else {
            return this.update(section);
        }

    }

    /**
     * 依据小节 vod 查找课程
     */
    public ResponseDto findSectionByVod(String vod) {

        SectionExample example = new SectionExample();
        example.createCriteria().andVodEqualTo(vod);
        List<Section> sectionList = sectionMapper.selectByExample(example);

        if (CollectionUtils.isEmpty(sectionList)) {
            return ResponseDto.success();
        }

        return ResponseDto.success(sectionList.get(0));
    }


    private ResponseDto update(Section section) {
        section.setUpdateAt(new Date());
        return sectionMapper.updateByPrimaryKey(section) == 1 ?
                ResponseDto.success() : ResponseDto.error();
    }


    private ResponseDto insert(Section section) {

        section.setId(UuidUtil.getShortUuid());
        section.setCreatedAt(new Date());
        return sectionMapper.insert(section) == 1 ?
                ResponseDto.success() : ResponseDto.error();
    }

    @Transactional(rollbackFor = Exception.class)
    public ResponseDto delete(String id) {

        return sectionMapper.deleteByPrimaryKey(id) == 1 ?
            ResponseDto.success() : ResponseDto.error();
    }

    /**
     * 依据课程ID删除小节
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteByCourseId(String courseId) {

        SectionExample example = new SectionExample();
        example.createCriteria().andCourseIdEqualTo(courseId);
        sectionMapper.deleteByExample(example);
    }

    /**
     * 依据大章ID删除小节
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteByChapterId(String chapterId) {

        SectionExample example = new SectionExample();
        example.createCriteria().andChapterIdEqualTo(chapterId);
        sectionMapper.deleteByExample(example);
    }

    public List<SectionDto> findSectionsByCourseId(String courseId) {

        SectionExample example = new SectionExample();
        example.createCriteria().andCourseIdEqualTo(courseId);

        return CopyUtil.copyList(sectionMapper.selectByExample(example), SectionDto.class);
    }

}
