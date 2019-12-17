package com.datapath.moldova.loader.mapps.containers;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class TendersList {

    @JsonProperty("next_page")
    private Page nextPage;
    private List<TenderData> data;

    @Data
    public static class TenderData {
        private String id;
        private String date;
    }

    @Data
    public static class Page {
        private String offset;
    }

}