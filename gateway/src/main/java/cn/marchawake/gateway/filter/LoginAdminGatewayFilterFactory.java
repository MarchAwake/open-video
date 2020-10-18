package cn.marchawake.gateway.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;


@Component
public class LoginAdminGatewayFilterFactory extends AbstractGatewayFilterFactory<Object> {

    private final LoginAdminGatewayFilter loginAdminGatewayFilter;

    @Autowired
    public LoginAdminGatewayFilterFactory(LoginAdminGatewayFilter loginAdminGatewayFilter) {
        this.loginAdminGatewayFilter = loginAdminGatewayFilter;
    }

    @Override
    public GatewayFilter apply(Object config) {
        return loginAdminGatewayFilter;
    }
}