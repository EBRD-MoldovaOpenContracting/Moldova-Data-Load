package com.datapath.moldova.loader.mtender.containers;

import lombok.Data;

import java.util.List;

@Data
public class CompiledRelease {

    private String id;
    private String ocid;
    private String date;
    private List<String> tag;
    private String initiationType;

    private Tender tender;

    private List<Party> parties;
    private List<Award> awards;
    private List<Contract> contracts;

}