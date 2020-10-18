package cn.marchawake.file.controller.web;

import cn.marchawake.server.dto.FileDto;
import cn.marchawake.server.dto.ResponseDto;
import cn.marchawake.server.service.FileService;
import cn.marchawake.server.utils.ValidatorUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController("web")
@RequestMapping("/web")
public class PlayController {


    private final FileService service;

    @Autowired
    public PlayController(FileService service) {
        this.service = service;
    }


    @Value("${file.domain}")
    private String FILE_DOMAIN;


    @GetMapping("/play/{vod}")
    public ResponseDto play(@PathVariable String vod) {

        ValidatorUtil.require(vod, "播放凭证");
        ValidatorUtil.length(vod, "播放凭证", 32, 32);
        FileDto fileDto = service.findByVod(vod);
        return ResponseDto.success(FILE_DOMAIN + fileDto.getPath());
    }
}
