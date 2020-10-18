package cn.marchawake.server.service;

import cn.marchawake.server.dto.MemberCourseDto;
import cn.marchawake.server.dto.PageDto;
import cn.marchawake.server.dto.ResponseDto;
import cn.marchawake.server.entity.MemberCourse;
import cn.marchawake.server.entity.MemberCourseExample;
import cn.marchawake.server.mapper.MemberCourseMapper;
import cn.marchawake.server.utils.CopyUtil;
import cn.marchawake.server.utils.UuidUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import java.util.List;

/**
 * <h1>MemberCourse 表服务接口实现定义</h1>
 *
 * @author March
 * @date 2020/7/2
 */
@Service
@Slf4j
public class MemberCourseService {

    /** 数据访问接口 */
    private final MemberCourseMapper memberCourseMapper;

    @Autowired
    public MemberCourseService(MemberCourseMapper memberCourseMapper) {
        this.memberCourseMapper = memberCourseMapper;
    }

    public ResponseDto list(PageDto<MemberCourseDto> pageDto) {

        PageHelper.startPage(pageDto.getPage(),pageDto.getSize());
        MemberCourseExample memberCourseExample = new MemberCourseExample();
        List<MemberCourse> memberCourseList = memberCourseMapper.selectByExample(memberCourseExample);

        pageDto.setTotal(new PageInfo<>(memberCourseList).getTotal());
        pageDto.setList(CopyUtil.copyList(memberCourseList, MemberCourseDto.class));

        return ResponseDto.success(pageDto);
    }

    @Transactional(rollbackFor = Exception.class)
    public ResponseDto save(MemberCourseDto memberCourseDto) {

        MemberCourse memberCourse = CopyUtil.copy(memberCourseDto, MemberCourse.class);
        if (StringUtils.isEmpty(memberCourseDto.getId())) {
            return this.insert(memberCourse);
        }else {
            return this.update(memberCourse);
        }
    }

    private ResponseDto update(MemberCourse memberCourse) {

        return memberCourseMapper.updateByPrimaryKey(memberCourse) == 1 ?
                ResponseDto.success() : ResponseDto.error();
    }

    private ResponseDto insert(MemberCourse memberCourse) {

        memberCourse.setId(UuidUtil.getShortUuid());
        return memberCourseMapper.insert(memberCourse) == 1 ?
                ResponseDto.success() : ResponseDto.error();
    }

    @Transactional(rollbackFor = Exception.class)
    public ResponseDto delete(String id) {

        return memberCourseMapper.deleteByPrimaryKey(id) == 1 ?
            ResponseDto.success() : ResponseDto.error();
    }



    /**
     * 依据课程ID，会员ID查询报名状态,（没有报名进行数据插入）进行数据封装返回
     */
    public ResponseDto enroll(MemberCourseDto memberCourseDto) {

        MemberCourse memberCourseDb = this.select(memberCourseDto.getMemberId(), memberCourseDto.getCourseId());
        if (memberCourseDb == null) {
            MemberCourse memberCourse = CopyUtil.copy(memberCourseDto, MemberCourse.class);
            this.insert(memberCourse);
            return ResponseDto.success(CopyUtil.copy(memberCourse, MemberCourseDto.class)) ;
        } else {
            // 如果已经报名，则直接返回已报名的信息
            return ResponseDto.success(CopyUtil.copy(memberCourseDb, MemberCourseDto.class));
        }
    }

    /**
     * 根据会员ID和课程ID查询记录
     */
    private MemberCourse select(String memberId, String courseId) {

        MemberCourseExample example = new MemberCourseExample();
        example.createCriteria()
                .andCourseIdEqualTo(courseId)
                .andMemberIdEqualTo(memberId);
        List<MemberCourse> memberCourseList = memberCourseMapper.selectByExample(example);

        return  CollectionUtils.isEmpty(memberCourseList) ? null : memberCourseList.get(0);
    }

    /**
     * 获取报名信息
     */
    public ResponseDto getEnroll(MemberCourseDto memberCourseDto) {
        MemberCourse memberCourse = this.select(memberCourseDto.getMemberId(), memberCourseDto.getCourseId());
        return ResponseDto.success(CopyUtil.copy(memberCourse, MemberCourseDto.class));
    }
}
