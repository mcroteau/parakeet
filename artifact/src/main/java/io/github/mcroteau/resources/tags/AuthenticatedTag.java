package io.github.mcroteau.resources.tags;

import io.github.mcroteau.Parakeet;
import io.github.mcroteau.resources.Constants;
import io.github.mcroteau.resources.access.Accessor;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;

public class AuthenticatedTag extends TagSupport {

    @Override
    public int doStartTag() throws JspException {

        try {
            JspWriter out = pageContext.getOut();

            HttpServletRequest req = (HttpServletRequest) pageContext.getRequest();
            HttpSession session = req.getSession(false);

            if(session != null) {
                Accessor accessor = (Accessor) req.getAttribute(Constants.ACCESSOR_LOOKUP);
                Parakeet parakeet = new Parakeet(accessor);

                if(parakeet.isAuthenticated()){
                    out.println("true");
                }else{
                    out.println("false");
                }

            }else{
                out.println("false");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return SKIP_BODY;
    }
}
