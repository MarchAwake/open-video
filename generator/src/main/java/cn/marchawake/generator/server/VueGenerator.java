package cn.marchawake.generator.server;

import cn.marchawake.generator.utils.DbUtil;
import cn.marchawake.generator.utils.Field;
import cn.marchawake.generator.utils.FreeMarkerUtil;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.util.*;

/**
 * <h1>公共模块代码生成</h1>
 *
 * @author March
 * @date 2020/7/12
 */
public class VueGenerator {

    private static final String MODULE = "business";
    private static final String GENERATOR_CONFIG_PATH = "server/src/main/resources/generator/generatorConfig.xml";
    private static final String TO_VUE_PATH = "admin/src/views/admin/";

    public static void main(String[] args) throws Exception {

        // 只生成配置文件中的第一个table节点
        File file = new File(GENERATOR_CONFIG_PATH);
        SAXReader reader=new SAXReader();
        //读取xml文件到Document中
        Document doc=reader.read(file);
        //获取xml文件的根节点
        Element rootElement=doc.getRootElement();
        //读取context节点
        Element contextElement = rootElement.element("context");
        //定义一个Element用于遍历
        Element tableElement;
        //取第一个“table”的节点
        tableElement=contextElement.elementIterator("table").next();
        String Domain = tableElement.attributeValue("domainObjectName");
        String tableName = tableElement.attributeValue("tableName");
        String tableNameCn = DbUtil.getTableComment(tableName);
        String domain = Domain.substring(0, 1).toLowerCase() + Domain.substring(1);
        System.out.println("表："+tableElement.attributeValue("tableName"));
        System.out.println("Domain："+tableElement.attributeValue("domainObjectName"));

        List<Field> fieldList = DbUtil.getColumnByTableName(tableName);
        Set<String> typeSet = getJavaTypes(fieldList);

        Map<String, Object> map = new HashMap<>();
        map.put("Domain", Domain);
        map.put("domain", domain);
        map.put("tableNameCn", tableNameCn);
        map.put("module", MODULE);
        map.put("fieldList", fieldList);
        map.put("typeSet", typeSet);


        /** 生成 vue 层*/
        FreeMarkerUtil.initConfig("vue.ftl");
        FreeMarkerUtil.generator(TO_VUE_PATH + domain + ".vue", map);

    }

    /**
     * 获取所有的Java类型，使用Set去重
     */
    private static Set<String> getJavaTypes(List<Field> fieldList) {
        Set<String> set = new HashSet<>();
        for (int i = 0; i < fieldList.size(); i++) {
            Field field = fieldList.get(i);
            set.add(field.getJavaType());
        }
        return set;
    }
}
