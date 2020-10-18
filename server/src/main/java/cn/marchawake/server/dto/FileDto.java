package cn.marchawake.server.dto;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "文件传输对象")
public class FileDto {


    /**
    * id
    */
    private String id;


    /**
    * 相对路径
    */
    private String path;


    /**
    * 文件名
    */
    private String name;


    /**
    * 文件后缀
    */
    private String suffix;


    /**
    * 大小|字节B
    */
    private Integer size;


    /**
    * 用途|枚举[FileUseEnum]：COURSE("C", "课程"), TEACHER("T", "讲师")
    */
    private String use;


    /**
    * 创建时间
    */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createdAt;


    /**
    * 修改时间
    */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date updatedAt;

    /**
     * 分片开始索引
     */
    private Integer shardIndex;

    /**
     * 分片大小
     */
    private Integer shardSize;

    /**
     * 分片总数
     */
    private Integer shardTotal;

    /**
     * 分家标识
     */
    private String key;

    /**
     * base64
     */
    private String shard;


    /**
     * 视频点播
     */
    private String vod;


}