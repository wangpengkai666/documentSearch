package com.example.documentseach.task;

import lombok.Data;

import java.util.LinkedList;
import java.util.List;

/**
 * @author wangpengkai
 */
@Data
public class TaskBatch<T> {
    /**
     * 批次中需要处理的内容
     */
    private List<T> items = new LinkedList<>();
}
