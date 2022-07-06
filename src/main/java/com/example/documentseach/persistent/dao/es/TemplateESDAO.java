package com.example.documentseach.persistent.dao.es;

import com.example.documentseach.common.util.log.KLog;
import com.example.documentseach.common.util.container.ListUtil;
import com.example.documentseach.common.util.log.LoggerFactory;
import org.elasticsearch.action.admin.indices.template.delete.DeleteIndexTemplateRequest;
import org.elasticsearch.action.admin.indices.template.get.GetIndexTemplatesResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.indices.GetIndexTemplatesRequest;
import org.elasticsearch.client.indices.PutIndexTemplateRequest;
import org.elasticsearch.cluster.metadata.IndexTemplateMetadata;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author wangpengkai
 */
@Repository
public class TemplateESDAO extends BaseESDAO {

    private static final KLog LOGGER = LoggerFactory.getLog(TemplateESDAO.class);

    /**
     * 以map的形式来创建模板，主要是根据模板的名称,匹配模式,字段设置关系,设置,别名来进行设置
     * @param templateName
     * @param patterns
     * @param mappings
     * @param settings
     * @return
     */
    public boolean createTemplate(String templateName, List<String> patterns, Map<String, Object> mappings, Map<String, Object> settings) {
        PutIndexTemplateRequest putIndexTemplateRequest = new PutIndexTemplateRequest(templateName)
                .patterns(patterns)
                .mapping(mappings)
                .settings(settings);
        try {
            AcknowledgedResponse acknowledgedResponse = client.indices().putTemplate(putIndexTemplateRequest, RequestOptions.DEFAULT);
            LOGGER.warn("class=TemplateESDAO||method=createTemplate||errMsg={}",acknowledgedResponse);
        } catch (Exception e) {
            LOGGER.error("class=TemplateESDAO||method=createTemplate||errMsg={}", e.getMessage());
            return false;
        }

        return true;
    }

    /**
     * 根据模板的名称删除对应的模板
     * @param templateName
     * @return
     */
    public boolean deleteTemplate(String templateName) {
        DeleteIndexTemplateRequest deleteIndexTemplateRequest = new DeleteIndexTemplateRequest(templateName);
        try {
            client.indices().deleteTemplate(deleteIndexTemplateRequest,RequestOptions.DEFAULT);
            
        } catch (IOException e) {
            LOGGER.error("class=TemplateESDAO||method=deleteTemplate||errMsg={}","delete index template fail");
            return false;
        }
        
        return true;
    }

    /**
     * 获取和模板明名称匹配的模板数值
     * @param templateName
     * @return
     */
    public List<IndexTemplateMetadata> getIndexTemplatesByName(String templateName) {
        GetIndexTemplatesRequest getIndexTemplatesRequest = new GetIndexTemplatesRequest(templateName);
        try {
            GetIndexTemplatesResponse template = client.indices().getTemplate(getIndexTemplatesRequest, RequestOptions.DEFAULT);
            List<IndexTemplateMetadata> indexTemplates = template.getIndexTemplates();
            return ListUtil.isEmpty(indexTemplates) ? new LinkedList<>() : indexTemplates;
        } catch (IOException e) {
            LOGGER.error("class=TemplateESDAO||method=deleteTemplate||errMsg={}", "get index template fail");
            return new LinkedList<>();
        }
    }
}
