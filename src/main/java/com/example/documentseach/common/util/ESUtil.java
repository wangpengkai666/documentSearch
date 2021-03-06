package com.example.documentseach.common.util;

import com.example.documentseach.common.util.container.StringUtil;
import com.example.documentseach.common.util.log.KLog;
import com.example.documentseach.common.util.log.LoggerFactory;
import com.example.documentseach.persistent.client.ESOpClient;
import org.apache.lucene.search.TotalHits;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.ActionFuture;
import org.elasticsearch.action.admin.indices.alias.get.GetAliasesRequest;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.open.OpenIndexRequest;
import org.elasticsearch.action.admin.indices.open.OpenIndexResponse;
import org.elasticsearch.action.admin.indices.stats.IndexStats;
import org.elasticsearch.action.admin.indices.stats.IndicesStatsRequest;
import org.elasticsearch.action.admin.indices.stats.IndicesStatsResponse;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.*;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.WriteRequest;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.action.support.replication.ReplicationResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.GetAliasesResponse;
import org.elasticsearch.client.IndicesAdminClient;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.*;
import org.elasticsearch.cluster.metadata.AliasMetadata;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.core.TimeValue;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.xcontent.XContentBuilder;
import org.elasticsearch.xcontent.XContentFactory;
import org.elasticsearch.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author wangpengkai
 */
@Component
public class ESUtil {

    private static final KLog LOGGER = LoggerFactory.getLog(ESUtil.class);
    @Autowired
    private ESOpClient esOpClient;

    private RestHighLevelClient client;

    private static final String INDEX_KEY = "index";
    private static final String INDEX = "spider";
    private static final String SHARDS = "index.number_of_shards";
    private static final String REPLICAS = "index.number_of_replicas";

    private static final String ID = "id";
    private static final String JSON_STR = "json";
    private static final String CREATED = "created";
    private static final String UPDATED = "updated";
    private static final String DELETED = "deleted";
    private static final String CONFLICT = "conflict";
    private static final String NOT_FOUND = "not_found";

    @PostConstruct
    public void init() {
        client = esOpClient.getClient();
    }


    /*** =========================================    ??????    =============================================== ***/

    /**
     * ??????es??????????????????
     *
     * @param indexName
     * @return
     */
    public boolean checkIndexExist(String indexName) throws IOException {
        GetIndexRequest request = new GetIndexRequest(indexName);
        request.local(false);
        request.humanReadable(true);
        request.includeDefaults(false);
        return client.indices().exists(request, RequestOptions.DEFAULT);
    }

    /**
     * ??????es??????
     *
     * @param indexName
     * @return
     */
    public boolean createIndex(String indexName) throws IOException {
        if (checkIndexExist(indexName)) {
            LOGGER.error("class=ESUtil||method=createIndex||errMsg={}", indexName + " index has exited");
            return false;
        }
        CreateIndexRequest request = new CreateIndexRequest(indexName);
        CreateIndexResponse response = client.indices().create(request, RequestOptions.DEFAULT);
        // ??????????????????????????????????????????
        if (response.isAcknowledged() || response.isShardsAcknowledged()) {
            LOGGER.info("class=ESUtil||method=createIndex||msg={}", indexName + " build success");
            return true;
        }
        return false;
    }

    /**
     * ??????????????????????????????????????????
     *
     * @param indexName
     * @param shards
     * @param replicas
     * @return
     * @throws IOException
     */
    public boolean createIndexWithShardsAndRep(String indexName, int shards, int replicas) throws IOException {
        if (checkIndexExist(indexName)) {
            LOGGER.error("class=ESUtil||method=createIndex||errMsg={}", indexName + " index has exited");
            return false;
        }
        Settings.Builder builder = Settings.builder().put(SHARDS, shards).put(REPLICAS, replicas);
        CreateIndexRequest request = new CreateIndexRequest(indexName).settings(builder);
        CreateIndexResponse response = client.indices().create(request, RequestOptions.DEFAULT);
        if (response.isAcknowledged() || response.isShardsAcknowledged()) {
            LOGGER.info("class=ESUtil||method=createIndex||msg={}", indexName + " build success");
            return true;
        }
        return false;
    }

