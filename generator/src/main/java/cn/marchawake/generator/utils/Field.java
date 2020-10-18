package cn.marchawake.generator.utils;

import lombok.*;

/**
 * 表属性的工具类
 *
 * @author March
 * @date 2020/7/13
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Field {

    /** 字段名，例如：course_id */
    private String name;

    /** 字段名，例如：course_id */
    private String nameHump; // 字段名小驼峰：courseId

    /** 字段名，例如：course_id */
    private String nameBigHump; // 字段名大驼峰：CourseId

    /** 字段名，例如：course_id */
    private String nameCn; // 中文名：课程

    /** 字段名，例如：course_id */
    private String type; // 字段类型：char(8)

    /** 字段名，例如：course_id */
    private String javaType; // java类型：String

    /** 字段名，例如：course_id */
    private String comment; // 注释：课程|ID

    /** 字段名，例如：course_id */
    private Boolean nullAble; // 是否可为空

    /** 字段名，例如：course_id */
    private Integer length; // 字符串长度

    /** 字段名，例如：course_id */
    private Boolean enums; // 是否是枚举

    /** 字段名，例如：course_id */
    private String enumsConst; // 枚举常量 COURSE_LEVEL

}
