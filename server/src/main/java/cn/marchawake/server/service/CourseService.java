package cn.marchawake.server.service;

import cn.marchawake.server.constant.ResponseTypeEnum;
import cn.marchawake.server.dto.*;
import cn.marchawake.server.entity.*;
import cn.marchawake.server.enums.CourseStatusEnum;
import cn.marchawake.server.mapper.CourseCategoryMapper;
import cn.marchawake.server.mapper.CourseContentMapper;
import cn.marchawake.server.mapper.CourseMapper;
import cn.marchawake.server.mapper.TeacherMapper;
import cn.marchawake.server.mapper.custom.CustomCourseMapper;
import cn.marchawake.server.utils.CopyUtil;
import cn.marchawake.server.utils.UuidUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;

/**
 * <h1>Course 表服务接口实现定义</h1>
 *
 * @author March
 * @date 2020/7/2
 */
@Service
@Slf4j
public class CourseService {

    /**
     * 课程表持久化接口
     */
    private final CourseMapper courseMapper;

    /**
     * 自定义课程表持久化接口
     */
    private final CustomCourseMapper customCourseMapper;

    /**
     * 课程分类关联表持久化接口
     */
    private final CourseCategoryMapper courseCategoryMapper;

    /**
     * 课程内容表持久化接口
     */
    private final CourseContentMapper courseContentMapper;

    /**
     * 大章服务层接口
     */
    private final ChapterService chapterService;

    /**
     * 小节服务层接口
     */
    private final SectionService sectionService;

    /**
     * 讲师持久化接口
     */
    private final TeacherMapper teacherMapper;


    @Autowired
    public CourseService(CourseMapper courseMapper,
                         CustomCourseMapper customCourseMapper,
                         CourseCategoryMapper courseCategoryMapper,
                         CourseContentMapper courseContentMapper,
                         ChapterService chapterService,
                         SectionService sectionService,
                         TeacherMapper teacherMapper) {
        this.courseMapper = courseMapper;
        this.customCourseMapper = customCourseMapper;
        this.courseCategoryMapper = courseCategoryMapper;
        this.chapterService = chapterService;
        this.courseContentMapper = courseContentMapper;
        this.sectionService = sectionService;
        this.teacherMapper = teacherMapper;
    }

    @Transactional(rollbackFor = Exception.class)
    public ResponseDto list(CoursePageDto pageDto) {

        pageDto.setStatus(CourseStatusEnum.PUBLISH.getCode());
        PageHelper.startPage(pageDto.getPage(), pageDto.getSize());
        List<CourseDto> courseDtoList = customCourseMapper.list(pageDto);
        PageInfo<CourseDto> pageInfo = new PageInfo<>(courseDtoList);
        pageDto.setTotal(pageInfo.getTotal());
        pageDto.setList(courseDtoList);

        return ResponseDto.success(pageDto);
    }


    @Transactional(rollbackFor = Exception.class)
    public ResponseDto save(CourseDto courseDto) {

        try {
            Course course = CopyUtil.copy(courseDto, Course.class);
            if (StringUtils.isEmpty(courseDto.getId())) {
               this.insert(course);
            }else {
               this.update(course);
            }
            courseDto.setId(course.getId());
            saveBatch(courseDto);
            return ResponseDto.success();
        } catch (Exception e) {
            log.error(ResponseTypeEnum.ERROR.getDESC() + "{}", e);
            return ResponseDto.error();
        }
    }


    private void update(Course course) {

        course.setUpdatedAt(new Date());
        courseMapper.updateByPrimaryKey(course);
    }


    private void insert(Course course) {

        course.setId(UuidUtil.getShortUuid());
        Date now = new Date();
        course.setCreatedAt(now);
        course.setUpdatedAt(now);
        courseMapper.insert(course);
    }


    @Transactional(rollbackFor = Exception.class)
    public ResponseDto delete(String id) {

        // 依据课程ID删除大章和小节
        chapterService.deleteByCourseId(id);
        return courseMapper.deleteByPrimaryKey(id) == 1 ?
            ResponseDto.success() : ResponseDto.error();
    }