    /**
     * ????????????
     *
     * @param indexName
     * @return
     * @throws IOException
     */
    public boolean deleteIndex(String indexName) throws IOException {
        if (!checkIndexExist(indexName)) {
            LOGGER.error(indexName + " ???????????????");
            return false;
        }
        DeleteIndexRequest request = new DeleteIndexRequest(indexName);
        AcknowledgedResponse response = client.indices().delete(request, RequestOptions.DEFAULT);
        if (response.isAcknowledged()) {
            LOGGER.info("class=ESUtil||method=deleteIndex||msg={}", indexName + " delete success");
            return true;
        }
        return false;
    }

    /**
     * ????????????
     *
     * @param indexName
     * @return
     * @throws IOException
     */
    public boolean openIndex(String indexName) throws IOException {
        if (!checkIndexExist(indexName)) {
            LOGGER.error("class=ESUtil||method=openIndex||errMsg={}", indexName + "does not exit");
            return false;
        }
        OpenIndexRequest request = new OpenIndexRequest(indexName);
        OpenIndexResponse response = client.indices().open(request, RequestOptions.DEFAULT);
        // ??????????????????????????????????????????
        if (response.isAcknowledged() || response.isShardsAcknowledged()) {
            LOGGER.info("class=ESUtil||method=openIndex||msg={}", indexName + "open success");
            return true;
        }
        return false;
    }

    /**
     * ????????????
     *
     * @param indexName
     * @return
     * @throws IOException
     */
    public boolean closeIndex(String indexName) throws IOException {
        if (!checkIndexExist(indexName)) {
            LOGGER.error("class=ESUtil||method=closeIndex||errMsg={}", indexName + "does not exit");
            return false;
        }
        CloseIndexRequest request = new CloseIndexRequest(indexName);
        AcknowledgedResponse response = client.indices().close(request, RequestOptions.DEFAULT);
        // ??????????????????????????????????????????
        if (response.isAcknowledged()) {
            LOGGER.info("class=ESUtil||method=closeIndex||msg={}", indexName + " open success");
            return true;
        }
        return false;
    }


    /*** =========================================    mapping    =============================================== ***/

    private XContentBuilder createBuilder() throws IOException {
        XContentBuilder builder = XContentFactory.jsonBuilder();
        builder.startObject();
        builder.startObject("properties");
        builder.startObject("message");
        builder.field("type", "text");
        // ???message?????????????????????
        // builder.field("analyzer", "ik_smart")
        builder.endObject();

        builder.startObject("timestamp");
        builder.field("type", "dateTime");
        // ???????????????????????????Long??????
        builder.field("format", "epoch_millis");
        builder.endObject();
        builder.endObject();
        builder.endObject();
        return builder;
    }

    public void setFieldsMapping(String indexName) throws IOException {
        if (!checkIndexExist(indexName)) {
            return;
        }
        try {
            PutMappingRequest request = new PutMappingRequest(indexName);
            request.source(createBuilder());
            AcknowledgedResponse response = client.indices().putMapping(request, RequestOptions.DEFAULT);
            if (response.isAcknowledged()) {
                LOGGER.info("class=ESUtil||method=setFieldsMapping||msg={}", indexName + " mapping set success");
            }
        } catch (IOException e) {
            LOGGER.error("class=ESUtil||method=setFieldsMapping||errMsg={}", "the type of doc is wrong");
            return;
        }
    }

