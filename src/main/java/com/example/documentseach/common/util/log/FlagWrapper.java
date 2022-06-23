package com.example.documentseach.common.util.log;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author wangpengkai
 */
public class FlagWrapper {
    public FlagWrapper() {
    }

    public static String wrapMessage(String message) {
        if (null == message) {
            return "";
        } else {
            String flag = LoggerFactory.getFlag();
            StringBuilder stringBuilder = new StringBuilder();
            if (flag != null && !flag.isEmpty() && !"null".equals(flag)) {
                stringBuilder.append("||").append("flag").append("=").append(flag);
            }

            TraceContext traceContext = LoggerFactory.getTrace();
            if (traceContext != null) {
                if (traceContext.getTraceId() != null) {
                    stringBuilder.append("||").append("traceid").append("=").append(traceContext.getTraceId());
                }

                if (traceContext.getSpanId() != null) {
                    stringBuilder.append("||").append("spanid").append("=").append(traceContext.getSpanId());
                }

                if (traceContext.getCspanId() != null) {
                    stringBuilder.append("||").append("cspanid").append("=").append(traceContext.getCspanId());
                }
            }

            return stringBuilder.length() == 0 ? message : message + stringBuilder.toString();
        }
    }

    public static String wrapExceptionMessage(String message) {
        try {
            return wrapMessage(message) + "||hostName=" + InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException var) {
            var.printStackTrace();
            return null;
        }
    }
}
