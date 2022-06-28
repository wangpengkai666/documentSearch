package com.example.documentseach.task;

import com.example.documentseach.common.util.container.ListUtil;
import com.example.documentseach.common.util.log.KLog;
import com.example.documentseach.common.util.log.LoggerFactory;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 可以执行并发任务的基类
 *
 * @author wangpengkai
 */
public abstract class ConcurrentBaseTask<T> {
    private final KLog LOGGER = LoggerFactory.getLog(ConcurrentBaseTask.class);

    protected TaskPool taskPool;

    public static final int SLEEP_SECONDS_PER_BATCH = 2;

    @PostConstruct
    public void init() {
        taskPool = new TaskPool();
        taskPool.init(poolSize(), getTaskName());
    }

    /**
     * 并发处理任务
     */
    public boolean execute() {
        long start = System.currentTimeMillis();

        List<TaskBatch<T>> batches = splitBatch(getAllItems());

        if (ListUtil.isEmpty(batches)) {
            LOGGER.warn("class=ConcurrentBaseTask||method=execute||batches is empty||task={}", getTaskName());
            return true;
        }

        LOGGER.info("class=ConcurrentBaseTask||method=execute||ConcurrentClusterTask||msg=task start||task={}||batchSize={}", getTaskName(), batches.size());

        CountDownLatch countDownLatch = new CountDownLatch(batches.size());
        AtomicBoolean succ = new AtomicBoolean(true);

        for (TaskBatch<T> taskBatch : batches) {
            taskPool.run(() -> {
                try {
                    if (!executeByBatch(taskBatch)) {
                        succ.set(false);
                    }
                } catch (Exception e) {
                    succ.set(false);
                    LOGGER.error("class=ConcurrentBaseTask||method=execute||errMsg={}||task={}", e.getMessage(),
                            getTaskName(), e);
                } finally {
                    countDownLatch.countDown();
                }
            });

            try {
                Thread.sleep(TimeUnit.SECONDS.toMillis(SLEEP_SECONDS_PER_BATCH));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                LOGGER.warn("class=ConcurrentBaseTask||method=execute||ConcurrentClusterTask Interrupted||task={}", getTaskName(), e);
            }
        }

        try {
            //等待所有任务完成
            if (countDownLatch.await(60L, TimeUnit.MINUTES)) {
                LOGGER.info("class=ConcurrentBaseTask||method=execute||ConcurrentClusterTask||msg=all task finish||task={}", getTaskName());
            } else {
                LOGGER.warn("class=ConcurrentBaseTask||method=execute||ConcurrentClusterTask||msg=has task time out||task={}", getTaskName());
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            LOGGER.warn("class=ConcurrentBaseTask||method=execute||ConcurrentClusterTask Interrupted||task={}", getTaskName(), e);
        }
        try {
            //记录任务的完成任务时间
            long cost = System.currentTimeMillis() - start;
        } catch (Exception e) {
            LOGGER.error("class=ConcurrentBaseTask||method=execute||errMsg={}",
                    e.getMessage(), e);
        }

        return succ.get();
    }

    /**
     * 获取任务批次
     *
     * @return
     */
    private List<TaskBatch<T>> splitBatch(List<T> allItems) {

        if (ListUtil.isEmpty(allItems)) {
            return new LinkedList<>();
        }

        // 打乱顺序
        Collections.shuffle(allItems);

        int workerCount = Math.min(taskPool.getPoolSize(), current());
        if (workerCount < 1) {
            workerCount = 1;
        }

        int batchSize = allItems.size() / workerCount + 1;

        LOGGER.info("class=BaseConcurrentTask||method=splitBatch||ConcurrentClusterTask||msg=splitBatch||task={}||workerCount={}||batchSize={}", getTaskName(),
                workerCount, batchSize);

        List<TaskBatch<T>> taskBatches = new LinkedList<>();
        TaskBatch<T> batch = new TaskBatch<>();
        for (int i = 0; i < allItems.size(); i++) {
            if (batch.getItems().size() >= batchSize) {
                taskBatches.add(batch);
                batch = new TaskBatch<>();
            }
            batch.getItems().add(allItems.get(i));
        }

        if (!batch.getItems().isEmpty()) {
            taskBatches.add(batch);
        }

        return taskBatches;
    }

    /**
     * 任务全集
     *
     * @return
     */
    protected abstract List<T> getAllItems();

    /**
     * 处理一个批次任务
     *
     * @param taskBatch
     */
    protected abstract boolean executeByBatch(TaskBatch<T> taskBatch) throws Exception;

    /**
     * 获取任务名称
     *
     * @return 任务名称
     */
    public abstract String getTaskName();

    /**
     * 任务的线程个数
     *
     * @return
     */
    public abstract int poolSize();

    /**
     * 并发度
     *
     * @return
     */
    public abstract int current();
}