    /**
     * ??????????????????mapping
     * @param indexName
     * @param mapping
     * @return
     * @throws IOException
     */
//    public static boolean createOrUpdateMapping(String indexName, IElasticSearchMapping mapping) throws IOException {
//        try {
//            // mapping????????????????????????????????????
//            if (!checkMappingExist(indexName)) {
//                putMapping(indexName, mapping);
//                return true;
//            } else {
//                postMapping(indexName, mapping);
//                return true;
//            }
//
//        } catch (IOException e) {
//            LOGGER.error("??????????????????mapping?????????", e);
//            return false;
//        }
//    }


    /**
     * ??????es???mapping????????????
     * @param indexName
     * @return
     */


    /**
     * ??????mapping???put??????
     * @param indexName
     * @param mapping
     * @return
     */
//    public static boolean putMapping(String indexName, IElasticSearchMapping mapping) throws IOException {
//        if (!checkIndexExist(indexName)) {
//            LOGGER.error(indexName + " ???????????????");
//            return false;
//        }
//        String mappingJson = Strings.toString(mapping.getMapping());
//        PutMappingRequest putMappingRequest = new PutMappingRequest(indexName).source(mappingJson, XContentType.JSON);
//        AcknowledgedResponse response = client.indices().putMapping(putMappingRequest, RequestOptions.DEFAULT);
//        return response.isAcknowledged();
//    }

    /**
     * ??????mapping,post??????
     * @param indexName
     * @param mapping
     * @return
     */


    /**
     * =========================================    mapping    ===============================================
     **/


    /**
     * ????????????
     *
     * @param indexName
     * @param id
     * @param jsonString
     * @throws IOException
     */
    public void addDocByJson(String indexName, String id, String jsonString) throws IOException {
        if (!checkIndexExist(indexName)) {
            createIndex(indexName);
        }
        // request???opType?????????INDEX(????????????id????????????document???CREATE?????????????????????)
        IndexRequest request = new IndexRequest(indexName).id(id).source(jsonString, XContentType.JSON);
        IndexResponse response = null;
        try {
            response = client.index(request, RequestOptions.DEFAULT);

            String index = response.getIndex();
            String docId = response.getId();
            if (response.getResult().getLowercase().equals(CREATED)) {
                LOGGER.info("class=ESUtil||method=addDocByJson||msg={}||index={}||id={}", "add doc success", index, docId);
            } else if (response.getResult().getLowercase().equals(UPDATED)) {
                LOGGER.info("class=ESUtil||method=addDocByJson||msg={}||index={}||id={}", "update doc success", index, docId);
            }

            // ??????????????????
            ReplicationResponse.ShardInfo shardInfo = response.getShardInfo();
            if (shardInfo.getTotal() != shardInfo.getSuccessful()) {
                LOGGER.error("class=ESUtil||method=addDocByJson||errMsg={}||id={}", "not all doc add to shard ", docId);
            }
            // ??????????????????????????????
            if (shardInfo.getFailed() > 0) {
                for (ReplicationResponse.ShardInfo.Failure failure : shardInfo.getFailures()) {
                    LOGGER.error("class=ESUtil||method=addDocByJson||errMsg={}", "not all doc add to shard " + failure.reason());
                }
            }
        } catch (ElasticsearchException e) {
            if (e.status().equals(CONFLICT)) {
                LOGGER.error("class=ESUtil||method=addDocByJson||errMsg={}", "doc version conflict");
            }
            LOGGER.error("class=ESUtil||method=addDocByJson||errMsg={}", "add doc fail" + e.getDetailedMessage());
        }
    }

