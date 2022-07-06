package com.example.documentseach.core;

import com.example.documentseach.bean.dto.OperateRecordDTO;
import com.example.documentseach.bean.po.OperateRecordPO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户操作记录服务类
 * @author wangpengkai
 */
public interface OperateRecordService {

    /**
     * 根据输入的DTO类的信息进行条件匹配
     *
     * @param param
     * @return
     */
    List<OperateRecordPO> listByCondition(OperateRecordDTO param);

    /**
     * 操作记录插入
     *
     * @param param
     * @return
     */
    int insert(OperateRecordDTO param);

    /**
     * 删除操作信息记录中相关主题的id序列之前的信息
     *
     * @param topicId
     * @param id
     */
    void deleteByTopicIdAndLessThanId(int topicId, int id);
}
