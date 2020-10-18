package cn.marchawake.server.service;

import cn.marchawake.server.constant.BusinessModuleServiceStateEnum;
import cn.marchawake.server.dto.LoginMemberDto;
import cn.marchawake.server.dto.MemberDto;
import cn.marchawake.server.dto.PageDto;
import cn.marchawake.server.dto.ResponseDto;
import cn.marchawake.server.entity.Member;
import cn.marchawake.server.entity.MemberExample;
import cn.marchawake.server.mapper.MemberMapper;
import cn.marchawake.server.utils.CopyUtil;
import cn.marchawake.server.utils.UuidUtil;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * <h1>Member 表服务接口实现定义</h1>
 *
 * @author March
 * @date 2020/7/2
 */
@Service
@Slf4j
public class MemberService {

    /** 数据访问接口 */
    private final MemberMapper memberMapper;

    @Autowired
    public MemberService(MemberMapper memberMapper) {
        this.memberMapper = memberMapper;
    }

    public ResponseDto list(PageDto<MemberDto> pageDto) {

        PageHelper.startPage(pageDto.getPage(),pageDto.getSize());
        MemberExample memberExample = new MemberExample();
        List<Member> memberList = memberMapper.selectByExample(memberExample);

        pageDto.setTotal(new PageInfo<>(memberList).getTotal());
        pageDto.setList(CopyUtil.copyList(memberList, MemberDto.class));

        return ResponseDto.success(pageDto);
    }


    @Transactional(rollbackFor = Exception.class)
    public ResponseDto save(MemberDto memberDto) {

        Member memberDB = findMemberByEmail(memberDto.getEmail());
        if (memberDB != null) {
            return ResponseDto.success();
        }
        Member member = CopyUtil.copy(memberDto, Member.class);
        if (StringUtils.isEmpty(memberDto.getId())) {
            return this.insert(member);
        }else {
            return this.update(member);
        }
    }


    private ResponseDto update(Member member) {

        return memberMapper.updateByPrimaryKey(member) == 1 ?
                ResponseDto.success() : ResponseDto.error();
    }


    private ResponseDto insert(Member member) {

        member.setId(UuidUtil.getShortUuid());
        member.setRegisterTime(new Date());
        return memberMapper.insert(member) == 1 ?
                ResponseDto.success() : ResponseDto.error();
    }


    public ResponseDto login(MemberDto memberDto, RedisTemplate<String, String> redisTemplate) {

        Member memberDB = findMemberByEmail(memberDto.getEmail());

        // 判断会员是否存在
        if (memberDB == null) {
            return ResponseDto.failure(BusinessModuleServiceStateEnum.LOGIN_EMAIL_NOT_EXIST.getDesc());
        }

        // 判断密码是否正确
        if (!memberDB.getPassword().equals(DigestUtils.md5DigestAsHex(memberDto.getPassword().getBytes()))) {
            return  ResponseDto.failure(BusinessModuleServiceStateEnum.LOGIN_PASSWORD_ERROR.getDesc());
        }

        // 校验通过,封装数据返回
        LoginMemberDto loginMemberDto = new LoginMemberDto();
        loginMemberDto.setToken(UuidUtil.getShortUuid());
        loginMemberDto.setId(memberDB.getId());
        loginMemberDto.setPassword(memberDB.getPassword());
        loginMemberDto.setEmail(memberDB.getEmail());
        loginMemberDto.setName(memberDB.getName());
        loginMemberDto.setPhoto(memberDB.getPhoto());


        redisTemplate.opsForValue().set(loginMemberDto.getToken(), JSON.toJSONString(loginMemberDto), 3600, TimeUnit.SECONDS);
        return ResponseDto.success(loginMemberDto);
    }

    /**
     * 根据邮箱查找会员
     */
    public Member findMemberByEmail(String email) {

        MemberExample example = new MemberExample();
        example.createCriteria().andEmailEqualTo(email);
        List<Member> memberList = memberMapper.selectByExample(example);

        return CollectionUtils.isEmpty(memberList) ? null : memberList.get(0);
    }

    /**
     * 重置密码
     */
    public ResponseDto resetPassword(MemberDto memberDto) {

        Member memberDB = findMemberByEmail(memberDto.getEmail());
        if (memberDB == null) {
            return ResponseDto.failure(BusinessModuleServiceStateEnum.EMAIL_NOT_REGISTER.getDesc());
        }
        // 双重加密
        Member member = new Member();
        member.setId(memberDB.getId());
        member.setPassword(DigestUtils.md5DigestAsHex(memberDto.getPassword().getBytes()));

        return memberMapper.updateByPrimaryKeySelective(member) == 1 ? ResponseDto.success() : ResponseDto.error();
    }


    /**
     * 查询邮箱是否被注册, 已注册: data = true, 未注册: data = false
     */
    public ResponseDto isEmailExist(String email) {
        return findMemberByEmail(email) == null ? ResponseDto.success(false) : ResponseDto.success(true);
    }
}
