package com.sharklabs.ams.security;



import com.sharklabs.ams.feign.AuthServiceProxy;
import com.sharklabs.ams.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Map;

//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;

@Component("PrivilegeHandler")
public class PrivilegeHandler {
    
    @Autowired
    private AuthServiceProxy authServiceProxy;

    public boolean hasCreate(){
//        Authentication principal = SecurityContextHolder.getContext().getAuthentication();
        Map<String,Object> principal = authServiceProxy.getUserDetails(((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getHeader("Authorization"));
        if(principal==null){
            return false;
        }
        String[] authoritiesToCheck = new String[]{
                "ASSETS_CREATE_WEB",
                "ASSETS_CREATE_MOBILE"
        };
        return Util.containsAnyWord(principal.get("authorities").toString(), authoritiesToCheck);
    }

    public boolean hasDelete(){
//        Authentication principal = SecurityContextHolder.getContext().getAuthentication();
        Map<String,Object> principal = authServiceProxy.getUserDetails(((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getHeader("Authorization"));
        if(principal==null){
            return false;
        }
        String[] authoritiesToCheck = new String[]{
                "ASSETS_DELETE_WEB",
                "ASSETS_DELETE_MOBILE"
        };
        return Util.containsAnyWord(principal.get("authorities").toString(), authoritiesToCheck);
    }

    public boolean hasRead(){
//        Authentication principal = SecurityContextHolder.getContext().getAuthentication();
        Map<String,Object> principal = authServiceProxy.getUserDetails(((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getHeader("Authorization"));
        if(principal==null){
            return false;
        }
        String[] authoritiesToCheck = new String[]{
                "ASSETS_READ_WEB",
                "ASSETS_READ_MOBILE"
        };
        return Util.containsAnyWord(principal.get("authorities").toString(), authoritiesToCheck);
    }

    public boolean hasUpdate(){
//        Authentication principal = SecurityContextHolder.getContext().getAuthentication();
        Map<String,Object> principal = authServiceProxy.getUserDetails(((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getHeader("Authorization"));
        if(principal==null){
            return false;
        }
        String[] authoritiesToCheck = new String[]{
                "ASSETS_UPDATE_WEB",
                "ASSETS_UPDATE_MOBILE"
        };
        return Util.containsAnyWord(principal.get("authorities").toString(), authoritiesToCheck);
    }

    public boolean hasCategory(){
//        Authentication principal = SecurityContextHolder.getContext().getAuthentication();
        Map<String,Object> principal = authServiceProxy.getUserDetails(((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getHeader("Authorization"));
        if(principal==null){
            return false;
        }
        String[] authoritiesToCheck = new String[]{
                "ASSETS_CATEGORY_WEB",
                "ASSETS_CATEGORY_MOBILE"
        };
        return Util.containsAnyWord(principal.get("authorities").toString(), authoritiesToCheck);
    }

}
