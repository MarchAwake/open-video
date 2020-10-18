package cn.marchawake.${module}.controller.admin;

import cn.marchawake.server.dto.${Domain}Dto;
import cn.marchawake.server.dto.PageDto;
import cn.marchawake.server.dto.ResponseDto;
import cn.marchawake.server.service.${Domain}Service;
import cn.marchawake.server.utils.ValidatorUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * <h1>${tableNameCn}请求控制器</h1>
 *
 * @author March
 * @date 2020/7/8
 */
@RequestMapping("/admin/${domain}")
@RestController
@Slf4j
public class ${Domain}Controller {

    /** ${tableNameCn}服务接口 */
    private final ${Domain}Service service;

    /** ${tableNameCn}相关业务 */
    public static final String BUSINESS_NAME = "${tableNameCn}";

    @Autowired
    public ${Domain}Controller(${Domain}Service service) {
        this.service = service;
    }

    /**
     * <h2>获取${tableNameCn}列表</h2>
     * @param pageDto {@link PageDto}
     * @return {@link PageDto}
     */
    @PostMapping("/list")
    public ResponseDto list(@RequestBody PageDto<${Domain}Dto> pageDto) {
        
        return service.list(pageDto);
    }

    /**
     * <h2>保存或修改${tableNameCn}</h2>
     * @param ${domain}Dto {@link ${Domain}Dto}
     * @return {@link ${Domain}Dto}
     */
    @PostMapping("/save")
    public ResponseDto save(@RequestBody ${Domain}Dto ${domain}Dto) {
        // 保存校验
        <#list fieldList as field>
            <#if field.name!="id" && field.nameHump!="createdAt" && field.nameHump!="updatedAt" && field.nameHump!="sort">
                <#if !field.nullAble>
        ValidatorUtil.require(${domain}Dto.get${field.nameBigHump}(), "${field.nameCn}");
                </#if>
                <#if (field.length > 0)>
        ValidatorUtil.length(${domain}Dto.get${field.nameBigHump}(), "${field.nameCn}", 1, ${field.length?c});
                </#if>
            </#if>
        </#list>
        return service.save(${domain}Dto);
    }

    /**
     * <h2>根据${tableNameCn} ID 删除${tableNameCn}</h2>
     * @param id
     * @return {@link ResponseDto}
     */
    @DeleteMapping("/delete/{id}")
    public ResponseDto delete(@PathVariable String id) {
        ValidatorUtil.require(id, "${tableNameCn}ID");
        ValidatorUtil.length(id, "${tableNameCn}ID", 1, 8);
        return service.delete(id);
    }

}
