package cn.marchawake.business.controller.web;

import cn.marchawake.server.dto.ResponseDto;
import cn.marchawake.server.service.CategoryService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("webCategoryController")
@RequestMapping("/web/category")
@Api(tags = "web-分类操作的相关接口")
public class CategoryController {

    public static final String BUSINESS_NAME = "分类";


    private final CategoryService service;

    @Autowired
    public CategoryController(CategoryService service) {
        this.service = service;
    }

    /**
     * 列表查询
     */
    @PostMapping("/all")
    public ResponseDto all() {

        return service.all();
    }
}
