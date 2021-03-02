package xyz.goioc.resources.filters;

import xyz.goioc.resources.Cache;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ParakeetFilter implements Filter {

    @Override
    public void init(FilterConfig config) throws ServletException { }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        Cache.storeRequest(req);
        Cache.storeResponse(resp);

        if(request != null && response != null) {
            chain.doFilter(request, response);
        }
    }

    @Override
    public void destroy() { }

}
