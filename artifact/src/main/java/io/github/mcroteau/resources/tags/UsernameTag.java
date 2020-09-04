package io.github.mcroteau.resources.tags;

import io.github.mcroteau.Parakeet;
import io.github.mcroteau.resources.Constants;
import io.github.mcroteau.resources.access.Accessor;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

public class UsernameTag extends TagSupport {

    @Override
    public int doStartTag() throws JspException {

        try {

            JspWriter out = pageContext.getOut();

            HttpServletRequest req = (HttpServletRequest) pageContext.getRequest();
            HttpSession session = req.getSession(false);

            if(session != null) {
                ServletContext context = req.getServletContext();
                Parakeet parakeet = (Parakeet) context.getAttribute(Constants.PARAKEET_LOOKUP);

                System.out.println("username parakeet : " + parakeet);

                if(parakeet == null) return TagSupport.SKIP_BODY;

                if(parakeet.isAuthenticated()){
                    out.println(parakeet.getUser());
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return TagSupport.SKIP_BODY;
    }
}
