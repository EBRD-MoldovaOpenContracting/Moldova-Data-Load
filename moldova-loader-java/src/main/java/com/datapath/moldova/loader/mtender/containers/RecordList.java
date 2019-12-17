package com.datapath.moldova.loader.mtender.containers;

import lombok.Data;

import java.util.List;

@Data
public class RecordList {

    private List<Record> data;
    private String offset;

    @Data
    public static class Record {
        private String ocid;
        private String date;
    }

}