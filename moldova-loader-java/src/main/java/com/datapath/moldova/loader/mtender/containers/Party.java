package com.datapath.moldova.loader.mtender.containers;

import lombok.Data;

import java.util.List;

@Data
public class Party {

    private String id;
    private String name;
    private Identifier identifier;
    private List<String> roles;

    private Address address;

}
