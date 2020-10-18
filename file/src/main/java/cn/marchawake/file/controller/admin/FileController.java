package cn.marchawake.file.controller.admin;

import cn.marchawake.server.dto.FileDto;
import cn.marchawake.server.dto.PageDto;
import cn.marchawake.server.dto.ResponseDto;
import cn.marchawake.server.service.FileService;
import cn.marchawake.server.utils.ValidatorUtil;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * <h1>文件请求控制器</h1>
 *
 * @author March
 * @date 2020/7/8
 */
@RequestMapping("/admin/file")
@RestController
@Slf4j
public class FileController {

    /** 文件服务接口 */
    private final FileService service;

    /** 文件相关业务 */
    public static final String BUSINESS_NAME = "文件";

    @Autowired
    public  FileController(FileService service) {
        this.service = service;
    }

    /**
     * 获取文件列表
     */
    @PostMapping("/list")
    public ResponseDto list(@RequestBody PageDto<FileDto> pageDto) {
        
        return service.list(pageDto);
    }


    /**
     * 保存或修改文件
     */
    @PostMapping("/save")
    public ResponseDto save(@RequestBody FileDto fileDto) {

        // 保存校验
        ValidatorUtil.require(fileDto.getPath(), "相对路径");
        ValidatorUtil.length(fileDto.getPath(), "相对路径", 1, 100);
        ValidatorUtil.length(fileDto.getName(), "文件名", 1, 100);
        ValidatorUtil.length(fileDto.getSuffix(), "文件后缀", 1, 10);

        log.info("保存的小节： {}", JSON.toJSONString(fileDto));
        return service.save(fileDto);
    }
}
