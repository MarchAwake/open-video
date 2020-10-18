package cn.marchawake.server.utils;

import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;
import java.util.ArrayList;
import java.util.List;

/**
 * 拷贝对象数据工具类
 *
 * @author March
 * @date 2020/7/10
 */
public class CopyUtil {

    /**
     * <h2>将资源 list 的每个元素对象数据拷贝到指定对象，并把拷贝对象存放到 list 容器 </h2>
     * @param source {@link List}
     * @param clazz {@link Class}
     * @return java.util.List<T>
     */
    public static <T> List<T> copyList(List source, Class<T> clazz) {
        List<T> target = new ArrayList<>();
        if (!CollectionUtils.isEmpty(source)){
            if (!CollectionUtils.isEmpty(source)){
                for (Object c: source) {
                    T obj = copy(c, clazz);
                    target.add(obj);
                }
            }
        }
        return target;
    }

    /**
     * <h2>拷贝资源对象的数据到指定对象</h2>
     * @param source {@link Object}
     * @param clazz {@link Class}
     * @return T
     */
    public static <T> T copy(Object source, Class<T> clazz) {
        if (source == null) {
            return null;
        }
        T obj = null;
        try {
            obj = clazz.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        assert obj != null;
        BeanUtils.copyProperties(source, obj);
        return obj;
    }
}
