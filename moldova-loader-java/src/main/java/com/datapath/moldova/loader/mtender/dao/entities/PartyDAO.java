package com.datapath.moldova.loader.mtender.dao.entities;

import lombok.Data;

@Data
public class PartyDAO {

    private String name;
    private IdentifierDAO identifier;
    private AddressDAO address;

}
