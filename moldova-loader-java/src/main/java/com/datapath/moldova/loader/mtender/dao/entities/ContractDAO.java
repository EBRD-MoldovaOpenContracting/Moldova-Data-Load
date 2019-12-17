package com.datapath.moldova.loader.mtender.dao.entities;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.OffsetDateTime;

@Data
public class ContractDAO {

    @Field("id")
    private String id;

    private String contractNumber;
    private OffsetDateTime contractDate;
    private OffsetDateTime finalDate;
    private OffsetDateTime dateSigned;

    private PeriodDAO period;

    private ValueDAO value;

    private GoodsDAO goods;

    private String awardID;
    private AwardDAO award;

    private Double amount;

    private String title = "";
    private String status;
    private ParticipantDAO participant;
    private String source = "public.mtender.gov.md/tenders";

}