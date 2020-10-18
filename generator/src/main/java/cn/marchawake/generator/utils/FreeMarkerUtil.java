package cn.marchawake.generator.utils;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 模板代码生成器工具类
 *
 * @author March
 * @date 2020/7/12
 */
public class FreeMarkerUtil {

    static String ftlPath = "generator\\src\\main\\java\\cn\\marchawake\\generator\\ftl\\";

    static Template temp;

    /**
     * <h2>初始化模板配置</h2>
     * @param ftlName 模板名
     * @return void
     */
    public static void initConfig(String ftlName) throws IOException {
        Configuration cfg = new Configuration(Configuration.VERSION_2_3_29);
        cfg.setDirectoryForTemplateLoading(new File(ftlPath));
        cfg.setObjectWrapper(new DefaultObjectWrapper(Configuration.VERSION_2_3_29));
        temp = cfg.getTemplate(ftlName);
    }

    /**
     * <h2>根据模板生成文件</h2>
     * @param fileName 生成的文件名
     * @param map {@link Map}
     * @return void
     */
    public static void generator(String fileName, Map<String, Object> map) throws IOException, TemplateException {
        FileWriter fw = new FileWriter(fileName);
        BufferedWriter bw = new BufferedWriter(fw);
        temp.process(map, bw);
        bw.flush();
        fw.close();
    }

}
