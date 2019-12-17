package com.datapath.moldova.loader.mtender.containers;

import lombok.Data;

@Data
public class Item {

    private String id;
    private String description;
    private Classification classification;
    private Double quantity;
    private Unit unit;

}