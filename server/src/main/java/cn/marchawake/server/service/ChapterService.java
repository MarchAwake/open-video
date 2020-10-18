package cn.marchawake.server.service;

import cn.marchawake.server.constant.ResponseTypeEnum;
import cn.marchawake.server.dto.ChapterDto;
import cn.marchawake.server.dto.ChapterPageDto;
import cn.marchawake.server.dto.ResponseDto;
import cn.marchawake.server.entity.Chapter;
import cn.marchawake.server.entity.ChapterExample;
import cn.marchawake.server.mapper.ChapterMapper;
import cn.marchawake.server.utils.CopyUtil;
import cn.marchawake.server.utils.UuidUtil;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import java.util.List;

/**
 * <h1>Chapter 表服务接口实现定义</h1>
 *
 * @author March
 * @date 2020/7/2
 */
@Service
@Slf4j
public class ChapterService {

    private final ChapterMapper chapterMapper;

    private final SectionService sectionService;

    @Autowired
    public ChapterService(ChapterMapper chapterMapper, SectionService sectionService) {
        this.chapterMapper = chapterMapper;
        this.sectionService = sectionService;
    }

    /**
     * <h2>查询 Chapter 数据到 ResponseDto </h2>
     * @return {@link ResponseDto}
     */
    public ResponseDto list(ChapterPageDto chapterPageDto) {

        /** 分页查询 */
        PageHelper.startPage(chapterPageDto.getPage(),chapterPageDto.getSize());

        ChapterExample chapterExample = new ChapterExample();
        ChapterExample.Criteria criteria = chapterExample.createCriteria();

        // 有课程 id 按照 课程 id 查找
        if (!StringUtils.isEmpty(chapterPageDto.getCourseId())) {
            criteria.andCourseIdEqualTo(chapterPageDto.getCourseId());
        }

        List<Chapter> chapterList = null;
        try {
            chapterList = chapterMapper.selectByExample(chapterExample);
        } catch (Exception e) {
            log.error(ResponseTypeEnum.ERROR.getDESC() + ": {}", JSON.toJSONString(e));
            return ResponseDto.error();
        }

        /** 设置 PageDto 总条数 */
        chapterPageDto.setTotal(new PageInfo<>(chapterList).getTotal());

        /** 将数据封装到 pageDto */
        chapterPageDto.setList(CopyUtil.copyList(chapterList, ChapterDto.class));

        /** 将数据存放到 ResponseDto 并返回*/
        return ResponseDto.success(chapterPageDto);
    }

    @Transactional(rollbackFor = Exception.class)
    public ResponseDto save(ChapterDto chapterDto) {
        Chapter chapter = CopyUtil.copy(chapterDto, Chapter.class);
        if (StringUtils.isEmpty(chapterDto.getId())) {
            return this.insert(chapter);
        }else {
            return this.update(chapter);
        }
    }


    private ResponseDto update(Chapter chapter) {

        return chapterMapper.updateByPrimaryKey(chapter) == 1 ?
                ResponseDto.success() : ResponseDto.error();
    }

    private ResponseDto insert(Chapter chapter) {

        chapter.setId(UuidUtil.getShortUuid());
        return chapterMapper.insert(chapter) == 1 ?
                ResponseDto.success() : ResponseDto.error();
    }

    @Transactional(rollbackFor = Exception.class)
    public ResponseDto delete(String id) {

        sectionService.deleteByChapterId(id);
        return chapterMapper.deleteByPrimaryKey(id) == 1 ?
            ResponseDto.success() : ResponseDto.error();
    }

    /**
     * 依据课程ID删除大章和小节
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteByCourseId(String courseId) {

        sectionService.deleteByCourseId(courseId);
        ChapterExample example = new ChapterExample();
        example.createCriteria().andCourseIdEqualTo(courseId);
        chapterMapper.deleteByExample(example);
    }

    public List<ChapterDto> findChaptersByCourseId(String courseId) {

        ChapterExample example = new ChapterExample();
        example.createCriteria().andCourseIdEqualTo(courseId);

        return CopyUtil.copyList(chapterMapper.selectByExample(example) ,ChapterDto.class);
    }

}
