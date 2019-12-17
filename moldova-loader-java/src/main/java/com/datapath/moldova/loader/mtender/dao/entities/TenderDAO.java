package com.datapath.moldova.loader.mtender.dao.entities;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Data
public class TenderDAO {

    @Field("id")
    private String id;
    private String title;
    private String description;
    private String status;
    private String procurementMethod;
    private String awardCriteria = "";
    private String eligibilityCriteria = "";

    private List<ItemDAO> items;

    private PeriodDAO tenderPeriod;

    private PartyDAO procuringEntity;

}