    /**
     * 更新课程时间
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateCourseTime(List<Course> courseList) {

        for (Course c : courseList) {
            customCourseMapper.updateTime(c.getId());
        }
    }


    /**
     * 根据课程 ID 查找课程关联分类的列表
     */
    public ResponseDto findCourseCategoryListById(String id) {

        CourseCategoryExample example  = new CourseCategoryExample();
        example.createCriteria().andCourseIdEqualTo(id);
        List<CourseCategory> courseCategoryList = courseCategoryMapper.selectByExample(example);
        return ResponseDto.success(CopyUtil.copyList(courseCategoryList, CourseCategoryDto.class));
    }


    /**
     * 保存课程关联分类的列表（先删除原有的再更新）
     */
    private void saveBatch(CourseDto courseDto) {

        CourseCategoryExample example = new CourseCategoryExample();
        example.createCriteria().andCourseIdEqualTo(courseDto.getId());
        courseCategoryMapper.deleteByExample(example);
        List<CategoryDto> categoryDtoList = courseDto.getCategorys();

        for (CategoryDto categoryDto : categoryDtoList) {
            CourseCategory courseCategory = new CourseCategory();
            courseCategory.setId(UuidUtil.getShortUuid());
            courseCategory.setCourseId(courseDto.getId());
            courseCategory.setCategoryId(categoryDto.getId());
            courseCategoryMapper.insert(courseCategory);
        }
    }


    /**
     * 依据课程 ID 查找课程内容
     */
    public ResponseDto findCourseContentById(String id) {

        CourseContent courseContent = courseContentMapper.selectByPrimaryKey(id);
        return courseContent == null ? ResponseDto.success() : ResponseDto.success(courseContent);
    }

    /**
     * 依据课程 ID 保存课程内容（先查后插入|更新）
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseDto saveCourseContentById(CourseContentDto courseContentDto) {

        CourseContent courseContent = CopyUtil.copy(courseContentDto, CourseContent.class);
        int effectRow = courseContentMapper.updateByPrimaryKeyWithBLOBs(courseContent);
        if (0 == effectRow) {
            courseContentMapper.insert(courseContent);
        }
        return ResponseDto.success();
    }

    /**
     * 排序（使用自定义 mapper）
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseDto sort(SortDto sortDto) {

        // 修改当前记录的排序值
        customCourseMapper.updateSort(sortDto);

        // 如果排序值变大
        if (sortDto.getNewSort() > sortDto.getOldSort()) {
            customCourseMapper.moveSortsForward(sortDto);
        }

        // 如果排序值变小
        if (sortDto.getNewSort() < sortDto.getOldSort()) {
            customCourseMapper.moveSortsBackward(sortDto);
        }

        return ResponseDto.success();
    }

    public ResponseDto getNewCourseList(PageDto<CourseDto> pageDto) {

        PageHelper.startPage(pageDto.getPage(),pageDto.getSize());
        CourseExample example = new CourseExample();
        example.setOrderByClause("created_at desc");
        example.createCriteria().andStatusEqualTo(CourseStatusEnum.PUBLISH.getCode());

        return ResponseDto.success(courseMapper.selectByExample(example));
    }

    /**
     * 查找某一课程，供web模块用，只能查已发布的
     */
    public ResponseDto findCourse(String id) {

        Course course = courseMapper.selectByPrimaryKey(id);
        if (course == null || !CourseStatusEnum.PUBLISH.getCode().equals(course.getStatus())) {
            return null;
        }
        CourseDto courseDto = CopyUtil.copy(course, CourseDto.class);

        // 查询内容
        CourseContent content = courseContentMapper.selectByPrimaryKey(id);
        if (content != null) {
            courseDto.setContent(content.getContent());
        }

        // 查找讲师信息
        Teacher teacher = teacherMapper.selectByPrimaryKey(courseDto.getTeacherId());
        courseDto.setTeacher(CopyUtil.copy(teacher, TeacherDto.class));

        // 查找章信息
        List<ChapterDto> chapterDtoList = chapterService.findChaptersByCourseId(id);
        courseDto.setChapterList(chapterDtoList);

        // 查找节信息
        List<SectionDto> sectionDtoList = sectionService.findSectionsByCourseId(id);
        courseDto.setSectionList(sectionDtoList);

        return ResponseDto.success(courseDto);
    }
}
