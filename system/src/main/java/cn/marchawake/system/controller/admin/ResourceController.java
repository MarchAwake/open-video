package cn.marchawake.system.controller.admin;

import cn.marchawake.server.dto.ResponseDto;
import cn.marchawake.server.service.ResourceService;
import cn.marchawake.server.utils.ValidatorUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * <h1>请求控制器</h1>
 *
 * @author March
 * @date 2020/09/21
 */
@RequestMapping("/admin/resource")
@RestController
@Slf4j
public class ResourceController {

    private final ResourceService service;

    public static final String BUSINESS_NAME = "RESOURCE";

    @Autowired
    public ResourceController(ResourceService service) {
        this.service = service;
    }


    /**
     * 加载资源树图
     */
    @GetMapping("/load-tree")
    public ResponseDto loadTree() {
        
        return service.loadTree();
    }


    /**
     * 保存
     */
    @PostMapping("/save")
    public ResponseDto save(@RequestBody String jsonString) {

        ValidatorUtil.require(jsonString, "JSON字符串");
        return service.saveJson(jsonString);
    }

}
