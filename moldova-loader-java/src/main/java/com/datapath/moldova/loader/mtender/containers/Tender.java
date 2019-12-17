package com.datapath.moldova.loader.mtender.containers;

import lombok.Data;

import java.util.List;

@Data
public class Tender {

    private String id;
    private String title;
    private String description;
    private String status;
    private String procurementMethod;
    private String awardCriteria;

    private List<Item> items;

    private Period tenderPeriod;


}
