package io.github.mcroteau.web;

import io.github.mcroteau.ParakeetFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AuthServlet extends HttpServlet {

    ParakeetFactory parakeetFactory;

    public AuthServlet(){
        ParakeetFactory parakeetFactory = (ParakeetFactory) getServletContext().getAttribute("parakeetFactory");
        if(parakeetFactory == null) {
            parakeetFactory = new ParakeetFactory();
            getServletContext().setAttribute("parakeetFactory", parakeetFactory);
        }
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");
    }
}
