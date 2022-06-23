package com.example.documentseach.common.util.log;

import org.slf4j.Logger;

import java.lang.reflect.Constructor;

/**
 * @author wangpengkai
 */
public class LoggerFactory {
    private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(LoggerFactory.class);
    private static Constructor<? extends KLog> logConstructor;
    private static ThreadLocal<String> flag = new ThreadLocal();
    private static ThreadLocal<TraceContext> trace = new ThreadLocal();

    private static synchronized void useSlf4jLogging() throws Exception {
        Class clazz = Slf4jFacade.class;

        try {
            logConstructor = clazz.getConstructor(String.class);
            LOGGER.info("Logging initialized using '" + clazz + "' adapter.");
        } catch (Throwable var2) {
            LOGGER.error("Error setting Log implementation.  Cause: " + var2, var2);
            throw new Exception("Error setting Log implementation.  Cause: " + var2, var2);
        }
    }

    private LoggerFactory() {
    }

    public static KLog getLog(Class<?> aClass) {
        return getLog(aClass.getName());
    }

    public static KLog getLog(String logger) {
        try {
            return (KLog)logConstructor.newInstance(logger);
        } catch (Exception var2) {
            LOGGER.error("Error creating logger for logger " + logger + ".  Cause: " + var2, var2);
            throw new RuntimeException();
        }
    }

    public static void setFlag(String value) {
        flag.set(value);
    }

    public static void setTrace(TraceContext traceContext) {
        trace.set(traceContext);
    }

    public static void setUniqueFlag() {
        flag.set(getUniqueFlag());
    }

    public static String getFlag() {
        return (String)flag.get();
    }

    public static TraceContext getTrace() {
        return (TraceContext)trace.get();
    }

    public static void removeFlag() {
        flag.remove();
    }

    public static String getUniqueFlag() {
        return FlagGenerator.get().toStringBabble();
    }

    static {
        try {
            useSlf4jLogging();
        } catch (Throwable var1) {
            LOGGER.error("Error creating logger for logger " + ".  Cause: " + var1);
        }
    }
}
