package cn.marchawake.server.config;

import cn.marchawake.server.utils.UuidUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.support.spring.PropertyPreFilters;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;


/**
 * 日志切面处理
 *
 * @author March
 * @date 2020/7/12
 */
@Aspect
@Slf4j
@Component
public class LogAspect {

    /** 定义一个切点 */
    @Pointcut("execution(public * cn.marchawake.*.controller..*Controller.*(..))")
    public void controllerPointcut() {

    }

    private String nameCn;
    private String businessName;

    @Before("controllerPointcut()")
    public void doBefore(JoinPoint joinPoint) {

        /** 日志编号 */
        MDC.put("UUID", UuidUtil.getShortUuid());

        /** 开始打印请求日志信息 */
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        /** 获取签名 */
        Signature signature = joinPoint.getSignature();
        String name = signature.getName();

        /** 打印业务操作 */
        if (name.contains("list") || name.contains("query")) {
            nameCn = "查询";
        }else if (name.contains("save")) {
            nameCn = "保存";
        }else if (name.contains("delete")) {
            nameCn = "删除";
        }else {
            nameCn = "其他操作";
        }

        /** 使用反射获取业务名称 */
        Class clazz = signature.getDeclaringType();
        Field field = null;


        try {
            field = clazz.getField("BUSINESS_NAME");
            if (!StringUtils.isEmpty(field)) {
                businessName = (String) field.get(clazz);
            }
        } catch (NoSuchFieldException e) {
            log.error("未获取到业务名称:{}", JSON.toJSONString(clazz));
        } catch (IllegalAccessException e) {
            log.error("获取业务名称失败:{}",JSON.toJSONString(field),e);
        }

        /** 打印请求信息 */
        log.info("---------------【{}】{}开始---------------", businessName, nameCn);
        log.info("请求地址: {} {}", request.getRequestURL().toString(), request.getMethod());
        log.info("类名方法：{}.{}", signature.getDeclaringTypeName(), name);

        /** 打印请求参数 */
        Object[] args = joinPoint.getArgs();
        Object[] arguments = new Object[args.length];
        for (int i = 0; i < args.length; i++) {
            if (args[i] instanceof ServletRequest
                    || args[i] instanceof ServletResponse
                    || args[i] instanceof MultipartFile) {
                continue;
            }
            arguments[i] = args[i];
        }

        /** 排除字段，敏感字段或者超长的字段不显示 */
        String[] excludeProperties = {"shard", "password"};
        PropertyPreFilters filters = new PropertyPreFilters();
        PropertyPreFilters.MySimplePropertyPreFilter excludeFilter = filters.addFilter();
        excludeFilter.addExcludes(excludeProperties);
        log.info("请求参数： {}", JSON.toJSONString(arguments, excludeFilter));
    }

    @Around("controllerPointcut()")
    public Object doAround(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {

        Long startTime = System.currentTimeMillis();
        Object result = proceedingJoinPoint.proceed();

        /** 排除字段，敏感字段或者超长的字段不显示 */
        String[] excludeProperties = {"shard", "password", "pwd"};
        PropertyPreFilters filters = new PropertyPreFilters();
        PropertyPreFilters.MySimplePropertyPreFilter excludeFilter = filters.addFilter();
        excludeFilter.addExcludes(excludeProperties);
        log.info("返回结果： {}", JSON.toJSONString(result, excludeFilter));
        log.info("--------------- 【{}】{}结束 耗时：{} ms ---------------",
                businessName, nameCn, System.currentTimeMillis() - startTime);

        return result;
    }
}
