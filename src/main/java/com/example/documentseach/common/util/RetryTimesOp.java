package com.example.documentseach.common.util;

import org.elasticsearch.ElasticsearchTimeoutException;
import org.elasticsearch.cluster.metadata.ProcessClusterEventTimeoutException;

import java.util.function.Function;

/**
 * @author wangpengkai
 */
public class RetryTimesOp {
    private static final int SEC_30 = 30 * 1000;
    private static final int MIN_5  = 5 * 60 * 1000;

    private RetryTimesOp(){}

    public static boolean esRetryExecute(String methodName, int tryCount,
                                         RetryExecutor.Handler handler) throws Exception {
        try {
            return RetryExecutor.builder().name(methodName).retryCount(tryCount).handler(new RetryExecutor.Handler() {
                @Override
                public boolean process() throws Exception {
                    return handler.process();
                }

                @Override
                public boolean needRetry(Exception e) {
                    return e instanceof ProcessClusterEventTimeoutException
                            || e instanceof ElasticsearchTimeoutException;
                }

                @Override
                public int retrySleepTime(int retryTims){
                    int sleepTime       = retryTims * SEC_30;
                    int randomSleepTime = (int)(Math.random() * 100);
                    int totalSleepTime  = sleepTime + randomSleepTime;

                    return totalSleepTime > MIN_5 ? MIN_5 : totalSleepTime;
                }
            }).execute();
        } catch (Exception e) {
            throw new Exception(e.getMessage(), e);
        }
    }

    /**
     * 定制重试方法等待的时间
     * @param methodName 方法名称
     * @param tryCount 重试次数
     * @param handler 重试的操作
     * @param retrySleepTime 重试间隔的等待时间
     * @return 整个重试方法执行的结果
     * @throws Exception 抛异常
     */
    public static boolean esRetryExecuteWithGivenTime(String methodName, int tryCount,
                                                      RetryExecutor.Handler handler, Function<Integer, Integer> retrySleepTime) throws Exception {
        try {
            return RetryExecutor.builder().name(methodName).retryCount(tryCount).handler(new RetryExecutor.Handler() {
                @Override
                public boolean process() throws Exception {
                    return handler.process();
                }

                @Override
                public boolean needRetry(Exception e) {
                    return e instanceof ProcessClusterEventTimeoutException
                            || e instanceof ElasticsearchTimeoutException;
                }

                @Override
                public int retrySleepTime(int retryTimes) {
                    return retrySleepTime.apply(retryTimes);
                }
            }).execute();
        } catch (Exception e) {
            throw new Exception(e.getMessage(), e);
        }
    }
}
