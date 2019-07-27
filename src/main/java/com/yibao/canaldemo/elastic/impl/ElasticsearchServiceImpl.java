package com.yibao.canaldemo.elastic.impl;

import com.alibaba.fastjson.JSON;
import com.yibao.canaldemo.dao.entity.PatientDoctorRelationDO;
import com.yibao.canaldemo.dao.mapper.PatientDoctorRelationMapper;
import com.yibao.canaldemo.elastic.ElasticsearchService;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author houxiurong
 * @date 2019-07-27
 */
@Slf4j
@Service
public class ElasticsearchServiceImpl implements ElasticsearchService {

    @Autowired
    private TransportClient transportClient;

    @Resource
    private PatientDoctorRelationMapper patientDoctorRelationMapper;

    @Override
    public void insertById(String index, String type, String id, Map<String, Object> dataMap) {
        log.info("开始查询数据库patientDoctorRelation->id=" + Integer.valueOf(id));
        PatientDoctorRelationDO patientDoctorRelation = patientDoctorRelationMapper.selectById(Integer.valueOf(id));
        log.info("数据库查询patientDoctorRelation->data:{}", JSON.toJSONString(patientDoctorRelation));
        transportClient.prepareIndex(index, type, id).setSource(dataMap).get();
        log.info("落数据到Es-patientDoctorRelation->es-data:{}", JSON.toJSONString(dataMap));
    }

    @Override
    public void batchInsertById(String index, String type, Map<String, Map<String, Object>> idDataMap) {
        BulkRequestBuilder bulkRequestBuilder = transportClient.prepareBulk();
        idDataMap.forEach((id, dataMap) -> bulkRequestBuilder.add(transportClient.prepareIndex(index, type, id).setSource(dataMap)));
        try {
            BulkResponse bulkResponse = bulkRequestBuilder.execute().get();
            if (bulkResponse.hasFailures()) {
                log.error("elasticsearch批量插入错误, index=" + index + ", type=" + type + ", data=" + JSON.toJSONString(idDataMap) + ", cause:" + bulkResponse.buildFailureMessage());
            }
        } catch (Exception e) {
            log.error("elasticsearch批量插入错误, index=" + index + ", type=" + type + ", data=" + JSON.toJSONString(idDataMap), e);
        }
    }

    @Override
    public void update(String index, String type, String id, Map<String, Object> dataMap) {
        this.insertById(index, type, id, dataMap);
    }

    @Override
    public void deleteById(String index, String type, String id) {
        transportClient.prepareDelete(index, type, id).get();
    }
}
