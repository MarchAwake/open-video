package cn.marchawake.server.service;

import cn.marchawake.server.dto.RoleUserDto;
import cn.marchawake.server.dto.PageDto;
import cn.marchawake.server.dto.ResponseDto;
import cn.marchawake.server.entity.RoleUser;
import cn.marchawake.server.entity.RoleUserExample;
import cn.marchawake.server.mapper.RoleUserMapper;
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
 * <h1>RoleUser 表服务接口实现定义</h1>
 *
 * @author March
 * @date 2020/7/2
 */
@Service
@Slf4j
public class RoleUserService {

    /** 数据访问接口 */
    private final RoleUserMapper roleUserMapper;

    @Autowired
    public RoleUserService(RoleUserMapper roleUserMapper) {
        this.roleUserMapper = roleUserMapper;
    }

    public ResponseDto list(PageDto<RoleUserDto> pageDto) {

        /** 分页查询 */
        PageHelper.startPage(pageDto.getPage(),pageDto.getSize());

        RoleUserExample roleUserExample = new RoleUserExample();
        List<RoleUser> roleUserList = roleUserMapper.selectByExample(roleUserExample);

        /** 设置 PageDto 总条数 */
        pageDto.setTotal(new PageInfo<>(roleUserList).getTotal());

        /** 将数据封装到 pageDto */
        pageDto.setList(CopyUtil.copyList(roleUserList, RoleUserDto.class));

        /** 将数据存放到 ResponseDto 并返回*/
        return ResponseDto.success(pageDto);
    }

    @Transactional(rollbackFor = Exception.class)
    public ResponseDto save(RoleUserDto roleUserDto) {

        RoleUser roleUser = CopyUtil.copy(roleUserDto, RoleUser.class);
        if (StringUtils.isEmpty(roleUserDto.getId())) {
            return this.insert(roleUser);
        }else {
            return this.update(roleUser);
        }
    }

    private ResponseDto update(RoleUser roleUser) {

        return roleUserMapper.updateByPrimaryKey(roleUser) == 1 ?
                ResponseDto.success() : ResponseDto.error();
    }

    private ResponseDto insert(RoleUser roleUser) {

        roleUser.setId(UuidUtil.getShortUuid());
        return roleUserMapper.insert(roleUser) == 1 ?
                ResponseDto.success() : ResponseDto.error();
    }

    @Transactional(rollbackFor = Exception.class)
    public ResponseDto delete(String id) {

        return roleUserMapper.deleteByPrimaryKey(id) == 1 ?
            ResponseDto.success() : ResponseDto.error();
    }

}
