package cn.marchawake.server.service;

import cn.marchawake.server.constant.BusinessModuleServiceStateEnum;
import cn.marchawake.server.dto.EmailboxDto;
import cn.marchawake.server.dto.MemberDto;
import cn.marchawake.server.dto.PageDto;
import cn.marchawake.server.dto.ResponseDto;
import cn.marchawake.server.entity.Emailbox;
import cn.marchawake.server.entity.EmailboxExample;
import cn.marchawake.server.enums.EmailboxStatusEnum;
import cn.marchawake.server.enums.EmailboxUseEnum;
import cn.marchawake.server.mapper.EmailboxMapper;
import cn.marchawake.server.utils.CopyUtil;
import cn.marchawake.server.utils.UuidUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;

/**
 * <h1>Emailbox 表服务接口实现定义</h1>
 *
 * @author March
 * @date 2020/7/2
 */
@Service
@Slf4j
public class EmailboxService {

    /** 数据访问接口 */
    private final EmailboxMapper emailboxMapper;

    @Autowired
    public EmailboxService(EmailboxMapper emailboxMapper) {
        this.emailboxMapper = emailboxMapper;
    }

    public ResponseDto list(PageDto<EmailboxDto> pageDto) {

        PageHelper.startPage(pageDto.getPage(),pageDto.getSize());
        EmailboxExample emailboxExample = new EmailboxExample();
        List<Emailbox> emailboxList = emailboxMapper.selectByExample(emailboxExample);

        pageDto.setTotal(new PageInfo<>(emailboxList).getTotal());
        pageDto.setList(CopyUtil.copyList(emailboxList, EmailboxDto.class));

        return ResponseDto.success(pageDto);
    }

    @Transactional(rollbackFor = Exception.class)
    public ResponseDto save(EmailboxDto emailboxDto) {

        Emailbox emailbox = CopyUtil.copy(emailboxDto, Emailbox.class);
        emailbox.setId(UuidUtil.getShortUuid());
        emailbox.setCreatedAt(new Date());
        return emailboxMapper.insert(emailbox) == 1 ?
                ResponseDto.success() : ResponseDto.error();
    }

    /**
     * 依据 邮箱,用途,时间间隔 查找邮箱验证码, 与前端输入的邮箱验证码进行比对
     * @param memberDto 会员数据
     * @param use 邮箱验证码的用途
     */
    public ResponseDto validCode(MemberDto memberDto, EmailboxUseEnum use) {

        EmailboxExample example = new EmailboxExample();
        example.createCriteria().andEmailEqualTo(memberDto.getEmail())
                .andUseEqualTo(use.getCode())
                .andCreatedAtGreaterThan(new Date(System.currentTimeMillis() - 5 * 60 * 1000));

        List<Emailbox> emailboxList = emailboxMapper.selectByExample(example);

        if (CollectionUtils.isEmpty(emailboxList)) {

            log.warn("邮箱验证码已过期");
            return ResponseDto.failure(BusinessModuleServiceStateEnum.EMAIL_CODE_EXPIRED.getDesc());
        }

        Emailbox emailboxDB = emailboxList.get(0);
        if (emailboxDB.getCode().equals(memberDto.getEmailCode())) {

            emailboxDB.setStatus(EmailboxStatusEnum.USED.getCode());
            emailboxMapper.updateByPrimaryKey(emailboxDB);
            return ResponseDto.success();
        } else {

            log.warn("邮箱验证码错误");
            return ResponseDto.failure(BusinessModuleServiceStateEnum.EMAIL_CODE_ERROR.getDesc());
        }
    }

    /**
     * 发送邮箱验证码
     * 1分钟内不能重复发送验证码
     */
    public ResponseDto sendCode(EmailboxDto emailboxDto) {

        EmailboxExample example = new EmailboxExample();
        example.createCriteria().andEmailEqualTo(emailboxDto.getEmail())
                .andUseEqualTo(emailboxDto.getUse())
                .andCreatedAtGreaterThan(new Date(System.currentTimeMillis() - 60 * 1000));
        List<Emailbox> emailboxList = emailboxMapper.selectByExample(example);

        if (CollectionUtils.isEmpty(emailboxList)) {

            saveAndSend(emailboxDto);
            return ResponseDto.success();
        } else {
            log.warn("请求验证码过于频繁");
            return ResponseDto.failure(BusinessModuleServiceStateEnum.EMAIL_CODE_TOO_FREQUENT.getDesc());
        }
    }

    /**
     * 保存并发送邮箱验证码
     */
    private void saveAndSend(EmailboxDto emailboxDto) {

        // 生成6位数字
        String code = String.valueOf((int)(((Math.random() * 9) + 1) * 100000));
        emailboxDto.setCreatedAt(new Date());
        emailboxDto.setStatus(EmailboxStatusEnum.NOT_USED.getCode());
        emailboxDto.setCode(code);
        this.save(emailboxDto);

        HtmlEmail email = new HtmlEmail();
        try {
            //需要修改，126邮箱为smtp.126.com,163邮箱为163.smtp.com，QQ为smtp.qq.com
            email.setHostName("smtp.qq.com");

            // 设置编码
            email.setCharset("utf-8");

            //设置发送人到的邮箱和用户名和授权码(授权码是自己设置的)
            email.setAuthentication("2633609468@qq.com","");

            //发送人的邮箱为自己的，用户名可以随便填
            email.setFrom("2633609468@qq.com","开源视频");

            // 设置收件人
            email.addTo(emailboxDto.getEmail());

            //此处填写邮件名，邮件名可任意填写
            email.setSubject("开源视频");

            // 设置发送信息
            email.setTextMsg("尊敬的会员,您本次操作的验证码为: " + code);

            email.setSmtpPort(587);
            // 发送
            email.send();
        } catch (EmailException e) {
            e.printStackTrace();
        }

    }


}
