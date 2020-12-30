package xyz.strongperched.web;

import xyz.strongperched.Parakeet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class SignoutServlet extends HttpServlet {

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }

    public void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException {
        Parakeet.logout();
        req.setAttribute("message", "Successfully signed out!");
        req.getRequestDispatcher("/jsp/index.jsp").forward(req, resp);
    }
}
