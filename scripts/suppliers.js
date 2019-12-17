
function processCompany(company) {
    db.ocds_release.find({ 'awards.suppliers.name': company.name }).forEach(release => {
        if (release.awards) {
            release.awards.forEach(award => {
                if (award.suppliers) {
                    award.suppliers.forEach(supplier => {
                        if (supplier.name === company.name) {
                            company.id = supplier.identifier.id;
                        }
                    })
                }
            })
        }
    });

    if (!company.id) {
        throw new Error(company.id);
    }
    print(company.id, '\t', company.name, '\t', company.source);
    
}

let i = 0;
db.companies_etenders.aggregate(
    [
        {
            $group: {
                _id: "$cleanname",
                name: { "$first": "$name" },
            }
        }
    ]
).forEach(company => {
    processCompany(company);
})
