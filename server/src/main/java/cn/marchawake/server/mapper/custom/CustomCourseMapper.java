package cn.marchawake.server.mapper.custom;

import cn.marchawake.server.dto.CourseDto;
import cn.marchawake.server.dto.CoursePageDto;
import cn.marchawake.server.dto.SortDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * <h1>自定义课程映射器</h1>
 *
 * @author MarchAwake
 * @date 2020/08/10
 */
public interface CustomCourseMapper {

    List<CourseDto> list(@Param("pageDto") CoursePageDto pageDto);

    int updateTime(@Param("courseId") String courseId);

    int updateSort(SortDto sortDto);

    int moveSortsBackward(SortDto sortDto);

    int moveSortsForward(SortDto sortDto);
}