    /**
     * ????????????
     *
     * @param indexName
     * @param id
     * @return
     * @throws IOException
     */
    public Map<String, Object> getDoc(String indexName, String id) throws IOException {
        Map<String, Object> resultMap = new HashMap();
        GetRequest request = new GetRequest(indexName, id);
        // ??????
        request.realtime(false);
        // ????????????????????????
        request.refresh(true);

        GetResponse response = null;
        try {
            response = client.get(request, RequestOptions.DEFAULT);
        } catch (ElasticsearchException e) {
            if (e.status().equals(NOT_FOUND)) {
                LOGGER.error("class=ESUtil||method=getDoc||errMsg={}", e.getDetailedMessage());
            }
            if (e.status().equals(CONFLICT)) {
                LOGGER.error("class=ESUtil||method=getDoc||errMsg={}", "version conflict");
            }
            LOGGER.error("class=ESUtil||method=getDoc||errMsg={}", "find doc fail" + e.getDetailedMessage());
        }

        if (response != null) {
            if (response.isExists()) {
                resultMap = response.getSourceAsMap();
            } else {
                LOGGER.error("class=ESUtil||method=getDoc||errMsg={}", "can not find doc");
            }
        }
        return resultMap;
    }

    /**
     * ????????????
     *
     * @param indexName
     * @param id
     * @throws IOException
     */
    public void deleteDoc(String indexName, String id) throws IOException {
        DeleteRequest request = new DeleteRequest(indexName, id);
        DeleteResponse response = null;
        try {
            response = client.delete(request, RequestOptions.DEFAULT);
        } catch (ElasticsearchException e) {
            if (e.status().equals(CONFLICT)) {
                LOGGER.error("class=ESUtil||method=deleteDoc||errMsg={}", "version conflict");
            }
            LOGGER.error("class=ESUtil||method=deleteDoc||errMsg={}", "delete doc fail" + e.getDetailedMessage());
        }

        if (response != null) {
            if (response.getResult().getLowercase().equals(NOT_FOUND)) {
                LOGGER.error("class=ESUtil||method=deleteDoc||errMsg={}", "can not find doc");
            }

            // ????????????
            ReplicationResponse.ShardInfo shardInfo = response.getShardInfo();
            if (shardInfo.getTotal() != shardInfo.getSuccessful()) {
                LOGGER.error("class=ESUtil||method=deleteDoc||errMsg={}", "part of shard can not response");
            }
            if (shardInfo.getFailed() > 0) {
                for (ReplicationResponse.ShardInfo.Failure failure : shardInfo.getFailures()) {
                    LOGGER.error("class=ESUtil||method=deleteDoc||errMsg={}", failure.reason());
                }
            }
            LOGGER.info("class=ESUtil||method=deleteDoc||Msg={}||index={}||id={}", "delete doc success", indexName, id);
        }
    }

    /**
     * ????????????JSON???????????????????????????????????????????????????????????????
     *
     * @param indexName
     * @param id
     * @param jsonString
     * @throws IOException
     */
    public void updateDocByJson(String indexName, String id, String jsonString) throws IOException {
        if (!checkIndexExist(indexName)) {
            createIndex(indexName);
        }
        UpdateRequest request = new UpdateRequest(indexName, id);
        request.doc(jsonString, XContentType.JSON);
        // ??????????????????????????????????????????????????????????????????????????????
        request.docAsUpsert(true);
        try {
            UpdateResponse response = client.update(request, RequestOptions.DEFAULT);
            String index = response.getIndex();
            String docId = response.getId();
            if (response.getResult().getLowercase().equals(CREATED)) {
                LOGGER.info("class=ESUtil||method=updateDocByJson||msg={}||index={}||id={}", "doc add success", index, docId);
            } else if (response.getResult().getLowercase().equals(UPDATED)) {
                LOGGER.info("class=ESUtil||method=updateDocByJson||msg={}||index={}||id={}", "doc update success", index, docId);
            } else if (response.getResult().getLowercase().equals(DELETED)) {
                LOGGER.error("class=ESUtil||method=updateDocByJson||msg={}||index={}||id={}", "doc has been removed", index, docId);
            }

            ReplicationResponse.ShardInfo shardInfo = response.getShardInfo();
            if (shardInfo.getTotal() != shardInfo.getSuccessful()) {
                LOGGER.error("class=ESUtil||method=updateDocByJson||errMsg={}||index={}||id={}", "not all shard get response", index, docId);
            }
            if (shardInfo.getFailed() > 0) {
                for (ReplicationResponse.ShardInfo.Failure failure : shardInfo.getFailures()) {
                    LOGGER.error("class=ESUtil||method=updateDocByJson||errMsg={}||index={}||id={}", failure.reason(), index, docId);
                }
            }
        } catch (ElasticsearchException e) {
            if (e.status().equals("not_found")) {
                LOGGER.error("class=ESUtil||method=updateDocByJson||errMsg={}", "can not find doc");
            } else if (e.status().equals("conflict")) {
                LOGGER.error("class=ESUtil||method=updateDocByJson||errMsg={}", "version conflict");
            }
        }
    }

