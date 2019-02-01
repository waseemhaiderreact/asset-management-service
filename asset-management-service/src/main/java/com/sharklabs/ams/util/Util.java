package com.sharklabs.ams.util;

//import org.apache.logging.log4j.ThreadContext;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.LinkedHashMap;

/*
*    Description: All util functions, commonly used throughout this service are included in this class. Ideally, this class should be in commons
*    Version History: v1
*
*    Version        Author            Description                Date
*    ===============================================================
*    v1            Imtiaz Hassan    Initial Version            04-Dec-2018
@
*/

public class Util {

    /*
    * We need to use session ID and logged in user name in log4j's default logging pattern. This function setts these values in the ThreadContext.
    * */

    public void setThreadContextForLogging() {
        // TODO: Implement Zuul and use interceptor
        try {
            String sessionId = RequestContextHolder.currentRequestAttributes().getSessionId();
            Authentication principal = SecurityContextHolder.getContext().getAuthentication();
            if (principal != null) {
                String userName = ((LinkedHashMap) principal.getPrincipal()).get("username")+"";
                ThreadContext.put("userId", userName);

            }
            ThreadContext.put("sessionId", sessionId);
        } catch (Exception e) {
            // Unable to set thread context. Since it is a non-blocking error, the normal execution should still continue to work.
            e.printStackTrace();
        }

    }

    /*
    * Clear ThreadContext values set in setThreadContextForLogging function. Below funciton should only be called when setThreadContextForLogging is invoked before it.
    * */
    public void clearThreadContextForLogging() {
        ThreadContext.clearMap();

    }
}
