package cn.marchawake.server.utils;
import cn.marchawake.server.exception.ValidatorException;
import org.springframework.util.StringUtils;


/**
 * 检验工具类定义
 *
 * @author March
 * @date 2020/7/10
 */
public class ValidatorUtil {

    /**
     * 对象空校验
     */
    public static void require(Object obj, String fieldName) {

        if (obj == null) {
            throw new ValidatorException(fieldName + "不能为空");
        }
    }


    /**
     * 空校验（null or ""）
     */
    public static void require(String str, String fieldName) {

        if (StringUtils.isEmpty(str)) {
            throw new ValidatorException(fieldName + "不能为空");
        }
    }


    /**
     * 长度校验
     */
    public static void length(String str, String fieldName, int min, int max) {

        if (StringUtils.isEmpty(str)) {
            return;
        }

        int length = str.length();

        if (length < min || length > max) {
            throw new ValidatorException(fieldName + "长度" + min + "~" + max + "位");
        }
    }
}
