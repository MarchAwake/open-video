package cn.marchawake.server.mapper.custom;

import cn.marchawake.server.dto.ResourceDto;
import org.apache.ibatis.annotations.Param;
import java.util.List;


public interface CustomUserMapper {

    List<String> findRoleIds(@Param("userId") String userId);

    List<String> findResourceIds(@Param("roleIds") List<String> roleIds);

    List<ResourceDto> findResources(@Param("resourceIds") List<String> resourceIds);




}
