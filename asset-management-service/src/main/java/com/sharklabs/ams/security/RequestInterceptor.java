package com.sharklabs.ams.security;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component

public class RequestInterceptor extends HandlerInterceptorAdapter {

    private final Logger LOGGER = LogManager.getLogger(RequestInterceptor.class);

    @Value("${introspectionEndpoint}")
    private String introspectionEndpoint;

    private IntrospectionHandler introspectionHandler;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if(request.getRequestURI().contains("/assets/inspections/assetName") ||
                request.getRequestURI().contains("/assets/inspections/assetCategory") ||
                request.getRequestURI().contains("/assets/user") ||
                request.getRequestURI().contains("/assets/user/detail/wallet") ||
                request.getRequestURI().contains("/assets/asset-groups/name")||
                request.getRequestURI().contains("/assets/asset/asset-group/uuid") ||
                request.getRequestURI().contains("/assets/get/categories") ||
                request.getRequestURI().contains("/assets/get")
                )
            return true;

        String bearerToken = request.getHeader("Authorization");

        try{

            if(this.introspectionHandler == null)
                this.introspectionHandler = new IntrospectionHandler(introspectionEndpoint,true);

            // Validate for authorization
            if (!this.introspectionHandler.isAuthorized(bearerToken)) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Sorry.Invalid/Revoked/Expired Access Token.");
                return false;
            }
            return true;
        }catch(Exception e){
            LOGGER.error("An Error occurred while Validating Token "+bearerToken,e);
            e = null;
            return false;
        }finally{
            bearerToken = null;
        }
    }


}
