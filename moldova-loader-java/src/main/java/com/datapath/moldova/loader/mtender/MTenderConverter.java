package com.datapath.moldova.loader.mtender;

import com.datapath.moldova.loader.mtender.containers.*;
import com.datapath.moldova.loader.mtender.dao.entities.*;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static org.springframework.util.CollectionUtils.containsAny;
import static org.springframework.util.CollectionUtils.isEmpty;

@Component
public class MTenderConverter {

    public ReleaseDAO convert(RecordPackage recordPackage) {

        Optional<CompiledRelease> compiledRelease = recordPackage.getRecords().stream()
                .map(Record::getCompiledRelease)
                .filter(release -> release.getTag().contains("compiled"))
                .findAny();

        List<Award> apiAwards = recordPackage.getRecords().stream()
                .map(Record::getCompiledRelease)
                .filter(release -> !isEmpty(release.getAwards()))
                .flatMap(release -> release.getAwards().stream())
                .collect(toList());

        CompiledRelease releaseApi = compiledRelease.get();
        ReleaseDAO releaseDAO = getRelease(releaseApi);
        PartyDAO procuringEntityDAO = getProcuringEntity(releaseApi);

        TenderDAO tenderDAO = getTender(releaseApi);
        tenderDAO.setProcuringEntity(procuringEntityDAO);
        releaseDAO.setTender(tenderDAO);


        List<AwardDAO> daoAwards = getAwards(apiAwards);
        releaseDAO.setAwards(daoAwards);


        List<ContractDAO> daoContracts = getContracts(recordPackage);

        daoContracts.forEach(contract -> {

            Optional<Award> awardApi = apiAwards.stream()
                    .filter(awardDAO -> awardDAO.getId().equalsIgnoreCase(contract.getAwardID()))
                    .findFirst();
            awardApi.ifPresent(award -> {
                contract.setAward(getAward(award));
                if (award.getValue() != null) {
                    ValueDAO valueDAO = new ValueDAO();
                    valueDAO.setAmount(Double.parseDouble(award.getValue().getAmount()));
                    contract.setValue(valueDAO);
                    contract.setAmount(Double.parseDouble(award.getValue().getAmount()));
                } else {
                    ValueDAO valueDAO = new ValueDAO();
                    valueDAO.setAmount(0.0);
                    contract.setValue(valueDAO);
                    contract.setAmount(0.0);
                }

                if (!isEmpty(award.getItems())) {
                    String description = award.getItems().get(0).getClassification().getDescription();
                    contract.getGoods().setMdValue(description);
                }

                ParticipantDAO participantDAO = new ParticipantDAO();
                if (!isEmpty(award.getSuppliers())) {
                    participantDAO.setFullName(award.getSuppliers().get(0).getName());
                }
                contract.setParticipant(participantDAO);

            });

            if (!awardApi.isPresent()) {
                ValueDAO valueDAO = new ValueDAO();
                valueDAO.setAmount(0.0);
                contract.setValue(valueDAO);
                contract.setAmount(0.0);
                contract.setDateSigned(OffsetDateTime.now());
            }
        });


        releaseDAO.setContracts(daoContracts);

        releaseDAO.setBuyer(procuringEntityDAO);

        return releaseDAO;
    }

    private List<ContractDAO> getContracts(RecordPackage recordPackage) {
        Stream<Contract> apiContracts = recordPackage.getRecords().stream()
                .map(Record::getCompiledRelease)
                .filter(release -> containsAny(release.getTag(), asList("award", "awardUpdate")))
                .filter(release -> !isEmpty(release.getContracts()))
                .flatMap(release -> release.getContracts().stream());

        return apiContracts.map(this::getContract).collect(toList());

    }

