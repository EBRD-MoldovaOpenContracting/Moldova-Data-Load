db.ocds_release.find({ "source" : "public.api.mepps.openprocurement.net" }).forEach(release => {
    if ( release.contracts) {
        let contracts = release.contracts.map(contract => {
            contract.source = "public.mtender.gov.md/tenders";
            return contract;
        })
        print(release.ocid);

        db.contracts_collection.insert(contracts);
    }
});