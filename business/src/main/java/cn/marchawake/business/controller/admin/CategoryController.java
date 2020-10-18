package cn.marchawake.business.controller.admin;

import cn.marchawake.server.dto.CategoryDto;
import cn.marchawake.server.dto.ResponseDto;
import cn.marchawake.server.service.CategoryService;
import cn.marchawake.server.utils.ValidatorUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * 类请求控制器
 *
 * @author March
 * @date 2020/7/8
 */
@RequestMapping("/admin/category")
@RestController
@Slf4j
@Api(tags = "admin-分类操作的相关接口")
public class CategoryController {

    /** 分类服务接口 */
    private final CategoryService service;

    /** 业务名称 */
    public static final String BUSINESS_NAME = "分类表";

    @Autowired
    public CategoryController(CategoryService service) {
        this.service = service;
    }


    /**
     * 获取所有分类列表
     */
    @ApiOperation("查找所有分类")
    @GetMapping("/all")
    public ResponseDto all() {
        return service.all();
    }


    /**
     * 保存|修改分类
     */
    @ApiOperation(value = "/save", notes = "新增或者修改分类,要求传入JSON格式参数：分类ID(新增不用传入), 分类的父ID(parent, 一级分类父ID为空), 分类名称")
    @PostMapping("/save")
    public ResponseDto save(@RequestBody  CategoryDto categoryDto) {

        ValidatorUtil.length(categoryDto.getName(), "名称", 1, 50);
        return service.save(categoryDto);
    }


    /**
     * 依据分类 ID 删除分类
     */
    @ApiOperation(value = "删除分类", notes = "依据分类 ID 删除分类,ID要求为8位的字符串")
    @DeleteMapping("/delete/{id}")
    public ResponseDto delete(@PathVariable @ApiParam(name = "分类ID", required = true) String id) {

        ValidatorUtil.require(id, "分类ID");
        ValidatorUtil.length(id, "分类ID", 8, 8);
        return service.delete(id);
    }

}
