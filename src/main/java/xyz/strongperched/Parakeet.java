package xyz.strongperched;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;
import java.util.Set;

import xyz.strongperched.resources.Cache;
import xyz.strongperched.resources.Constants;
import xyz.strongperched.resources.access.Accessor;

public class Parakeet {

    static Accessor accessor;
    static Map<String, HttpSession> sessions = new ConcurrentHashMap<String, HttpSession>();

    public static String getUser(){
        HttpServletRequest req = Cache.getRequest();
        HttpSession session = req.getSession();

        if(session != null){
            return (String) session.getAttribute(Constants.USER_KEY);
        }
        return "";
    }

    public static boolean login(String username, String password){
        String hashedPassword = hash(password);
        String storedPassword = accessor.getPassword(username);

        if(!isAuthenticated() &&
                storedPassword.equals(hashedPassword)){

            HttpServletRequest req = Cache.getRequest();

            HttpSession oldSession = req.getSession(false);
            if(oldSession != null){
                oldSession.invalidate();
            }

            HttpSession httpSession = req.getSession(true);
            httpSession.setAttribute(Constants.USER_KEY, username);
            sessions.put(httpSession.getId(), httpSession);

            Cookie cookie = new Cookie(Constants.COOKIE, httpSession.getId());
            cookie.setPath("/");

            HttpServletResponse resp = Cache.getResponse();
            resp.addCookie(cookie);
            
            return true;

        }

        return false;
    }

    public static boolean logout(){
        HttpServletRequest req = Cache.getRequest();
        HttpServletResponse resp = Cache.getResponse();
        HttpSession session = req.getSession();

        if(session != null){
            session.removeAttribute(Constants.USER_KEY);
            if(sessions.containsKey(session.getId())){
                sessions.remove(session.getId());
            }
        }

        expireCookie(req, resp);
        return true;
    }

    public static boolean isAuthenticated(){
        HttpServletRequest req = Cache.getRequest();
        if(req != null) {
            HttpSession session = req.getSession(false);
            if (session != null && sessions.containsKey(session.getId())) {
                return true;
            }
        }
        return false;
    }

    private static void expireCookie(HttpServletRequest req, HttpServletResponse resp){
        Cookie cookie = new Cookie(Constants.COOKIE, "");
        cookie.setMaxAge(0);
        resp.addCookie(cookie);
    }

    public static boolean containsCookie(HttpServletRequest req){
        Cookie[] cookies = req.getCookies();
        if(cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(Constants.COOKIE)) {
                    return true;
                }
            }
        }
        return false;
    }


    public static boolean hasRole(String role){
        String user = getUser();
        if(user != null) {
            Set<String> roles = accessor.getRoles(user);
            if(roles.contains(role)){
                return true;
            }
        }
        return false;
    }


    public static boolean hasPermission(String permission){
        String user = getUser();
        if(user != null) {
            Set<String> permissions = accessor.getPermissions(user);
            if(permissions.contains(permission)){
                return true;
            }
        }
        return false;
    }

    /////////////// Thank you stack overflow ///////////////
    public static String hash(String password){
        MessageDigest md = null;
        StringBuffer passwordHashed = new StringBuffer();

        try {
            md = MessageDigest.getInstance(Constants.HASH_256);
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

    public static String dirty(String password){
        MessageDigest md = null;
        StringBuffer passwordHashed = new StringBuffer();

        try {
            md = MessageDigest.getInstance(Constants.HASH_256);
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

    public static boolean perch(Accessor accessor){
        Parakeet.accessor = accessor;
        return true;
    }

}
