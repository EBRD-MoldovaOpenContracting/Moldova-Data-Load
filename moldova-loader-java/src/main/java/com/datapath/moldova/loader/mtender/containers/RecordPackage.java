package com.datapath.moldova.loader.mtender.containers;

import lombok.Data;

import java.util.List;

@Data
public class RecordPackage {

    private String ocid;
    private List<Record> records;

}
