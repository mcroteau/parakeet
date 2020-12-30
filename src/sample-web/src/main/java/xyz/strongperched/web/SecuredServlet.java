package xyz.strongperched.web;

import xyz.strongperched.Parakeet;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class SecuredServlet extends HttpServlet {

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        PrintWriter out = resp.getWriter();

        if(Parakeet.isAuthenticated()){
            req.getRequestDispatcher("/jsp/secured.jsp").forward(req, resp);
        }else{
            out.println("<h1>Unauthorized yo!</h1>");
        }
    }

}
