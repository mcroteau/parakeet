package io.github.mcroteau;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;

import org.junit.jupiter.api.Test;
import org.apache.log4j.Logger;
import org.apache.log4j.BasicConfigurator;
import org.mockito.Mockito;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import io.github.mcroteau.resources.access.impl.MockAccessor;
import io.github.mcroteau.resources.filters.CacheFilter;

import io.github.mcroteau.utils.AuthdIncrementor;
import io.github.mcroteau.utils.TestConstants;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class CacheFilterTest {

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

                        MockHttpServletResponse mockResp = new MockHttpServletResponse();
                        MockHttpServletRequest mockReq = new MockHttpServletRequest();
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
            };
            thread.start();
            thread.join();
        }

        log.info(incrementer.getCount() + " .");
        assertTrue(incrementer.getCount() == TestConstants.MOCK_REQUESTS);
    }

}
