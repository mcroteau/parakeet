package io.github.mcroteau.resources.tags;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;

public class AuthenticatedTag extends TagSupport {

    @Override
    public int doStartTag() throws JspException {

        try {

            JspWriter out = pageContext.getOut();
            

        } catch (Exception e) {
            e.printStackTrace();
        }
        return SKIP_BODY;
    }
}
