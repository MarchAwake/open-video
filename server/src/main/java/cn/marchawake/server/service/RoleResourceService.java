package cn.marchawake.server.service;

import cn.marchawake.server.dto.RoleResourceDto;
import cn.marchawake.server.dto.PageDto;
import cn.marchawake.server.dto.ResponseDto;
import cn.marchawake.server.entity.RoleResource;
import cn.marchawake.server.entity.RoleResourceExample;
import cn.marchawake.server.mapper.RoleResourceMapper;
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
 * <h1>RoleResource 表服务接口实现定义</h1>
 *
 * @author March
 * @date 2020/7/2
 */
@Service
@Slf4j
public class RoleResourceService {

    /** 数据访问接口 */
    private final RoleResourceMapper roleResourceMapper;

    @Autowired
    public RoleResourceService(RoleResourceMapper roleResourceMapper) {
        this.roleResourceMapper = roleResourceMapper;
    }

    public ResponseDto list(PageDto<RoleResourceDto> pageDto) {

        /** 分页查询 */
        PageHelper.startPage(pageDto.getPage(),pageDto.getSize());

        RoleResourceExample roleResourceExample = new RoleResourceExample();
        List<RoleResource> roleResourceList = roleResourceMapper.selectByExample(roleResourceExample);

        /** 设置 PageDto 总条数 */
        pageDto.setTotal(new PageInfo<>(roleResourceList).getTotal());

        /** 将数据封装到 pageDto */
        pageDto.setList(CopyUtil.copyList(roleResourceList, RoleResourceDto.class));

        /** 将数据存放到 ResponseDto 并返回*/
        return ResponseDto.success(pageDto);
    }

    @Transactional(rollbackFor = Exception.class)
    public ResponseDto save(RoleResourceDto roleResourceDto) {

        RoleResource roleResource = CopyUtil.copy(roleResourceDto, RoleResource.class);
        if (StringUtils.isEmpty(roleResourceDto.getId())) {
            return this.insert(roleResource);
        }else {
            return this.update(roleResource);
        }
    }

    private ResponseDto update(RoleResource roleResource) {

        return roleResourceMapper.updateByPrimaryKey(roleResource) == 1 ?
                ResponseDto.success() : ResponseDto.error();
    }

    private ResponseDto insert(RoleResource roleResource) {

        roleResource.setId(UuidUtil.getShortUuid());
        return roleResourceMapper.insert(roleResource) == 1 ?
                ResponseDto.success() : ResponseDto.error();
    }

    @Transactional(rollbackFor = Exception.class)
    public ResponseDto delete(String id) {

        return roleResourceMapper.deleteByPrimaryKey(id) == 1 ?
            ResponseDto.success() : ResponseDto.error();
    }

}
