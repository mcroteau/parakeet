package io.github.mcroteau.web;

import io.github.mcroteau.ParakeetFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;


public class SigninServlet extends HttpServlet {

    ParakeetFactory parakeetFactory;

    public SigninServlet(){
        ParakeetFactory parakeetFactory = (ParakeetFactory) getServletContext().getAttribute("parakeetFactory");
        if(parakeetFactory == null) {
            parakeetFactory = new ParakeetFactory();
            getServletContext().setAttribute("parakeetFactory", parakeetFactory);
        }
    }


    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");
    }


}
