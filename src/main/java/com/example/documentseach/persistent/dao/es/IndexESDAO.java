package com.example.documentseach.persistent.dao.es;

import com.alibaba.fastjson2.JSONObject;
import org.springframework.stereotype.Repository;

/**
 * @author wangpengkai
 */
@Repository
public class IndexESDAO extends BaseESDAO {
    /**
     * @return
     */
    public boolean createIndex() {
        return false;
    }

    /**
     * @return
     */
    public boolean deleteIndex() {
        return false;
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
