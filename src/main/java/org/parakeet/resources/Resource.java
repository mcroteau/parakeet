package org.parakeet.resources;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class Resource {

    public static final String COOKIE = "JSESSIONID";

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

    public static Date getDate(int days) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, days);
        Date date = cal.getTime();
        return date;
    }

    public static Cookie getCookie(){
        HttpServletRequest req = getRequest();
        if(req != null){
            Cookie[] cookies = req.getCookies();
            if(cookies != null) {
                for (Cookie cookie : cookies) {
                    if (cookie.getName().equals(Resource.COOKIE)) {
                        return cookie;
                    }
                }
            }
        }
        return null;
    }

    public static String hash(String password){
        MessageDigest md = null;
        StringBuffer passwordHashed = new StringBuffer();

        try {
            md = MessageDigest.getInstance("SHA-256");
            md.update(password.getBytes());

            byte byteData[] = md.digest();

            for (int i = 0; i < byteData.length; i++) {
                passwordHashed.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
            }

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return passwordHashed.toString();
    }


}
