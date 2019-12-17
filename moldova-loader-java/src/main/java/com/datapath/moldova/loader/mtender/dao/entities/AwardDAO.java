package com.datapath.moldova.loader.mtender.dao.entities;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Data
public class AwardDAO {

    @Field("id")
    private String id;
    private String description;
    private ValueDAO value;

    private PeriodDAO contractPeriod;

    private List<PartyDAO> suppliers;

    private List<ItemDAO> items;

}
