package cn.marchawake.server.service;

import cn.marchawake.server.dto.${Domain}Dto;
import cn.marchawake.server.dto.PageDto;
import cn.marchawake.server.dto.ResponseDto;
import cn.marchawake.server.entity.${Domain};
import cn.marchawake.server.entity.${Domain}Example;
import cn.marchawake.server.mapper.${Domain}Mapper;
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
 * <h1>${Domain} 表服务接口实现定义</h1>
 *
 * @author March
 * @date 2020/7/2
 */
@Service
@Slf4j
public class ${Domain}Service {

    /** 数据访问接口 */
    private final ${Domain}Mapper ${domain}Mapper;

    @Autowired
    public ${Domain}Service(${Domain}Mapper ${domain}Mapper) {
        this.${domain}Mapper = ${domain}Mapper;
    }

    public ResponseDto list(PageDto<${Domain}Dto> pageDto) {

        /** 分页查询 */
        PageHelper.startPage(pageDto.getPage(),pageDto.getSize());

        ${Domain}Example ${domain}Example = new ${Domain}Example();
        <#list fieldList as field>
            <#if field.nameHump=='sort'>
        ${domain}Example.setOrderByClause("sort asc");
            </#if>
        </#list>
        List<${Domain}> ${domain}List = ${domain}Mapper.selectByExample(${domain}Example);

        /** 设置 PageDto 总条数 */
        pageDto.setTotal(new PageInfo<>(${domain}List).getTotal());

        /** 将数据封装到 pageDto */
        pageDto.setList(CopyUtil.copyList(${domain}List, ${Domain}Dto.class));

        /** 将数据存放到 ResponseDto 并返回*/
        return ResponseDto.success(pageDto);
    }

    @Transactional(rollbackFor = Exception.class)
    public ResponseDto save(${Domain}Dto ${domain}Dto) {

        ${Domain} ${domain} = CopyUtil.copy(${domain}Dto, ${Domain}.class);
        if (StringUtils.isEmpty(${domain}Dto.getId())) {
            return this.insert(${domain});
        }else {
            return this.update(${domain});
        }
    }

    private ResponseDto update(${Domain} ${domain}) {

        <#list fieldList as field>
            <#if field.nameHump=='updatedAt'>
        ${domain}.setUpdatedAt(new Date());
            </#if>
        </#list>
        return ${domain}Mapper.updateByPrimaryKey(${domain}) == 1 ?
                ResponseDto.success() : ResponseDto.error();
    }

    private ResponseDto insert(${Domain} ${domain}) {

        ${domain}.setId(UuidUtil.getShortUuid());
        <#list typeSet as type>
            <#if type=='Date'>
        Date now = new Date();
            </#if>
        </#list>
        <#list fieldList as field>
            <#if field.nameHump=='createdAt'>
        ${domain}.setCreatedAt(now);
            </#if>
            <#if field.nameHump=='updatedAt'>
        ${domain}.setUpdatedAt(now);
            </#if>
        </#list>
        return ${domain}Mapper.insert(${domain}) == 1 ?
                ResponseDto.success() : ResponseDto.error();
    }

    @Transactional(rollbackFor = Exception.class)
    public ResponseDto delete(String id) {

        return ${domain}Mapper.deleteByPrimaryKey(id) == 1 ?
            ResponseDto.success() : ResponseDto.error();
    }

}
