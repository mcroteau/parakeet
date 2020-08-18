package org.parakeet;

import org.parakeet.resources.Resource;
import org.parakeet.resources.access.Accessor;

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
        HttpServletRequest req = Resource.getRequest();
        HttpSession session = req.getSession();

        if(session != null){
            return (String) session.getAttribute("user");
        }
        return "";
    }


    public boolean login(String username, String password){
        String hashedPassword = Resource.hash(password);
        String storedPassword = accessor.getPassword(username);

        if(!isAuthenticated() &&
                storedPassword.equals(hashedPassword)){

            HttpServletRequest req = Resource.getRequest();
            HttpServletResponse resp = Resource.getResponse();

            scavengeForCookies(req, resp);

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


    private void scavengeForCookies(HttpServletRequest req, HttpServletResponse resp) {
        if(containsCookie(req)){
            System.out.println("\n\nFOUND ONE!!!!!!\n\n");
            expireCookie(req, resp);
        }
    }


    public boolean logout(){
        HttpServletRequest req = Resource.getRequest();
        HttpServletResponse resp = Resource.getResponse();
        HttpSession session = req.getSession();

        if(session != null){
            if(sessions.containsKey(session.getId())){
                sessions.remove(session.getId());
            }
        }

        expireCookie(req, resp);

        return true;
    }

    public boolean isAuthenticated(){
        HttpServletRequest req = Resource.getRequest();
        if(req != null) {
            HttpSession session = req.getSession(false);

            if (session != null && containsCookie(req) && sessions.containsKey(session.getId())) {
                return true;
            }
        }
        return false;
    }


    private void expireCookie(HttpServletRequest req, HttpServletResponse resp){
        Cookie cookie = new Cookie(Resource.COOKIE, "");
        cookie.setMaxAge(0);
        resp.addCookie(cookie);
    }



    public boolean containsCookie(HttpServletRequest req){
        Cookie[] cookies = req.getCookies();
        for(Cookie cookie : cookies){
            if(cookie.getName().equals(Resource.COOKIE)){
                return true;
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
