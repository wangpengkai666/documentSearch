package com.example.documentseach.persistent.dao.es;

import com.example.documentseach.bean.po.ArticlePO;
import com.example.documentseach.common.util.ESUtil;
import com.example.documentseach.common.util.log.KLog;
import com.example.documentseach.common.util.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.LinkedList;
import java.util.List;

/**
 * 存储文档内容服务类
 * 这里是做了存储层的抽象话，数据持久化的操作和mybatis操作等同
 * @author wangpengkai
 */
@Repository
public class DocumentESDAO extends BaseESDAO {

    private static final KLog LOGGER = LoggerFactory.getLog(DocumentESDAO.class);

    @Autowired
    private ESUtil esUtil;

    private static final String INDEX_NAME_PRE_FIX = "article";

    public boolean storeDocuments(ArticlePO articlePO) {
        // 1.根据索引名称前缀获取当天的索引
        // 2.构造文章内容存储的结构
        // 3.进行文档的持久化操作
        return true;
    }

    public boolean storeDocuments(List<ArticlePO> articlePOs) {
        List<String> failStoreDocs = new LinkedList<>();
        for (ArticlePO articlePO : articlePOs) {
            if (!storeDocuments(articlePO)) {
                failStoreDocs.add(articlePO.toString());
            }
        }

        LOGGER.warn("class=DocumentESDAO||method=storeDocuments||fail add docs={}", failStoreDocs);

        return true;
    }
}
