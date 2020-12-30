package xyz.strongperched.web;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import xyz.strongperched.Parakeet;
import xyz.strongperched.resources.access.impl.MockAccessor;

public class AuthServlet extends HttpServlet {

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        Parakeet.perch(new MockAccessor());
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");

        if(Parakeet.login(username, password)){
            req.getRequestDispatcher("/jsp/secured.jsp").forward(req, resp);
        }else{
            resp.sendRedirect("/b/");
        }

    }
}
