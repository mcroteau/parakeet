package io.github.mcroteau;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;

import org.easymock.EasyMock;
import org.easymock.EasyMockSupport;
import org.easymock.IMocksControl;
import org.junit.jupiter.api.Test;
import org.apache.log4j.Logger;
import org.apache.log4j.BasicConfigurator;
import org.mockito.Mockito;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import io.github.mcroteau.resources.access.impl.MockAccessor;
import io.github.mcroteau.resources.filters.CacheFilter;

import javax.servlet.*;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import io.github.mcroteau.utils.AuthdIncrementor;
import io.github.mcroteau.utils.TestConstants;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class CacheFilterTest extends EasyMockSupport {

    private static Logger log = Logger.getLogger(CacheFilterTest.class);

    CacheFilterTest(){
        BasicConfigurator.configure();
    }

    @Test
    public void stressTestFilter() throws ServletException, IOException, InterruptedException {

        AuthdIncrementor incrementer = new AuthdIncrementor();
        CacheFilter filter = new CacheFilter();
        MockAccessor mockAccessor = new MockAccessor();

        for(int n = 0; n < TestConstants.MOCK_REQUESTS; n++) {
            Thread thread = new Thread(){
                @Override
                public void run(){
                    try {
                        Parakeet parakeet = new Parakeet(mockAccessor);

                        HttpServletRequest req = new MockHttpServletRequest();
                        HttpServletResponse resp = Mockito.mock(HttpServletResponse.class);
                        FilterConfig config = Mockito.mock(FilterConfig.class);
                        FilterChain filterChain = Mockito.mock(FilterChain.class);

                        filter.init(config);
                        filter.doFilter(req, resp, filterChain);

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
            };
            thread.start();
            thread.join();
        }

        log.info(incrementer.getCount() + " .");
        assertTrue(incrementer.getCount() == TestConstants.MOCK_REQUESTS);
    }

}
