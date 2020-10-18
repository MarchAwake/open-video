package cn.marchawake.gateway.filter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.Ordered;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class LoginAdminGatewayFilter implements GatewayFilter, Ordered {


    private final RedisTemplate<String, String> redisTemplate;

    public LoginAdminGatewayFilter(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
        // 设置序列化标准
        this.redisTemplate.setValueSerializer(new StringRedisSerializer());
        this.redisTemplate.setKeySerializer(new StringRedisSerializer());
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        String path = exchange.getRequest().getURI().getPath();
        return chain.filter(exchange);

//        if (!path.contains("admin")) {
//            return chain.filter(exchange);
//        }
//
//        boolean check =  path.contains("/system/admin/user/login")
//                || path.contains("/system/admin/user/logout")
//                || path.contains("/system/admin/kaptcha");
//
//        if (check) {
//            return chain.filter(exchange);
//        }
//
//
//        String token = exchange.getRequest().getHeaders().getFirst("token");
//
//        if (StringUtils.isEmpty(token)) {
//            log.info("Token Is Empty");
//            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
//            return exchange.getResponse().setComplete();
//        }
//
//        String data = redisTemplate.opsForValue().get(token);
//        if (StringUtils.isEmpty(data)) {
//            log.warn("Invalid Token");
//            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
//            return exchange.getResponse().setComplete();
//        }
//
//        // 增加权限校验，gateway里没有LoginUserDto，所以全部用JSON操作
//        boolean exist = false;
//        JSONObject userDto = JSON.parseObject(data);
//        JSONArray requests = userDto.getJSONArray("requests");
//        // 遍历所有【权限请求】，判断当前请求的地址是否在【权限请求】里
//        for (Object o : requests) {
//            String request = (String) o;
//            if (path.contains(request)) {
//                exist = true;
//                break;
//            }
//        }
//        if (!exist) {
//            log.warn("权限校验未通过" + path);
//            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
//            return exchange.getResponse().setComplete();
//        }
//
//        return chain.filter(exchange);
    }

    @Override
    public int getOrder()
    {
        return 1;
    }
}