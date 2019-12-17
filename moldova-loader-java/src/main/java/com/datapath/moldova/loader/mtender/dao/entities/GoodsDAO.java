package com.datapath.moldova.loader.mtender.dao.entities;

import lombok.Data;

@Data
public class GoodsDAO {

    private String mdValue;
    private ClassificationDAO classification;
    private int quantity;
    private UnitDAO unit;

}
