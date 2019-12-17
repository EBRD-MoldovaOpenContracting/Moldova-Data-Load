package com.datapath.moldova.loader.mtender;

import com.datapath.moldova.loader.mtender.containers.RecordPackage;
import com.datapath.moldova.loader.mtender.containers.RecordList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class MTenderRestManager {

    @Autowired
    private RestTemplate restTemplate;
    @Value("${mtender.url}")
    private String apiUrl;

    public RecordList getRecords(String offset) {
        return restTemplate.getForObject(apiUrl + "?offset={offset}", RecordList.class, offset);
    }

    public RecordPackage getRecordPackage(String ocid) {
        return restTemplate.getForObject(apiUrl + "/{ocid}", RecordPackage.class, ocid);
    }

}