    private ContractDAO getContract(Contract contractApi) {
        ContractDAO contractDAO = new ContractDAO();

        contractDAO.setId(contractApi.getId());
        contractDAO.setContractNumber(contractApi.getId());
        contractDAO.setStatus(contractApi.getStatus());
        if (contractApi.getDate() != null) {
            contractDAO.setContractDate(OffsetDateTime.parse(contractApi.getDate()));
            contractDAO.setFinalDate(OffsetDateTime.parse(contractApi.getDate()));
            contractDAO.setDateSigned(OffsetDateTime.parse(contractApi.getDate()));

            PeriodDAO periodDAO = new PeriodDAO();
            periodDAO.setStartDate(OffsetDateTime.parse(contractApi.getDate()));
            periodDAO.setEndDate(OffsetDateTime.parse(contractApi.getDate()));
            contractDAO.setPeriod(periodDAO);
        } else {
            contractDAO.setContractDate(OffsetDateTime.now());
            contractDAO.setFinalDate(OffsetDateTime.now());
            contractDAO.setDateSigned(OffsetDateTime.now());
            PeriodDAO periodDAO = new PeriodDAO();
            periodDAO.setStartDate(OffsetDateTime.now());
            periodDAO.setEndDate(OffsetDateTime.now());
            contractDAO.setPeriod(periodDAO);
        }

        contractDAO.setAwardID(contractApi.getAwardId());

        GoodsDAO goodsDAO = new GoodsDAO();
        goodsDAO.setMdValue("Diverse produse alimentare");
        ClassificationDAO classification = new ClassificationDAO();
        classification.setId("00000000-0");
        classification.setDescription("Diverse produse alimentare");
        goodsDAO.setClassification(classification);

        UnitDAO unitDAO = new UnitDAO("");
        goodsDAO.setUnit(unitDAO);
        contractDAO.setGoods(goodsDAO);

        return contractDAO;
    }


    private List<AwardDAO> getAwards(List<Award> apiAwards) {
        if (isEmpty(apiAwards)) return new ArrayList<>();

        return apiAwards.stream().map(this::getAward).collect(toList());
    }

    private AwardDAO getAward(Award awardApi) {
        AwardDAO awardDAO = new AwardDAO();
        awardDAO.setId(awardApi.getId());
        awardDAO.setDescription(awardApi.getDescription());

        Value valueApi = awardApi.getValue();
        if (valueApi != null) {
            ValueDAO valueDAO = new ValueDAO();
            valueDAO.setAmount(Double.parseDouble(valueApi.getAmount()));
            valueDAO.setCurrency(valueApi.getCurrency());
            awardDAO.setValue(valueDAO);
        }

        if (awardApi.getDate() != null) {
            PeriodDAO contractPeriod = new PeriodDAO();
            contractPeriod.setStartDate(OffsetDateTime.parse(awardApi.getDate()));
            awardDAO.setContractPeriod(contractPeriod);
        }

        List<PartyDAO> daoSuppliers = getSuppliers(awardApi.getSuppliers());
        awardDAO.setSuppliers(daoSuppliers);

        List<ItemDAO> daoItems = getItems(awardApi.getItems());
        awardDAO.setItems(daoItems);

        return awardDAO;
    }

    private List<PartyDAO> getSuppliers(List<Party> apiSuppliers) {
        if (isEmpty(apiSuppliers)) return null;

        return apiSuppliers.stream().map(this::getSupplier).collect(toList());
    }

    private PartyDAO getSupplier(Party supplierApi) {
        PartyDAO supplierDao = new PartyDAO();
        supplierDao.setName(supplierApi.getName());

        IdentifierDAO identifierDAO = new IdentifierDAO();
        identifierDAO.setId(supplierApi.getId());

        supplierDao.setIdentifier(identifierDAO);

        return supplierDao;
    }

    private PartyDAO getProcuringEntity(CompiledRelease releaseApi) {
        if (isEmpty(releaseApi.getParties())) return null;

        Optional<Party> procuringEntityApi = releaseApi.getParties()
                .stream()
                .filter(party -> !isEmpty(party.getRoles()))
                .filter(party -> party.getRoles().contains("procuringEntity"))
                .findAny();

        return procuringEntityApi.map(this::getParty).orElse(null);
    }

    private PartyDAO getParty(Party partyApi) {
        PartyDAO partyDAO = new PartyDAO();
        partyDAO.setName(partyApi.getName());

        IdentifierDAO identifierDAO = new IdentifierDAO();
        identifierDAO.setId(partyApi.getIdentifier().getId());
        identifierDAO.setScheme(partyApi.getIdentifier().getScheme());
        identifierDAO.setLegalName(partyApi.getIdentifier().getLegalName());

        Address addressApi = partyApi.getAddress();
        if (addressApi != null) {
            AddressDAO addressDAO = new AddressDAO();
            addressDAO.setStreetAddress(addressApi.getStreetAddress());

            AddressDetails addressDetailsApi = addressApi.getAddressDetails();
            if (addressDetailsApi != null) {
                Locality localityApi = addressDetailsApi.getLocality();
                if (localityApi != null) {
                    addressDAO.setLocality(localityApi.getDescription());
                }
            }

            partyDAO.setAddress(addressDAO);
        }

        partyDAO.setIdentifier(identifierDAO);

        return partyDAO;
    }

