package com.example.documentseach.task;

import com.alibaba.fastjson2.JSONObject;
import com.example.documentseach.common.util.GetJSONFromTxt;
import com.example.documentseach.common.util.log.KLog;
import com.example.documentseach.common.util.log.LoggerFactory;
import com.example.documentseach.persistent.dao.es.TemplateESDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.*;

/**
 * @author wangpengkai
 */
@Configuration
public class TemplateCreateTask {
    private static final KLog LOGGER = LoggerFactory.getLog(TemplateCreateTask.class);

    @Autowired
    private TemplateESDAO templateESDAO;

    private Set<String> preCreateTemplateName = new HashSet<>(Arrays.asList("article"));

    @PostConstruct
    public void init() {
        try {
            List<String> failCreateTemplateNames = new LinkedList<>();
            for (String templateName : preCreateTemplateName) {
                JSONObject templateStructure = GetJSONFromTxt.readDslFileInJarFile(templateName);
                boolean template = templateESDAO.createTemplate(templateName,
                        templateStructure.getJSONArray("index_patterns").toList(String.class),
                        templateStructure.getJSONObject("mappings"),
                        templateStructure.getJSONObject("settings")
                );
                if (!template) failCreateTemplateNames.add(templateName);
            }
            LOGGER.warn("class=TemplateCreateTask||method=init||the fail add templates is {}", failCreateTemplateNames);
        } catch (Exception e) {
            LOGGER.error("class=TemplateCreateTask||method=init||errMsg={}", e.getMessage());
        }
    }
}
