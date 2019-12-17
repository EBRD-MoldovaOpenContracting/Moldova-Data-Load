function convertRelease(apiRelease) {
    let release = {};
    release.source = 'public.api.mepps.openprocurement.net';
    release.date = apiRelease.date;
    release.buyer = apiRelease.procuringEntity;
    if (!release.buyer.name) release.buyer.name = '';

    release.contracts = apiRelease.contracts ? apiRelease.contracts : [];

    if (apiRelease.awards) {
        release.awards = apiRelease.awards;
        release.awards = release.awards.map(award => {

            if (award.complaintPeriod) {
                award.contractPeriod = award.complaintPeriod;
            } else {
                award.contractPeriod = {
                    startDate: new Date(),
                    endDate: new Date()
                }
            }
            award.items = apiRelease.items;
            return award;
        });
    } else {
        release.awards = []
    }

    if (release.contracts) {
        release.contracts.forEach(contract => {
            contract.finalDate = contract.dateSigned;
            contract.contractDate = contract.dateSigned;

            if (contract.value) {
                contract.amount = contract.value.amount;
            }

            if (!contract.dateSigned) {
                contract.dateSigned = new Date();
                contract.finalDate = new Date();
                contract.contractDate = new Date();
            }

            if (!contract.period) {
                contract.period = {
                    startDate: new Date(),
                    endDate: new Date()
                }
            }

            contract.goods = {};
            if (apiRelease.items) {
                contract.goods.mdValue = apiRelease.items[0].classification.description;
            }

            if (!contract.title) {
                contract.title = '';
            }

            if (contract.suppliers) {
                contract.participant = {
                    fullName: contract.suppliers[0].name
                }
            }
        });
    }

    release.tender = apiRelease;
    release.tender.eligibilityCriteria = '';

    if (!release.tender.awardCriteria) {
        release.tender.awardCriteria = '';
    }
    if (!release.tender.tenderPeriod) {
        release.tender.tenderPeriod = {
            startDate: new Date(),
            endDate: new Date()
        }
    }
    release = iterate(release, '');
    return release;
}

function iterate(obj, stack) {
    for (var property in obj) {
        if (obj.hasOwnProperty(property)) {
            if (typeof obj[property] == "object") {
                iterate(obj[property], stack + '.' + property);
            } else {
                if (property.toLowerCase().includes("date") && isDate(obj[property])) {
                    obj[property] = new Date(obj[property]);
                }
            }
        }
    }
    return obj;
}

function isDate(date) {
    return (new Date(date) !== "Invalid Date" && !isNaN(new Date(date))) ? true : false;
}

module.exports = {
    convertRelease
}