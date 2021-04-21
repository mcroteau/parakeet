package xyz.goioc.resources.tags;

import xyz.goioc.Parakeet;

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
                ServletContext context = session.getServletContext();

                if(Parakeet.isAuthenticated()){
                    out.println(Parakeet.getUser());
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return TagSupport.SKIP_BODY;
    }
}