    private TenderDAO getTender(CompiledRelease releaseApi) {
        Tender tenderApi = releaseApi.getTender();
        TenderDAO tenderDAO = new TenderDAO();
        tenderDAO.setId(tenderApi.getId());
        tenderDAO.setTitle(tenderApi.getTitle() != null ? tenderApi.getTitle() : "");
        tenderDAO.setDescription(tenderApi.getDescription());
        tenderDAO.setStatus(tenderApi.getStatus());
        tenderDAO.setProcurementMethod(tenderApi.getProcurementMethod());
        if (tenderApi.getAwardCriteria() != null) {
            tenderDAO.setAwardCriteria(tenderApi.getAwardCriteria());
        }

        List<ItemDAO> daoItems = getItems(tenderApi.getItems());
        tenderDAO.setItems(daoItems);

        if (tenderApi.getTenderPeriod() != null) {
            PeriodDAO periodDAO = new PeriodDAO();
            if (tenderApi.getTenderPeriod().getStartDate() != null) {
                periodDAO.setStartDate(OffsetDateTime.parse(tenderApi.getTenderPeriod().getStartDate()));
            } else {
                periodDAO.setStartDate(OffsetDateTime.parse(tenderApi.getTenderPeriod().getStartDate()));
            }

            if (tenderApi.getTenderPeriod().getEndDate() != null) {
                periodDAO.setEndDate(OffsetDateTime.parse(tenderApi.getTenderPeriod().getEndDate()));
            } else {
                periodDAO.setEndDate(OffsetDateTime.parse(releaseApi.getDate()));
            }

            tenderDAO.setTenderPeriod(periodDAO);
        } else {
            PeriodDAO periodDAO = new PeriodDAO();
            periodDAO.setStartDate(OffsetDateTime.parse(releaseApi.getDate()));
            periodDAO.setEndDate(OffsetDateTime.parse(releaseApi.getDate()));
            tenderDAO.setTenderPeriod(periodDAO);
        }

        return tenderDAO;
    }

    private List<ItemDAO> getItems(List<Item> apiItems) {
        if (isEmpty(apiItems)) {
            ItemDAO itemDAO = new ItemDAO();
            itemDAO.setQuantity(0.0);
            ClassificationDAO classification = new ClassificationDAO();
            classification.setId("00000000-0");
            classification.setScheme("CPV");
            classification.setDescription("Diverse produse alimentare");
            itemDAO.setClassification(classification);

            itemDAO.setUnit(new UnitDAO(""));

            return Collections.singletonList(itemDAO);
        }
        return apiItems.stream().map(this::getItem).collect(toList());
    }

    private ItemDAO getItem(Item apiItem) {
        ItemDAO daoItem = new ItemDAO();

        daoItem.setId(apiItem.getId());
        daoItem.setDescription(apiItem.getDescription());

        if (apiItem.getClassification() != null) {
            ClassificationDAO classificationDAO = new ClassificationDAO();
            classificationDAO.setId(apiItem.getClassification().getId());
            classificationDAO.setScheme(apiItem.getClassification().getScheme());
            classificationDAO.setDescription(apiItem.getClassification().getDescription());

            daoItem.setClassification(classificationDAO);
        }


        daoItem.setQuantity(apiItem.getQuantity() != null ? apiItem.getQuantity() : 0);

        if (apiItem.getUnit() != null) {
            UnitDAO unitDao = new UnitDAO(apiItem.getUnit().getName());
            daoItem.setUnit(unitDao);
        } else {
            daoItem.setUnit(new UnitDAO(""));
        }

        return daoItem;
    }

    private ReleaseDAO getRelease(CompiledRelease releaseApi) {
        ReleaseDAO releaseDAO = new ReleaseDAO();
        releaseDAO.setId(releaseApi.getId());
        releaseDAO.setOcid(releaseApi.getOcid());
        releaseDAO.setDate(OffsetDateTime.parse(releaseApi.getDate()));
        releaseDAO.setTag(releaseApi.getTag());
        releaseDAO.setInitiationType(releaseApi.getInitiationType());
        return releaseDAO;
    }

}
