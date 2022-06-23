package com.example.documentseach.persistent.dao.es;

import com.example.documentseach.common.util.log.KLog;
import com.example.documentseach.common.util.log.LoggerFactory;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.indices.PutIndexTemplateRequest;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author wangpengkai
 */
@Repository
public class TemplateESDAO extends BaseESDAO {
    private static final KLog LOGGER = LoggerFactory.getLog(TemplateESDAO.class);

    public boolean createTemplate(String templateName, List<String> patterns, Map<String, Object> map, Map<String, Object> settings) {
        PutIndexTemplateRequest putIndexTemplateRequest = new PutIndexTemplateRequest(templateName)
                .patterns(patterns)
                .mapping(map)
                .settings(settings);
        try {
            client.indices().putTemplate(putIndexTemplateRequest, RequestOptions.DEFAULT);
        } catch (Exception e) {
            LOGGER.error("class=TemplateESDAO||method=createTemplate||errMsg={}", e.getMessage());
            return false;
        }

        return true;
    }
}
