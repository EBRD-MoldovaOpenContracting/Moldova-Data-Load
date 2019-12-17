package com.datapath.moldova.loader.mtender.containers;

import lombok.Data;

import java.util.List;

@Data
public class Award {

    private String id;
    private String description;
    private Value value;
    private String date;

    private List<Party> suppliers;

    private List<Item> items;

}
