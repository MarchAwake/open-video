package cn.marchawake.file.controller.admin;

import cn.marchawake.server.dto.FileDto;
import cn.marchawake.server.dto.ResponseDto;
import cn.marchawake.server.enums.FileUseEnum;
import cn.marchawake.server.service.FileService;
import cn.marchawake.server.utils.Base64ToMultipartFile;
import cn.marchawake.server.utils.UuidUtil;
import cn.marchawake.server.utils.ValidatorUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

/**
 * <h1>上传文件</h1>
 */

@RestController
@RequestMapping("/admin")
@Slf4j
public class UploadController {

    public static final String BUSINESS_NAME = "文件上传";

    @Value("${file.domain}")
    private String FILE_DOMAIN;

    @Value("${file.path}")
    private String FILE_PATH;

    private final FileService fileService;

    @Autowired
    public UploadController(FileService fileService) {
        this.fileService = fileService;
    }


    @PostMapping("/upload")
    public ResponseDto upload(@RequestBody FileDto fileDto) throws Exception {

        ValidatorUtil.require(fileDto, "文件内容");
        log.info("上传文件开始");

        String use = fileDto.getUse();
        String key = fileDto.getKey();
        String suffix = fileDto.getSuffix();
        String shardBase64 = fileDto.getShard();
        MultipartFile shard = Base64ToMultipartFile.base64ToMultipart(shardBase64);

        // 保存文件到本地
        FileUseEnum useEnum = FileUseEnum.getByCode(use);

        //如果文件夹不存在则创建
        String dir = useEnum.name().toLowerCase();
        File fullDir = new File(FILE_PATH + dir);
        if (!fullDir.exists()) {
            fullDir.mkdirs();
        }

//        String path = dir + File.separator + key + "." + suffix + "." + fileDto.getShardIndex();
        String path = new StringBuffer(dir)
                .append(File.separator)
                .append(key)
                .append(".")
                .append(suffix)
                .toString(); // course\6sfSqfOwzmik4A4icMYuUe.mp4
        String localPath = new StringBuffer(path)
                .append(".")
                .append(fileDto.getShardIndex())
                .toString(); // course\6sfSqfOwzmik4A4icMYuUe.mp4.1
        String fullPath = FILE_PATH + localPath;
        File dest = new File(fullPath);
        shard.transferTo(dest);
        log.info(dest.getAbsolutePath());

        log.info("保存文件记录开始");
        fileDto.setPath(path);
        fileDto.setVod(UuidUtil.getUuid());
        fileService.save(fileDto);

        fileDto.setPath(FILE_DOMAIN + path);


        if (fileDto.getShardIndex().equals(fileDto.getShardTotal())) {
            this.merge(fileDto);
        }

        return ResponseDto.success(fileDto);
    }

    @GetMapping("/check/{key}")
    public ResponseDto check(@PathVariable String key) {

        log.info("检查上传分片开始：{}", key);
        FileDto fileDto = fileService.findByKey(key);
        if (fileDto != null) {
            fileDto.setPath(FILE_DOMAIN + fileDto.getPath());
        }

        return ResponseDto.success(fileDto);
    }

    @GetMapping("/play/{vod}")
    public ResponseDto play(@PathVariable String vod) {

        ValidatorUtil.require(vod, "播放凭证");
        ValidatorUtil.length(vod, "播放凭证", 32, 32);
        FileDto fileDto = fileService.findByVod(vod);
        return ResponseDto.success(FILE_DOMAIN + fileDto.getPath());
    }

    /**
     * 合并视频
     */
    private void merge(FileDto fileDto) throws Exception {

        String path = fileDto.getPath(); //http://127.0.0.1:9000/file/f/course\6sfSqfOwzmik4A4icMYuUe.mp4
        path = path.replace(FILE_DOMAIN, ""); //course\6sfSqfOwzmik4A4icMYuUe.mp4

        byte[] buffer = new byte[10 * 1024 * 1024];
        int len = -1;
        Integer shardTotal = fileDto.getShardTotal();
        log.info("合并分片开始");
        for (int i = 1; i <= shardTotal; i++) {

            try(BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(FILE_PATH + path, true));
                BufferedInputStream bis = new BufferedInputStream(new FileInputStream(FILE_PATH + path + "." + i));) {
                // 读取第i个分片
                while ((len = bis.read(buffer)) != -1) {
                    bos.write(buffer,0, len);
                }

            } catch (IOException e) {
                log.error("分片合并异常", e);
            }
        }

        log.info("合并分片结束");

        System.gc();
        Thread.sleep(100);

        // 删除分片
        log.info("删除分片开始");
        for (int i = 1; i <= shardTotal; i++) {
            String filePath = FILE_PATH + path + "." + i;
            File file = new File(filePath);
            boolean result = file.delete();
            log.info("删除{}，{}", filePath, result ? "成功" : "失败");
        }
        log.info("删除分片结束");
    }
}
