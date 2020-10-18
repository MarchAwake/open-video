package cn.marchawake.server.service;

import cn.marchawake.server.dto.CategoryDto;
import cn.marchawake.server.dto.PageDto;
import cn.marchawake.server.dto.ResponseDto;
import cn.marchawake.server.entity.Category;
import cn.marchawake.server.entity.CategoryExample;
import cn.marchawake.server.mapper.CategoryMapper;
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
 * <h1>Category 表服务接口实现定义</h1>
 *
 * @author March
 * @date 2020/7/2
 */
@Service
@Slf4j
public class CategoryService {

    /** 数据访问接口 */
    private final CategoryMapper categoryMapper;

    @Autowired
    public CategoryService(CategoryMapper categoryMapper) {
        this.categoryMapper = categoryMapper;
    }

    public ResponseDto all() {

        CategoryExample categoryExample = new CategoryExample();
        categoryExample.setOrderByClause("sort asc");
        List<Category> categoryList = categoryMapper.selectByExample(categoryExample);
        return ResponseDto.success(CopyUtil.copyList(categoryList, CategoryDto.class));
    }


    @Transactional(rollbackFor = Exception.class)
    public ResponseDto save(CategoryDto categoryDto) {

        Category category = CopyUtil.copy(categoryDto, Category.class);
        if (StringUtils.isEmpty(categoryDto.getId())) {
            return this.insert(category);
        }else {
            return this.update(category);
        }
    }

    private ResponseDto update(Category category) {

        return categoryMapper.updateByPrimaryKey(category) == 1 ?
                ResponseDto.success() : ResponseDto.error();
    }

    private ResponseDto insert(Category category) {

        category.setId(UuidUtil.getShortUuid());
        return categoryMapper.insert(category) == 1 ?
                ResponseDto.success() : ResponseDto.error();
    }

    @Transactional(rollbackFor = Exception.class)
    public ResponseDto delete(String id) {

        deleteChildren(id);
        return categoryMapper.deleteByPrimaryKey(id) == 1 ?
            ResponseDto.success() : ResponseDto.error();
    }

    /**
     * 依据分类ID删除子类
     */
    private void deleteChildren(String id) {

        Category category = categoryMapper.selectByPrimaryKey(id);

        if ("00000000".equals(category.getParent())) {
            // 如果是父级分类就删除其下的子级分类
            CategoryExample categoryExample = new CategoryExample();
            categoryExample.createCriteria().andParentEqualTo(category.getId());
            categoryMapper.deleteByExample(categoryExample);
        }

    }

}
