package com.datapath.moldova.loader.mapps;

import com.datapath.moldova.loader.mapps.containers.TendersList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class MappsRestManager {

    @Autowired
    private RestTemplate restTemplate;
    @Value("${mapps.url}")
    private String apiUrl;

    public TendersList getTenders(String offset) {
        return restTemplate.getForObject(apiUrl + "?offset={offset}", TendersList.class, offset);
    }

    public String getTender(String id) {
        return restTemplate.getForObject(apiUrl + "/{id}", String.class, id);
    }

}