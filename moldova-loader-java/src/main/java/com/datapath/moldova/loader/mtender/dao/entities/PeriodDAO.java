package com.datapath.moldova.loader.mtender.dao.entities;

import lombok.Data;

import java.time.OffsetDateTime;

@Data
public class PeriodDAO {

    private OffsetDateTime startDate;
    private OffsetDateTime endDate;
}