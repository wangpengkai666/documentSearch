package com.example.documentseach.common.util.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

/**
 * @author wangpengkai
 */
public class Slf4jFacade implements KLog{
    private static Marker MARKER = MarkerFactory.getMarker("ILog");
    private Logger logger;
    private Logger errorLogger;

    public Slf4jFacade(String clazz) {
        this.logger = org.slf4j.LoggerFactory.getLogger(clazz);
        this.errorLogger = LoggerFactory.getLogger("errorLogger");
    }

    public void trace(String format, Object arg) {
        this.logger.trace(FlagWrapper.wrapMessage(format), arg);
    }

    public void trace(String format, Object arg1, Object arg2) {
        this.logger.trace(FlagWrapper.wrapMessage(format), arg1, arg2);
    }

    public void trace(String format, Object... arguments) {
        this.logger.trace(FlagWrapper.wrapMessage(format), arguments);
    }

    public void debug(String format, Object arg) {
        this.logger.debug(FlagWrapper.wrapMessage(format), arg);
    }

    public void debug(String format, Object arg1, Object arg2) {
        this.logger.debug(FlagWrapper.wrapMessage(format), arg1, arg2);
    }

    public void debug(String format, Object... arguments) {
        this.logger.debug(FlagWrapper.wrapMessage(format), arguments);
    }

    public void info(String format, Object arg) {
        this.logger.info(FlagWrapper.wrapMessage(format), arg);
    }

    public void info(String format, Object arg1, Object arg2) {
        this.logger.info(FlagWrapper.wrapMessage(format), arg1, arg2);
    }

    public void info(String format, Object... arguments) {
        this.logger.info(FlagWrapper.wrapMessage(format), arguments);
    }

    public void warn(String format, Object arg) {
        this.logger.warn(FlagWrapper.wrapMessage(format), arg);
    }

    public void warn(String format, Object... arguments) {
        this.logger.warn(FlagWrapper.wrapMessage(format), arguments);
    }

    public void warn(String format, Object arg1, Object arg2) {
        this.logger.warn(FlagWrapper.wrapMessage(format), arg1, arg2);
    }

    public void error(String format, Object arg) {
        String wrapperMessage = FlagWrapper.wrapMessage(format);
        this.errorLogger.error(wrapperMessage, arg);
        this.logger.error(wrapperMessage, arg);
    }

    public void error(String format, Object arg1, Object arg2) {
        String wrapperMessage = FlagWrapper.wrapMessage(format);
        this.errorLogger.error(wrapperMessage, arg1, arg2);
        this.logger.error(wrapperMessage, arg1, arg2);
    }

    public void error(String format, Object... arguments) {
        String wrapperMessage = FlagWrapper.wrapMessage(format);
        this.errorLogger.error(wrapperMessage, arguments);
        this.logger.error(wrapperMessage, arguments);
    }

    public boolean isTraceEnabled() {
        return this.logger.isTraceEnabled();
    }

    public void trace(String message) {
        this.logger.trace(MARKER, FlagWrapper.wrapMessage(message));
    }

    public void trace(String message, Throwable t) {
        this.logger.trace(MARKER, FlagWrapper.wrapMessage(message), t);
    }

    public boolean isDebugEnabled() {
        return this.logger.isDebugEnabled();
    }

    public boolean isInfoEnabled() {
        return this.logger.isInfoEnabled();
    }

    public void error(String message, Throwable e) {
        String msg = FlagWrapper.wrapExceptionMessage(message);
        this.errorLogger.error(MARKER, msg, e);
        this.logger.error(MARKER, msg, e);
    }

    public void error(String message) {
        String msg = FlagWrapper.wrapExceptionMessage(message);
        this.errorLogger.error(MARKER, msg);
        this.logger.error(MARKER, msg);
    }

    public void debug(String message) {
        this.logger.debug(MARKER, FlagWrapper.wrapMessage(message));
    }

    public void info(String message) {
        this.logger.info(MARKER, FlagWrapper.wrapMessage(message));
    }

    public void warn(String message) {
        this.logger.warn(MARKER, FlagWrapper.wrapMessage(message));
    }

    public void warn(String message, Throwable e) {
        this.logger.warn(MARKER, FlagWrapper.wrapMessage(message), e);
    }

    public boolean isErrorEnabled() {
        return this.logger.isErrorEnabled();
    }

    public void debug(String message, Throwable e) {
        this.logger.debug(MARKER, FlagWrapper.wrapMessage(message), e);
    }

    public void info(String message, Throwable e) {
        this.logger.info(MARKER, FlagWrapper.wrapMessage(message), e);
    }

    public boolean isWarnEnabled() {
        return this.logger.isWarnEnabled();
    }
}
