package com.example.documentseach.persistent.dao.es;

import com.alibaba.fastjson2.JSONObject;
import com.example.documentseach.common.util.ESUtil;
import com.example.documentseach.common.util.log.KLog;
import com.example.documentseach.common.util.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * @author wangpengkai
 */
@Repository
public class IndexESDAO extends BaseESDAO {

    private static final KLog LOGGER = LoggerFactory.getLog(IndexESDAO.class);
    @Autowired
    private ESUtil esUtil;

    /**
     * 创建指定索引名称的es索引
     * @return
     */
    public boolean createIndex(String indexName) {
        try {
            return esUtil.createIndex(indexName);
        } catch (Exception e) {
            LOGGER.error("class=IndexESDAO||method=createIndex||create index fail||indexName={}||errMsg={}",
                    indexName, e.getMessage());
            return false;
        }
    }

    /**
     * 创建带有副本，分片信息的索引
     * @param indexName
     * @param shards
     * @param replicas
     * @return
     */
    public boolean createIndexWithShardsAndRep(String indexName, int shards, int replicas) {
        try {
            return esUtil.createIndexWithShardsAndRep(indexName,shards,replicas);
        } catch (Exception e) {
            LOGGER.error("class=IndexESDAO||method=createIndexWithShardsAndRep||create index fail||indexName={}||errMsg={}",
                    indexName, e.getMessage());
            return false;
        }
    }

    /**
     * 删除指定索引
     * @return
     */
    public boolean deleteIndex(String indexName) {
        try {
            return esUtil.deleteIndex(indexName);
        } catch (Exception e) {
            LOGGER.error("class=IndexESDAO||method=deleteIndex||delete index fail||indexName={}||errMsg={}",
                    indexName, e.getMessage());
            return false;
        }
    }

    /**
     * @return
     */
    public boolean updateIndexSettings() {
        return false;
    }

    /**
     * @return
     */
    public boolean updateIndexMappings() {
        return false;
    }

    /**
     * @param indexName
     * @return
     */
    public JSONObject getIndexStructure(String indexName) {
        return null;
    }
}
