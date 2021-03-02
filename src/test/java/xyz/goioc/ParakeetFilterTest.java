package xyz.goioc;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;

import org.easymock.EasyMockSupport;
import org.junit.jupiter.api.Test;
import org.apache.log4j.Logger;
import org.apache.log4j.BasicConfigurator;
import org.mockito.Mockito;
import org.springframework.mock.web.MockHttpServletRequest;

import xyz.goioc.resources.access.impl.MockAccessor;
import xyz.goioc.resources.filters.ParakeetFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import xyz.goioc.utils.AuthdIncrementor;
import xyz.goioc.utils.TestConstants;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ParakeetFilterTest extends EasyMockSupport {

    private static Logger log = Logger.getLogger(ParakeetFilterTest.class);

    ParakeetFilterTest(){
        BasicConfigurator.configure();
    }

    @Test
    public void stressTestFilter() throws ServletException, IOException, InterruptedException {

        AuthdIncrementor incrementer = new AuthdIncrementor();
        ParakeetFilter filter = new ParakeetFilter();
        MockAccessor mockAccessor = new MockAccessor();

        for(int n = 0; n < TestConstants.MOCK_REQUESTS; n++) {
            Thread thread = new Thread(){
                @Override
                public void run(){
                    try {
                        Parakeet.perch(mockAccessor);

                        HttpServletRequest req = new MockHttpServletRequest();
                        HttpServletResponse resp = Mockito.mock(HttpServletResponse.class);
                        FilterConfig config = Mockito.mock(FilterConfig.class);
                        FilterChain filterChain = Mockito.mock(FilterChain.class);

                        filter.init(config);
                        filter.doFilter(req, resp, filterChain);

                        Parakeet.login(TestConstants.USER, TestConstants.PASS);

                        if(Parakeet.isAuthenticated()){
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
