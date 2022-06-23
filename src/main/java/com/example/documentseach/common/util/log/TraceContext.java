package com.example.documentseach.common.util.log;

/**
 * @author wangpengkai
 */
public class TraceContext {
    private String traceId;
    private String spanId;
    private String cspanId;

    public String getTraceId() {
        return this.traceId;
    }

    public String getSpanId() {
        return this.spanId;
    }

    public String getCspanId() {
        return this.cspanId;
    }

    public void setCspanId(String cspanId) {
        this.cspanId = cspanId;
    }

    public TraceContext(String traceId, String spanId) {
        this.traceId = traceId;
        this.spanId = spanId;
    }
}