    /**
     * ???????????????????????????????????????????????????????????????????????? [{"index":"xxx","id":"xx","json":"xxx"}, {"index":"xxx","id":"xx","json":"xxx"}]
     *
     * @param params
     * @throws IOException
     */
    public void bulkAdd(List<Map<String, String>> params) throws IOException {
        if (CollectionUtils.isEmpty(params)) {
            return;
        }
        BulkRequest bulkRequest = new BulkRequest();
        for (Map<String, String> map : params) {
            String index = map.getOrDefault(INDEX_KEY, INDEX);
            String id = map.get(ID);
            String jsonString = map.get(JSON_STR);
            if (StringUtil.isNotBlank(id) && StringUtil.isNotBlank(jsonString)) {
                IndexRequest request = new IndexRequest(index).id(id).source(jsonString, XContentType.JSON);
                bulkRequest.add(request);
            }
        }
        // ???????????????2??????
        bulkRequest.timeout(TimeValue.timeValueMinutes(2L));
        // ????????????
        bulkRequest.setRefreshPolicy(WriteRequest.RefreshPolicy.WAIT_UNTIL);
        if (bulkRequest.numberOfActions() == 0) {
            LOGGER.error("class=ESUtil||method=bulkAdd||errMsg={}", "bulk add fail:parameter is wrong");
            return;
        }
        BulkResponse bulkResponse = client.bulk(bulkRequest, RequestOptions.DEFAULT);
        // ??????????????????
        if (!bulkResponse.hasFailures()) {
            LOGGER.info("class=ESUtil||method=bulkAdd||msg={}", "bulk success");
        } else {
            for (BulkItemResponse bulkItemResponse : bulkResponse) {
                if (bulkItemResponse.isFailed()) {
                    BulkItemResponse.Failure failure = bulkItemResponse.getFailure();
                    LOGGER.error("class=ESUtil||method=bulkAdd||errMsg={}", failure.getMessage());
                } else {
                    LOGGER.info("class=ESUtil||method=bulkAdd||msg={}", "bulk success");
                }
            }
        }
    }

    /**
     * ??????????????????????????????????????????????????????????????????????????? [{"index":"xxx","id":"xx","json":"xxx"}, {"index":"xxx","id":"xx","json":"xxx"}]
     *
     * @param params
     * @throws IOException
     */
    public void bulkUpdate(List<Map<String, String>> params) throws IOException {
        if (CollectionUtils.isEmpty(params)) {
            return;
        }
        BulkRequest bulkRequest = new BulkRequest();
        for (Map<String, String> map : params) {
            String index = map.getOrDefault(INDEX_KEY, INDEX);
            String id = map.get(ID);
            String jsonString = map.get(JSON_STR);
            if (StringUtil.isNotBlank(id) && StringUtil.isNotBlank(jsonString)) {
                UpdateRequest request = new UpdateRequest(index, id).doc(jsonString, XContentType.JSON);
                request.docAsUpsert(true);
                bulkRequest.add(request);
            }
        }
        if (bulkRequest.numberOfActions() == 0) {
            LOGGER.error("class=ESUtil||method=bulkUpdate||errMsg={}", "bulk update fail:parameter is wrong");
            return;
        }
        bulkRequest.timeout(TimeValue.timeValueMinutes(2L));
        bulkRequest.setRefreshPolicy(WriteRequest.RefreshPolicy.WAIT_UNTIL);
        BulkResponse bulkResponse = client.bulk(bulkRequest, RequestOptions.DEFAULT);
        if (!bulkResponse.hasFailures()) {
            LOGGER.info("class=ESUtil||method=bulkUpdate||msg={}", "bulk update success");
            return;
        } else {
            for (BulkItemResponse bulkItemResponse : bulkResponse) {
                if (bulkItemResponse.isFailed()) {
                    BulkItemResponse.Failure failure = bulkItemResponse.getFailure();
                    LOGGER.error("class=ESUtil||method=bulkUpdate||errMsg={}", failure.getMessage());
                } else {
                    LOGGER.info("class=ESUtil||method=bulkUpdate||msg={}", "bulk update success");
                }
            }
        }
    }

