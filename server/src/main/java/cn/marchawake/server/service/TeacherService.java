package cn.marchawake.server.service;

import cn.marchawake.server.dto.TeacherDto;
import cn.marchawake.server.dto.PageDto;
import cn.marchawake.server.dto.ResponseDto;
import cn.marchawake.server.entity.Teacher;
import cn.marchawake.server.entity.TeacherExample;
import cn.marchawake.server.mapper.TeacherMapper;
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
 * <h1>Teacher 表服务接口实现定义</h1>
 *
 * @author March
 * @date 2020/7/2
 */
@Service
@Slf4j
public class TeacherService {

    /** 数据访问接口 */
    private final TeacherMapper teacherMapper;

    @Autowired
    public TeacherService(TeacherMapper teacherMapper) {
        this.teacherMapper = teacherMapper;
    }

    /**
     * <h2>查找所有讲师</h2>
     */
    public ResponseDto all() {

        return ResponseDto.success(CopyUtil.copyList(
                teacherMapper.selectByExample(null),
                TeacherDto.class));
    }

    public ResponseDto list(PageDto<TeacherDto> pageDto) {

        /** 分页查询 */
        PageHelper.startPage(pageDto.getPage(),pageDto.getSize());

        TeacherExample teacherExample = new TeacherExample();
        List<Teacher> teacherList = teacherMapper.selectByExample(teacherExample);

        /** 设置 PageDto 总条数 */
        pageDto.setTotal(new PageInfo<>(teacherList).getTotal());

        /** 将数据封装到 pageDto */
        pageDto.setList(CopyUtil.copyList(teacherList, TeacherDto.class));

        /** 将数据存放到 ResponseDto 并返回*/
        return ResponseDto.success(pageDto);
    }

    @Transactional(rollbackFor = Exception.class)
    public ResponseDto save(TeacherDto teacherDto) {

        Teacher teacher = CopyUtil.copy(teacherDto, Teacher.class);
        if (StringUtils.isEmpty(teacherDto.getId())) {
            return this.insert(teacher);
        }else {
            return this.update(teacher);
        }
    }

    private ResponseDto update(Teacher teacher) {

        return teacherMapper.updateByPrimaryKey(teacher) == 1 ?
                ResponseDto.success() : ResponseDto.error();
    }

    private ResponseDto insert(Teacher teacher) {

        teacher.setId(UuidUtil.getShortUuid());
        return teacherMapper.insert(teacher) == 1 ?
                ResponseDto.success() : ResponseDto.error();
    }

    @Transactional(rollbackFor = Exception.class)
    public ResponseDto delete(String id) {

        return teacherMapper.deleteByPrimaryKey(id) == 1 ?
            ResponseDto.success() : ResponseDto.error();
    }


}
