package com.example.documentseach.core.impl;

import com.example.documentseach.bean.dto.OperateRecordDTO;
import com.example.documentseach.bean.po.OperateRecordPO;
import com.example.documentseach.common.util.ConvertUtil;
import com.example.documentseach.common.util.log.KLog;
import com.example.documentseach.common.util.log.LoggerFactory;
import com.example.documentseach.core.OperateRecordService;
import com.example.documentseach.persistent.dao.mysql.optrcord.OperateRecordDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author wangpengkai
 */
@Service
public class OperateRecordServiceImpl implements OperateRecordService {
    private final static KLog LOGGER = LoggerFactory.getLog(OperateRecordServiceImpl.class);

    @Autowired
    private OperateRecordDAO operateRecordDAO;

    @Override
    public List<OperateRecordPO> listByCondition(OperateRecordDTO param) {
        return operateRecordDAO.listByCondition(ConvertUtil.obj2Obj(param, OperateRecordPO.class));
    }

    @Override
    public int insert(OperateRecordDTO param) {
        return operateRecordDAO.insert(ConvertUtil.obj2Obj(param, OperateRecordPO.class));
    }

    @Override
    public void deleteByTopicIdAndLessThanId(int topicId, int id) {
        operateRecordDAO.deleteByTopicIdAndLessThanId(topicId, id);
    }
}
