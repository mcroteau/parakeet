package io.github.mcroteau;

import io.github.mcroteau.resources.access.impl.MockAccessor;
import io.github.mcroteau.resources.filters.CacheFilter;
import io.github.mcroteau.utils.TestConstants;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class SessionUserTest {

    private static Logger log = Logger.getLogger(SessionUserTest.class);

    public SessionUserTest(){
        BasicConfigurator.configure();
    }

    @Test
    public void testGetUser() throws InterruptedException{

        CacheFilter filter = new CacheFilter();
        MockAccessor mockAccessor =  new MockAccessor();
        Parakeet parakeet = new Parakeet(mockAccessor);

        MockUtil mockUtil = new MockUtil();

        for(int n = 0; n < TestConstants.MOCK_REQUESTS; n++) {

            Thread thread = new Thread(){
                public void run() {
                    try {
                        MockHttpServletResponse mockResp = new MockHttpServletResponse();
                        MockHttpServletRequest mockReq = new MockHttpServletRequest();
                        FilterChain mockFilterChain = Mockito.mock(FilterChain.class);
                        FilterConfig mockFilterConfig = Mockito.mock(FilterConfig.class);

                        filter.init(mockFilterConfig);
                        filter.doFilter(mockReq, mockResp, mockFilterChain);

                        parakeet.login(mockUtil.getUser(), TestConstants.PASS);

                        filter.doFilter(mockReq, mockResp, mockFilterChain);

                        if(!parakeet.getUser().equals(mockUtil.getUser())){
                            mockUtil.incrementCount();
                            log.info(parakeet.getUser() + ":" + mockUtil.getUser());
                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };
            thread.start();
            thread.join();
            mockUtil.incrementIndex();
        }

        assertTrue(mockUtil.getCount() == 0);
    }

    public class MockUtil{
        int index = 0;
        int count = 0;

        public void incrementCount(){
            this.count++;
        }

        public void incrementIndex(){
            this.index++;
        }

        public int getCount(){
            return this.count;
        }

        public String getUser(){
            return "user" + this.index;
        }
    }
}
