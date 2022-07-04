package com.example.documentseach.task;

import com.example.documentseach.common.Result;
import com.example.documentseach.common.util.log.KLog;
import com.example.documentseach.common.util.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.PostConstruct;
import java.io.File;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

/**
 * 日志文件定时任务清理，主要是用于单机的一些操作
 *
 * @author wangpengkai
 */
@Configuration
@EnableScheduling
public class LogClearTask {
    private static final KLog LOGGER = LoggerFactory.getLog(LogClearTask.class);

    private static final Integer EXPIRE_INTERVAL = 2;

    @Value("logs")
    private String baseLogPath;

    /**
     * 在服务启动的时候进行一次数据的清理，之后会调用定时任务在凌晨避开高峰阶段进行处理
     */
    @PostConstruct
    private void init() {
        deleteLogFileBefore2DaysFromRoot();
    }

    /**
     * 对于日志文件进行定期的清理，暂时写死是2天，每天的凌晨2点触发
     */
    @Scheduled(cron = "0 0 2 ? * *")
    private void deleteLogFileBefore2DaysFromRoot() {
        LOGGER.info("class=LogClearTask||method=deleteLogFileBefore2DaysFromRoot||errMsg={}", "start to clean the log file");
        File file = new File(baseLogPath);
        // 这里只考虑双层的目录
        for (File subFile : Objects.requireNonNull(file.listFiles())) {
            deleteLogFileBefore2DaysByName(subFile.listFiles());
        }
    }

    /**
     * 这里对于日志文件的批量处理暂时不会采用线程池，因为删除的操作避开了服务使用的热点阶段
     *
     * @param files
     */
    private void deleteLogFileBefore2DaysByName(File[] files) {
        if (files == null) return;
        for (File file : files) {
            if (validLogFile(file).success() && is2DaysBeforeLog(file)) {
                boolean delete = file.delete();
                LOGGER.info("class=LogClearTask||method=deleteLogFileBefore2DaysByName||msg={}", "delete file:" + delete);
            }
        }
    }

    /**
     * 校验日志文件的格式是否正确
     *
     * @param file
     * @return
     */
    private Result<String> validLogFile(File file) {
        return file.isDirectory() || !file.getName().endsWith("log") ? Result.buildFail(file.getName() + "不是目录文件") : Result.buildSucc();
    }

    /**
     * 判断当前日志文件生成的时间是否是在两天以前，方便定期的删除日志文件，避免文件堆积
     *
     * @param file
     * @return
     */
    private boolean is2DaysBeforeLog(File file) {
        String name = file.getName();
        // todo 采用正则表达式
        String[] split = name.split("\\.")[0].split("-");
        LocalDate logDate = LocalDate.of(Integer.parseInt(split[2]), Integer.parseInt(split[3]), Integer.parseInt(split[4]));
        LocalDate now = LocalDate.now();
        long intervalDays = ChronoUnit.DAYS.between(logDate, now);
        return intervalDays >= EXPIRE_INTERVAL;
    }
}
