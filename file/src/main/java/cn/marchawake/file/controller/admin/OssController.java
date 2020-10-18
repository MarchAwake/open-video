package cn.marchawake.file.controller.admin;

import cn.marchawake.server.dto.FileDto;
import cn.marchawake.server.dto.ResponseDto;
import cn.marchawake.server.enums.FileUseEnum;
import cn.marchawake.server.service.FileService;
import cn.marchawake.server.utils.Base64ToMultipartFile;
import cn.marchawake.server.utils.ValidatorUtil;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.AppendObjectRequest;
import com.aliyun.oss.model.AppendObjectResult;
import com.aliyun.oss.model.ObjectMetadata;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.annotation.Resource;
import java.io.ByteArrayInputStream;

@RestController
@RequestMapping("/admin")
@Slf4j
public class OssController {

    @Value("${oss.accessKeyId}")
    private String accessKeyId;

    @Value("${oss.accessKeySecret}")
    private String accessKeySecret;

    @Value("${oss.endpoint}")
    private String endpoint;

    @Value("${oss.bucket}")
    private String bucket;

    @Value("${oss.domain}")
    private String ossDomain;

    public static final String BUSINESS_NAME = "文件上传";

    @Resource
    private FileService fileService;


    @PostMapping("/oss-append")
    public ResponseDto fileUpload(@RequestBody FileDto fileDto) throws Exception {

        ValidatorUtil.require(fileDto, "文件内容");

        log.info("上传文件开始");
        String use = fileDto.getUse();
        String key = fileDto.getKey();
        String suffix = fileDto.getSuffix();
        Integer shardIndex = fileDto.getShardIndex();
        Integer shardSize = fileDto.getShardSize();
        String shardBase64 = fileDto.getShard();
        MultipartFile shard = Base64ToMultipartFile.base64ToMultipart(shardBase64);

        // 保存文件到本地
        FileUseEnum useEnum = FileUseEnum.getByCode(use);

//        //如果文件夹不存在则创建
        assert useEnum != null;
        String dir = useEnum.name().toLowerCase();

        // String path = dir + File.separator + key + "." + suffix + "." + fileDto.getShardIndex();
        String path = new StringBuilder(dir)
                .append("/")
                .append(key)
                .append(".")
                .append(suffix)
                .toString(); // course\6sfSqfOwzmik4A4icMYuUe.mp4

        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        ObjectMetadata meta = new ObjectMetadata();
        // 指定上传的内容类型。
        meta.setContentType("text/plain");

        // 通过AppendObjectRequest设置多个参数。
        AppendObjectRequest appendObjectRequest = new AppendObjectRequest(bucket, path, new ByteArrayInputStream(shard.getBytes()),meta);


        // 第一次追加。
        // 设置文件的追加位置。
//        appendObjectRequest.setPosition(0L);
        appendObjectRequest.setPosition((long) ((shardIndex - 1) * shardSize));
        AppendObjectResult appendObjectResult = ossClient.appendObject(appendObjectRequest);
        // 文件的64位CRC值。此值根据ECMA-182标准计算得出。
        System.out.println(appendObjectResult.getObjectCRC());
        System.out.println(JSONObject.toJSONString(appendObjectResult));
        
        // 关闭OSSClient。
        ossClient.shutdown();

        log.info("保存文件记录开始");
        fileDto.setPath(path);
        fileService.save(fileDto);
        fileDto.setPath(ossDomain + path);
        
        return ResponseDto.success(fileDto);
    }

}