    /**
     * ????????????,?????????????????? [{"index":"xx","id":"xx"}, {"index":"xx","id":"xx"}]
     *
     * @param params
     * @throws IOException
     */
    public void bulkDelete(List<Map<String, String>> params) throws IOException {
        if (CollectionUtils.isEmpty(params)) {
            return;
        }
        BulkRequest bulkRequest = new BulkRequest();
        for (Map<String, String> param : params) {
            String index = param.getOrDefault(INDEX_KEY, INDEX);
            String id = param.get(ID);
            if (StringUtil.isNotBlank(id)) {
                DeleteRequest request = new DeleteRequest(index, id);
                bulkRequest.add(request);
            }
        }
        if (bulkRequest.numberOfActions() == 0) {
            LOGGER.error("class=ESUtil||method=bulkDelete||errMsg={}", "bulk delete fail:parameter is wrong");
            return;
        }
        bulkRequest.timeout(TimeValue.timeValueMinutes(2L));
        bulkRequest.setRefreshPolicy(WriteRequest.RefreshPolicy.WAIT_UNTIL);
        BulkResponse bulkResponse = client.bulk(bulkRequest, RequestOptions.DEFAULT);
        if (!bulkResponse.hasFailures()) {
            LOGGER.info("class=ESUtil||method=bulkDelete||msg={}", "bulk delete success");
        } else {
            for (BulkItemResponse bulkItemResponse : bulkResponse) {
                if (bulkItemResponse.isFailed()) {
                    BulkItemResponse.Failure failure = bulkItemResponse.getFailure();
                    LOGGER.error("class=ESUtil||method=bulkDelete||errMsg={}", failure.getMessage());
                } else {
                    LOGGER.info("class=ESUtil||method=bulkDelete||msg={}", "bulk success");
                }
            }
        }
    }

    /**
     * ????????????,?????????????????????????????????????????????[{"index":"xx","id":"xx"}, {"index":"xx","id":"xx"}]
     *
     * @param params
     * @return
     * @throws IOException
     */
    public List<Map<String, Object>> multiGet(List<Map<String, String>> params) throws IOException {
        List<Map<String, Object>> resultList = new ArrayList<>();

        MultiGetRequest request = new MultiGetRequest();
        for (Map<String, String> param : params) {
            String index = param.getOrDefault(INDEX_KEY, INDEX);
            String id = param.get(ID);
            if (StringUtil.isNotBlank(id)) {
                request.add(new MultiGetRequest.Item(index, id));
            }
        }
        request.realtime(false);
        request.refresh(true);
        MultiGetResponse response = client.mget(request, RequestOptions.DEFAULT);
        List<Map<String, Object>> list = parseMGetResponse(response);
        if (CollectionUtils.isNotEmpty(list)) {
            resultList.addAll(list);
        }
        return resultList;
    }

