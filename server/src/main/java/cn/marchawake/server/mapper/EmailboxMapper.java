package cn.marchawake.server.mapper;

import cn.marchawake.server.entity.Emailbox;
import cn.marchawake.server.entity.EmailboxExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface EmailboxMapper {
    long countByExample(EmailboxExample example);

    int deleteByExample(EmailboxExample example);

    int deleteByPrimaryKey(String id);

    int insert(Emailbox record);

    int insertSelective(Emailbox record);

    List<Emailbox> selectByExample(EmailboxExample example);

    Emailbox selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") Emailbox record, @Param("example") EmailboxExample example);

    int updateByExample(@Param("record") Emailbox record, @Param("example") EmailboxExample example);

    int updateByPrimaryKeySelective(Emailbox record);

    int updateByPrimaryKey(Emailbox record);
}