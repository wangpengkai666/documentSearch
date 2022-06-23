package com.example.documentseach.persistent.dao.es;

import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.indices.PutIndexTemplateRequest;
import org.elasticsearch.common.settings.Setting;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author wangpengkai
 */
@Repository
public class TemplateESDAO extends BaseESDAO {
    private static final Logger LOGGER = LoggerFactory.getLogger(TemplateESDAO.class.getName());

    public boolean createTemplate(String templateName, List<String> patterns, Map<String, Object> map, Map<String, Object> settings) {
        PutIndexTemplateRequest putIndexTemplateRequest = new PutIndexTemplateRequest(templateName)
                .patterns(patterns)
                .mapping(map)
                .settings(settings);
        try {
            client.indices().putTemplate(putIndexTemplateRequest, RequestOptions.DEFAULT);
        } catch (Exception e) {
            LOGGER.error("class:TemplateESDAO||method:createTemplate" + e.getMessage());
            return false;
        }

        return true;
    }
}
