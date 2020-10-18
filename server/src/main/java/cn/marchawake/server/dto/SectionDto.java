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
@ApiModel(value = "小节传输对象")
public class SectionDto {

    /**
    * 小节ID
    */
    private String id;


    /**
    * 视频标识
    */
    private String vod;


    /**
    * 小节标题
    */
    private String title;


    /**
    * 课程ID|course.id
    */
    private String courseId;


    /**
    * 大章ID|chapter.id
    */
    private String chapterId;


    /**
    * 视频地址
    */
    private String video;


    /**
    * 时长|单位秒
    */
    private Integer time;


    /**
    * 收费|枚举[SectionChargeEnum]:C 代表收费、F 代表免费
    */
    private String charge;


    /**
    * 顺序
    */
    private Integer sort;


    /**
    * 创建时间
    */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createdAt;


    /**
    * 更新时间
    */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date updateAt;
}