package cn.marchawake.server.service;

import cn.marchawake.server.dto.CourseContentFileDto;
import cn.marchawake.server.dto.PageDto;
import cn.marchawake.server.dto.ResponseDto;
import cn.marchawake.server.entity.CourseContentFile;
import cn.marchawake.server.entity.CourseContentFileExample;
import cn.marchawake.server.mapper.CourseContentFileMapper;
import cn.marchawake.server.utils.CopyUtil;
import cn.marchawake.server.utils.UuidUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import java.util.List;

/**
 * <h1>CourseContentFile 表服务接口实现定义</h1>
 *
 * @author March
 * @date 2020/7/2
 */
@Service
@Slf4j
public class CourseContentFileService {

    /** 课程内容文件持久化接口 */
    private final CourseContentFileMapper courseContentFileMapper;

    @Autowired
    public CourseContentFileService(CourseContentFileMapper courseContentFileMapper) {
        this.courseContentFileMapper = courseContentFileMapper;
    }

    public ResponseDto list(String courseId) {

        CourseContentFileExample example = new CourseContentFileExample();
        CourseContentFileExample.Criteria criteria = example.createCriteria();
        criteria.andCourseIdEqualTo(courseId);
        List<CourseContentFile> courseContentFileList = courseContentFileMapper.selectByExample(example);

        return ResponseDto.success(CopyUtil.copyList(courseContentFileList, CourseContentFileDto.class));
    }

    @Transactional(rollbackFor = Exception.class)
    public ResponseDto save(CourseContentFileDto courseContentFileDto) {

        CourseContentFile courseContentFile = CopyUtil.copy(courseContentFileDto, CourseContentFile.class);
        if (StringUtils.isEmpty(courseContentFileDto.getId())) {
            return this.insert(courseContentFile);
        }else {
            return this.update(courseContentFile);
        }
    }

    private ResponseDto update(CourseContentFile courseContentFile) {

        return courseContentFileMapper.updateByPrimaryKey(courseContentFile) == 1 ?
                ResponseDto.success() : ResponseDto.error();
    }

    private ResponseDto insert(CourseContentFile courseContentFile) {

        courseContentFile.setId(UuidUtil.getShortUuid());
        return courseContentFileMapper.insert(courseContentFile) == 1 ?
                ResponseDto.success() : ResponseDto.error();
    }

    @Transactional(rollbackFor = Exception.class)
    public ResponseDto delete(String id) {

        return courseContentFileMapper.deleteByPrimaryKey(id) == 1 ?
            ResponseDto.success() : ResponseDto.error();
    }

}
