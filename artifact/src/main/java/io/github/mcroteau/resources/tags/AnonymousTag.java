package io.github.mcroteau.resources.tags;

import io.github.mcroteau.Parakeet;
import io.github.mcroteau.resources.Constants;
import io.github.mcroteau.resources.access.Accessor;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;

public class AnonymousTag extends TagSupport {

    @Override
    public int doStartTag() throws JspException {

        try {

            JspWriter out = pageContext.getOut();

            HttpServletRequest req = (HttpServletRequest) pageContext.getRequest();
            HttpSession session = req.getSession(false);

            if(session != null) {
                ServletContext context = req.getServletContext();
                Parakeet parakeet = (Parakeet) context.getAttribute(Constants.PARAKEET_LOOKUP);

                if(parakeet == null) return TagSupport.EVAL_BODY_INCLUDE;

                if(parakeet.isAuthenticated()){
                    return TagSupport.SKIP_BODY;
                }else{
                    return TagSupport.EVAL_BODY_INCLUDE;
                }

            }else{
                return TagSupport.EVAL_BODY_INCLUDE;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return TagSupport.EVAL_BODY_INCLUDE;
    }
}
