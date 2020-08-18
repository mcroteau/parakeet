package org.parakeet;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;


import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.parakeet.resources.access.Accessor;
import org.parakeet.resources.access.impl.MockAccessor;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import org.parakeet.resources.filters.CacheFilter;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CacheFilterTest {

    private static Logger log = Logger.getLogger(CacheFilterTest.class);

    private int loggedIn = 0;

    private static int REQUESTS = 190;

    private Accessor mockAccessor;

    CacheFilterTest(){
        mockAccessor = new MockAccessor();
        BasicConfigurator.configure();
    }

    @Test
    public void testDoFilter() throws ServletException, IOException, InterruptedException {

        CacheFilter filter = new CacheFilter();
        AuthdIncrementor incrementor = new AuthdIncrementor();
        for(int n = 0; n < REQUESTS; n++) {
            Thread thread = new Thread("tweet " + n){
                @Override
                public void run(){

                    try {
                        Parakeet parakeet = new Parakeet(mockAccessor);

                        MockHttpServletResponse mockResp = new MockHttpServletResponse();
                        MockHttpServletRequest mockReq = new MockHttpServletRequest( );
                        FilterChain mockFilterChain = Mockito.mock(FilterChain.class);
                        FilterConfig mockFilterConfig = Mockito.mock(FilterConfig.class);

                        filter.init(mockFilterConfig);
                        filter.doFilter(mockReq, mockResp, mockFilterChain);

                        parakeet.login("mockyah", "birdyah");

                        if(parakeet.isAuthenticated()){
                            log.info("is logged in");
                            incrementor.increment();
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

//        Thread.sleep(3000);

        assertTrue(incrementor.getCount() == REQUESTS);
    }

    public class AuthdIncrementor {
        private int count;

        public void increment(){
            count++;
        }
        public int getCount(){
            return this.count;
        }
    }
}
