package com.datapath.moldova.loader.mtender;

import com.datapath.moldova.loader.mtender.containers.CompiledRelease;
import com.datapath.moldova.loader.mtender.containers.Record;
import com.datapath.moldova.loader.mtender.containers.RecordList;
import com.datapath.moldova.loader.mtender.containers.RecordPackage;
import com.datapath.moldova.loader.mtender.dao.entities.ReleaseDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static org.springframework.util.CollectionUtils.isEmpty;

@Component
public class MTenderHandler {

    @Autowired
    private MTenderRestManager restManager;

    @Autowired
    private MTenderConverter converter;

    @Autowired
    private MongoTemplate mongoTemplate;

    public void handle() {
        String offset = "";
        RecordList recordList;

        do {
            recordList = restManager.getRecords(offset);
            if (recordList.getData() == null) break;

            for (RecordList.Record record : recordList.getData()) {
                RecordPackage recordPackage = restManager.getRecordPackage(record.getOcid());
                processRecordPackage(recordPackage);
                offset = recordList.getOffset();
            }

        } while (!isEmpty(recordList.getData()));
    }

    private void processRecordPackage(RecordPackage recordPackage) {
        if (isProcessable(recordPackage)) {
            ReleaseDAO releaseDAO = converter.convert(recordPackage);
            System.out.println(releaseDAO.getOcid());
            mongoTemplate.save(releaseDAO);

            if (!isEmpty(releaseDAO.getContracts())) {
                releaseDAO.
                        getContracts()
                        .forEach(contract -> mongoTemplate.save(contract, "contracts_collection"));
            }
        }
    }

    private boolean isProcessable(RecordPackage recordPackage) {
        Optional<CompiledRelease> compiledRelease = recordPackage.getRecords().stream()
                .map(Record::getCompiledRelease)
                .filter(release -> release.getTag().contains("compiled"))
                .findAny();

        return compiledRelease.isPresent();
    }

}
