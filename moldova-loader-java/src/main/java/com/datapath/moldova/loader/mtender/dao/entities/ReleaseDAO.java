package com.datapath.moldova.loader.mtender.dao.entities;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.OffsetDateTime;
import java.util.List;

@Document(collection = "ocds_release")
@Data
public class ReleaseDAO {

    private String id;
    private String ocid;
    private OffsetDateTime date;
    private List<String> tag;
    private String initiationType;
    private TenderDAO tender;
    private PartyDAO buyer;
    private String source = "public.mtender.gov.md/tenders";

    private List<AwardDAO> awards;
    private List<ContractDAO> contracts;

}
