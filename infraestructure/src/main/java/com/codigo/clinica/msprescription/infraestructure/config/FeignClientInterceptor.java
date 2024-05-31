package com.codigo.clinica.msprescription.infraestructure.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class FeignClientInterceptor implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate requestTemplate) {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (requestAttributes != null) {
            String authorization = requestAttributes.getRequest().getHeader("Authorization");
            if(authorization != null && authorization.startsWith("Bearer ")){
                requestTemplate.header("Authorization", authorization);
            }
        }
    }
}
