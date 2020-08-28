package io.github.mcroteau;

import io.github.mcroteau.resources.Cache;
import io.github.mcroteau.resources.Constants;
import io.github.mcroteau.resources.access.Accessor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class Parakeet {

    Accessor accessor;
    Map<String, HttpSession> sessions;

    public Parakeet(Accessor accessor){
        this.accessor = accessor;
        this.sessions = new ConcurrentHashMap<String, HttpSession>();
    }

    public String getUser(){
        HttpServletRequest req = Cache.getRequest();
        HttpSession session = req.getSession();

        if(session != null){
            return (String) session.getAttribute("user");
        }
        return "";
    }


    public boolean login(String username, String password){
        String hashedPassword = Constants.hash(password);
        String storedPassword = accessor.getPassword(username);

        if(!isAuthenticated() &&
                storedPassword.equals(hashedPassword)){

            HttpServletRequest req = Cache.getRequest();

            HttpSession oldSession = req.getSession(false);
            if(oldSession != null){
                oldSession.invalidate();
            }

            HttpSession httpSession = req.getSession(true);
            httpSession.setAttribute("user", username);

            sessions.put(httpSession.getId(), httpSession);

            return true;

        }

        return false;
    }



    public boolean logout(){
        HttpServletRequest req = Cache.getRequest();
        HttpServletResponse resp = Cache.getResponse();
        HttpSession session = req.getSession();

        if(session != null){
            session.removeAttribute("user");
            if(sessions.containsKey(session.getId())){
                sessions.remove(session.getId());
            }
        }

        expireCookie(req, resp);

        return true;
    }

    public boolean isAuthenticated(){
        HttpServletRequest req = Cache.getRequest();
        if(req != null) {
            HttpSession session = req.getSession(false);

            if (session != null && sessions.containsKey(session.getId())) {
                return true;
            }
        }
        return false;
    }


    private void expireCookie(HttpServletRequest req, HttpServletResponse resp){
        Cookie cookie = new Cookie(Constants.COOKIE, "");
        cookie.setMaxAge(0);
        resp.addCookie(cookie);
    }



    public boolean containsCookie(HttpServletRequest req){
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


    public boolean hasRole(String role){
        String user = getUser();
        if(user != null) {
            Set<String> roles = accessor.getRoles(user);
            if(roles.contains(role)){
                return true;
            }
        }
        return false;
    }


    public boolean hasPermission(String permission){
        String user = getUser();
        if(user != null) {
            Set<String> permissions = accessor.getPermissions(user);
            if(permissions.contains(permission)){
                return true;
            }
        }
        return false;
    }

}
