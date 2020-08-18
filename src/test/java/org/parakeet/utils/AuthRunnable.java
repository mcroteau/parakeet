package org.parakeet.utils;

import org.apache.log4j.Logger;
import org.mockito.Mockito;
import org.parakeet.CacheFilterTest;
import org.parakeet.Parakeet;
import org.parakeet.resources.access.Accessor;
import org.parakeet.resources.access.impl.MockAccessor;
import org.parakeet.resources.filters.CacheFilter;
import org.parakeet.utils.AuthdIncrementor;
import org.parakeet.utils.TestConstants;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import java.io.IOException;

public class AuthRunnable implements Runnable {

    private static Logger log = Logger.getLogger(AuthRunnable.class);

    CacheFilter filter;
    Accessor mockAccessor;
    AuthdIncrementor incrementer;

    public AuthRunnable(AuthdIncrementor incrementer){
        this.filter = new CacheFilter();
        this.mockAccessor = new MockAccessor();
        this.incrementer = incrementer;
    }

    @Override
    public void run() {
        try {
            Parakeet parakeet = new Parakeet(mockAccessor);

            MockHttpServletResponse mockResp = new MockHttpServletResponse();
            MockHttpServletRequest mockReq = new MockHttpServletRequest( );
            FilterChain mockFilterChain = Mockito.mock(FilterChain.class);
            FilterConfig mockFilterConfig = Mockito.mock(FilterConfig.class);

            filter.init(mockFilterConfig);
            filter.doFilter(mockReq, mockResp, mockFilterChain);

            parakeet.login(TestConstants.USER, TestConstants.PASS);

            if(parakeet.isAuthenticated()){
                incrementer.increment();
            }

            filter.destroy();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ServletException e) {
            e.printStackTrace();
        }
    }
}