    private static List<Map<String, Object>> parseMGetResponse(MultiGetResponse response) {
        List<Map<String, Object>> resultList = new ArrayList<>();
        MultiGetItemResponse[] responses = response.getResponses();
        for (MultiGetItemResponse itemResponse : responses) {
            GetResponse getResponse = itemResponse.getResponse();
            if (getResponse != null) {
                if (!getResponse.isExists()) {
                    LOGGER.error("class=ESUtil||method=parseMGetResponse||errMsg={}||index={}||id={}",
                            "find doc fail", getResponse.getIndex(), getResponse.getId());
                } else {
                    resultList.add(getResponse.getSourceAsMap());
                }
            } else {
                MultiGetResponse.Failure failure = itemResponse.getFailure();
                ElasticsearchException e = (ElasticsearchException) failure.getFailure();
                if ("not_found".equals(e.status())) {
                    LOGGER.error("class=ESUtil||method=parseMGetResponse||errMsg={}||index={}||id={}",
                            "can not find doc", getResponse.getIndex(), getResponse.getId());
                } else if (e.status().equals("conflict")) {
                    LOGGER.error("class=ESUtil||method=parseMGetResponse||errMsg={}||index={}||id={}",
                            "doc conflict", getResponse.getIndex(), getResponse.getId());
                }
            }
        }
        return resultList;
    }


    /**
     * ??????es??????????????????????????????
     * @return
     * @throws IOException
     */
    public Set<String> getAllIndex() throws IOException {
        Set<String> indexResult = new HashSet<>();
        try {
            GetAliasesRequest request = new GetAliasesRequest();
            GetAliasesResponse getAliasesResponse = client.indices().getAlias(request, RequestOptions.DEFAULT);
            Map<String, Set<AliasMetadata>> aliases = getAliasesResponse.getAliases();
            if (aliases == null || aliases.isEmpty()) return indexResult;
            aliases.values().forEach(indexSet -> indexResult.addAll(indexSet.stream()
                    .map(AliasMetadata::alias)
                    .collect(Collectors.toList())));
            return indexResult;
        } catch (Exception e) {
            LOGGER.error("class=ESUtil||method=getAllIndex||errMsg={}", e.getMessage());
        }

        return indexResult;
    }

    /**
     * ????????????????????????????????????
     */
    public String searchAllDocInIndices(String... indices) {
        // ??????search??????
        SearchRequest searchRequest = new SearchRequest(indices);
        // ????????????source??????
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        // ??????????????????-?????????????????????
        searchSourceBuilder.query(QueryBuilders.matchAllQuery());
        // ?????????????????????????????????
        searchRequest.source(searchSourceBuilder);
        // ??????????????????es??????http??????
        SearchResponse searchResponse = null;
        try {
            searchResponse = client.search(searchRequest,RequestOptions.DEFAULT);
        } catch (IOException e) {
            LOGGER.error("class=ESUtil||method=searchAllDocInIndices||indices={}||errMsg={}",indices,e.getMessage());
            return null;
        }
        // ??????????????????
        SearchHits hits = searchResponse.getHits();
        // ????????????????????????????????????
        TotalHits totalHits = hits.getTotalHits();
        // ?????????????????????????????????
        SearchHit[] searchHits = hits.getHits();
        // ?????????????????????
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for(SearchHit searchHit:searchHits) {
            // ???????????????
            String id = searchHit.getId();
            // ???????????????
            Map<String, Object> sourceAsMap = searchHit.getSourceAsMap();
            // ???????????????
            try {
                Date timestamp = dateFormat.parse((String) sourceAsMap.get("timestamp"));
            } catch (Exception e) {
                LOGGER.error("class=ESUtil||method=searchAllDocInIndices||||errMsg={}",indices,e.getMessage());
            }
        }

        return searchResponse.toString();
    }


    /**
     * ???????????????????????????????????????
     */
    public String searchByKeyword(String word,String... indices) {
        return null;
    }

    /**
     * ???????????????????????????search-after???????????????????????????
     */

    /**
     * ??????????????????????????????scroll??????
     */
}
