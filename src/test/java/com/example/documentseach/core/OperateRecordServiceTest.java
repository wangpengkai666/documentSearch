package com.example.documentseach.core;

import com.example.documentseach.DocumentApplicationTest;
import com.example.documentseach.bean.dto.OperateRecordDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

public class OperateRecordServiceTest extends DocumentApplicationTest {

    @Autowired
    private OperateRecordService operateRecordService;

    @Test
    public void insertTest() {
        OperateRecordDTO operateRecordDTO = new OperateRecordDTO();
        operateRecordDTO.setOperator("wpk");
        operateRecordDTO.setContent("test");
        operateRecordDTO.setTopicId(1);
        operateRecordDTO.setType("1");
        operateRecordDTO.setCreateTime(new Date());
        operateRecordDTO.setUpdateTime(new Date());
        Assertions.assertEquals(1, operateRecordService.insert(operateRecordDTO));
    }
}
