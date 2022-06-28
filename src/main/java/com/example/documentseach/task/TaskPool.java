package com.example.documentseach.task;

import com.example.documentseach.common.util.log.KLog;
import com.example.documentseach.common.util.log.LoggerFactory;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;

import java.util.List;
import java.util.concurrent.*;

/**
 * @author wangpengkai
 */
public class TaskPool {

    private static final String THREAD_FACTORY_NAME = "TASK-POOL";

    private static final KLog LOGGER = LoggerFactory.getLog(TaskPool.class);

    private int poolSize;

    private final int waitTerminate = 20;

    private ExecutorService pool;

    private final ThreadFactory springThreadFactory = new CustomizableThreadFactory("TaskThreadPool");

    public void init(Integer poolSize, String taskName) {
        this.poolSize = poolSize;
        pool = new ThreadPoolExecutor(poolSize, poolSize,
                0, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(),
                springThreadFactory);
        LOGGER.warn("class=TaskPool||method=init||poolSize={}||taskName={}", poolSize, taskName);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            // 尝试进行和平关闭所有的任务
            pool.shutdown();
            try {
                if (!pool.awaitTermination(waitTerminate, TimeUnit.SECONDS)) {
                    LOGGER.warn("class=TaskPool||method=init||msg=still some task running, force to shutdown!");
                    // 立刻停止当前的任务，然后记录当前没有执行的任务
                    List<Runnable> shutTasks = pool.shutdownNow();
                    shutTasks.forEach(r -> LOGGER.warn("class=TaskPool||method=init||runnable={}||taskName={}", r, taskName));
                }
            } catch (InterruptedException e) {
                LOGGER.error("class=TaskPool||method=init||errMsg={}", e.getMessage());
                pool.shutdownNow();
                Thread.currentThread().interrupt();
            }

            LOGGER.info("class=TaskPool||method=init||msg={} shutdown finished", THREAD_FACTORY_NAME);
        }));

        LOGGER.info("class=TaskPool||method=init||TaskPool init finished.");
    }

    public void run(Runnable runnable) {
        pool.execute(runnable);
    }

    public <T> T submit(Callable<T> callable) throws ExecutionException, InterruptedException {
        Future<T> future = pool.submit(callable);
        return future.get();
    }

    public int getPoolSize() {
        return poolSize;
    }
}
