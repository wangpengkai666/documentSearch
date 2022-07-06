package com.example.documentseach.persistent.dao.mysql.optrcord;

import com.example.documentseach.bean.po.OperateRecordPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author wangpengkai
 */
@Repository
public interface OperateRecordDAO {

    List<OperateRecordPO> listByCondition(OperateRecordPO param);

    int insert(OperateRecordPO po);

    void deleteByTopicIdAndLessThanId(@Param("topicId")int topicId, @Param("id") int id);
}
