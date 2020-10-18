package cn.marchawake.server.dto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <h1>课程数据传输对象定义</h1>
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "课程传输对象")
public class CourseDto {


    /**
    * id
    */
    private String id;


    /**
    * 名称
    */
    private String name;


    /**
    * 概述
    */
    private String summary;


    /**
    * 时长|单位秒
    */
    private Integer time;


    /**
    * 价格 (元)
    */
    private BigDecimal price;


    /**
    * 封面
    */
    private String image;


    /**
    * 级别|枚举[CourseLevelEnum]:("1","初级"),("2","中级"),("3","高级")
    */
    private String level;


    /**
    * 收费|枚举[CourseChargeEnum]:CHARGE("C","收费"),("F","免费")
    */
    private String charge;


    /**
    * 状态|枚举[CourseStatusEnum]:PUBLISH("P","发布"),DRAFT("D","草稿")
    */
    private String status;


    /**
    * 报名数
    */
    private Integer enroll;


    /**
    * 顺序
    */
    private Integer sort;

    /**
     * 讲师 ID
     */
    private String teacherId;

    /**
     * 讲师信息
     */
    private TeacherDto teacher;

    /**
     * 大章信息
     */
    private List<ChapterDto> chapterList;

    /**
     * 小节信息
     */
    private List<SectionDto> sectionList;

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
     * 课程内容
     */
    private String content;

    /**
     * 分类传输对象
     */
    List<CategoryDto> categorys;
}