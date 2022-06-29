package com.example.documentseach.task;

import com.alibaba.fastjson2.JSONObject;
import com.example.documentseach.common.util.GetJSONFromTxt;
import com.example.documentseach.common.util.RetryTimesOp;
import com.example.documentseach.common.util.log.KLog;
import com.example.documentseach.common.util.log.LoggerFactory;
import com.example.documentseach.persistent.dao.es.TemplateESDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author wangpengkai
 */
@Configuration
public class TemplateCreateTask extends ConcurrentBaseTask<String> {
    private static final KLog LOGGER = LoggerFactory.getLog(TemplateCreateTask.class);

    @Autowired
    private TemplateESDAO templateESDAO;

    private List<String> preCreateTemplateName;

    private final Integer current = 5;

    private final String taskName = "templateCreateTask";

    @PostConstruct
    public void init0() {
        this.execute();
    }

    @Override
    protected List<String> getAllItems() {
        File file = new File("src/main/java/com/example/documentseach/default/template");
        preCreateTemplateName = Arrays.stream(file.listFiles()).map(File::getName).collect(Collectors.toList());
        return preCreateTemplateName;
    }

    @Override
    protected boolean executeByBatch(TaskBatch<String> templateNames) throws Exception {
        List<String> failCreateTemplateNames = new LinkedList<>();
        for (String templateName : templateNames.getItems()) {
            // 创建对应模板并且进行重试操作
            RetryTimesOp.esRetryExecute("createTemplate", 3, () -> {
                JSONObject templateStructure = GetJSONFromTxt.readDslFileInJarFile(templateName);
                return templateESDAO.createTemplate(templateName,
                        templateStructure.getJSONArray("index_patterns").toList(String.class),
                        templateStructure.getJSONObject("mappings"),
                        templateStructure.getJSONObject("settings")
                );
            });
        }

        LOGGER.warn("class=TemplateCreateTask||method=executeByBatch||the fail add templates is {}", failCreateTemplateNames);
        return true;
    }

    @Override
    public String getTaskName() {
        return this.taskName;
    }

    @Override
    public int poolSize() {
        return Runtime.getRuntime().availableProcessors();
    }

    @Override
    public int current() {
        return this.current;
    }
}
