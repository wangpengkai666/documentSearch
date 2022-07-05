package com.example.documentseach.dao;

import com.example.documentseach.DocumentApplicationTest;
import com.example.documentseach.bean.po.OperateRecordPO;
import com.example.documentseach.persistent.dao.mysql.optrcord.OperateRecordDAO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

public class OperateRecordDAOTest extends DocumentApplicationTest {
    @Autowired
    private OperateRecordDAO operateRecordDAO;

    @Test
    public void insertTest() {
        OperateRecordPO operateRecordPO = new OperateRecordPO();
        operateRecordPO.setOperator("wpk");
        operateRecordPO.setContent("test");
        operateRecordPO.setTopicId(1);
        operateRecordPO.setType("test");
        operateRecordPO.setCreateTime(new Date());
        operateRecordPO.setUpdateTime(new Date());
        Assertions.assertEquals(1, operateRecordDAO.insert(operateRecordPO));
    }
}
