package org.parakeet.resources;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class Cache {

    private static ThreadLocal<HttpServletRequest> req = new InheritableThreadLocal<HttpServletRequest>();

    private static ThreadLocal<HttpServletResponse> resp = new InheritableThreadLocal<HttpServletResponse>();

    public static void cacheRequest(HttpServletRequest request){
        req.set(request);
    }

    public static void cacheResponse(HttpServletResponse response){
        resp.set(response);
    }

    public static HttpServletRequest getRequest(){
        return req.get();
    }

    public static HttpServletResponse getResponse(){
        return resp.get();
    }




}
