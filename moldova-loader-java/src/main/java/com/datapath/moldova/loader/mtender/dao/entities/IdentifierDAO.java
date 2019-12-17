package com.datapath.moldova.loader.mtender.dao.entities;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
public class IdentifierDAO {

    @Field("id")
    private String id;
    private String scheme;
    private String legalName;

}
