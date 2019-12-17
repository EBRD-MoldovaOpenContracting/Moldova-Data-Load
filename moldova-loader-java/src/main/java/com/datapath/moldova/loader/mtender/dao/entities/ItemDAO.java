package com.datapath.moldova.loader.mtender.dao.entities;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
public class ItemDAO {

    @Field("id")
    private String id;
    private String description;
    private ClassificationDAO classification;
    private Double quantity;

    private UnitDAO unit;

}