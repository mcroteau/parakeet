package org.parakeet;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletException;

import org.junit.jupiter.api.Test;
import org.apache.log4j.Logger;
import org.apache.log4j.BasicConfigurator;
import org.parakeet.resources.access.Accessor;
import org.parakeet.resources.access.impl.MockAccessor;
import org.parakeet.utils.AuthRunnable;
import org.parakeet.utils.AuthdIncrementor;

import org.parakeet.resources.filters.CacheFilter;
import org.parakeet.utils.TestConstants;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class CacheFilterTest {

    private static Logger log = Logger.getLogger(CacheFilterTest.class);

    CacheFilterTest(){
        BasicConfigurator.configure();
    }

    @Test
    public void stressTestDoFilter() throws ServletException, IOException, InterruptedException {

        AuthdIncrementor incrementer = new AuthdIncrementor();
        ExecutorService executorService = Executors.newCachedThreadPool();

        for(int n = 0; n < TestConstants.MOCK_REQUESTS; n++) {
            executorService.execute(new AuthRunnable(incrementer));
        }

        executorService.shutdown();
        executorService.awaitTermination(1, TimeUnit.MINUTES);

        assertTrue(incrementer.getCount() == TestConstants.MOCK_REQUESTS);
    }

}
