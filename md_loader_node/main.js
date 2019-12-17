const axios = require('axios');
const converter = require('./release-converter');

const MongoClient = require('mongodb').MongoClient;
const assert = require('assert');

const mongoUrl = 'mongodb://localhost:27017';
const mongoDatabaseName = 'md';

const client = new MongoClient(mongoUrl);

processTenders();
client.close();

async function processTenders() {
    let dataLength = 0;
    let lastOffset = '';
    do {
        await axios.get('https://public.api.mepps.openprocurement.net/api/0/tenders?offset=' + lastOffset)
            .then(response => {
                let tenders = response.data.data;
                dataLength = tenders.length;
                lastOffset = response.data.next_page.offset;
                return response.data;
            })
            .then(handleTendersResponse)
            .catch(err => console.log(err));
    } while (dataLength > 1);
}

function handleTendersResponse(response) {
    let tenders = response.data;

    client.connect(function (err) {
        assert.equal(null, err);
        const db = client.db(mongoDatabaseName);
        const releaseCollection = db.collection('ocds_release');
        const contractCollection = db.collection('contracts_collection');
        for (const tender of tenders) {
            getTender(tender.id).then(response => {
                let apiRelease = response.data.data;
                let release = converter.convertRelease(apiRelease);
                releaseCollection.insertOne(release, (err) => {
                    if (err) console.log(err);
                })

                if (release.contracts) {
                    release.contracts.forEach(contract => {
                        contract.source = release.source;
                        contractCollection.insertOne(contract, (err) => {
                            if (err) console.log(err);
                        })
                    });
                }
                console.log(apiRelease.id);

            }).catch(err => console.log(err));
        }
    });
}

async function getTender(id) {
    return await axios.get('https://public.api.mepps.openprocurement.net/api/0/tenders/' + id);
}