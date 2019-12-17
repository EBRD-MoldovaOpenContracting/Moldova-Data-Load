package com.datapath.moldova.loader.mapps;

import com.datapath.moldova.loader.mapps.containers.TendersList;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import static org.springframework.util.CollectionUtils.isEmpty;

@Component
@Slf4j
public class MappsHandler {

    @Autowired
    private MappsRestManager restManager;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MongoTemplate mongoTemplate;

    public void handle() {
        try {
            String offset = "";
            TendersList tendersList;

            do {
                tendersList = restManager.getTenders(offset);
                if (tendersList.getData() == null) break;

                for (TendersList.TenderData record : tendersList.getData()) {
                    String tender = restManager.getTender(record.getId());
                    JsonNode dataNode = objectMapper.readTree(tender);
                    log.info("id = [{}], date = {}", record.getId(), record.getDate());
                    mongoTemplate.save(dataNode.at("/data").toString(), "ocds_release");
                    offset = tendersList.getNextPage().getOffset();
                }

            } while (!isEmpty(tendersList.getData()));
        } catch (JsonProcessingException e) {
            log.error(e.getMessage(), e);
        }
    }
}