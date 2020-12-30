# Legal Transition Programme

## Moldova: Policy and Business Advice and Support in Legislative Drafting for eProcurement Reforms

### Description:
[OCDS](https://standard.open-contracting.org/latest/en/)-based visualisation tool and [OCDS](https://standard.open-contracting.org/latest/en/) data feed are redeveloped to handle available data sources, data quality is assessed and data consolidation architecture is evaluated.

### Project timeline:

2019-2020

### Project status:
Fully deployed

## Problem statement
The value and reliability of knowledge obtained as a result of data analysis depend not only on the effectiveness of the analytical methods and algorithms used but also on how properly the initial data for analysis are selected and prepared. In the real world, there are a lot of issues that are to be faced during data selection and preparation. The most common issues, as well as influencing the development process, are the following:

- More than one data source (multi-platform systems);
- Data redundancy and/or insufficiency;
- &quot;Dirty data&quot; which includes but is not limited to omissions, abnormal values, duplicates and contradictions.

Therefore, before proceeding with the development of the analytical tools, it is necessary to perform a number of procedures in order to identify all the possible bottlenecks and be aware of the quality, integrity and consistency of data as well as to identify what causes the data quality problems and make a decision on how to eliminate those issues.

[OCDS](https://standard.open-contracting.org/latest/en/) standard was created to enable the publication of information on public procurement processes in a structured, unambiguous way, ensuring data consistency and compatibility. [OCDS](https://standard.open-contracting.org/latest/en/) usage is intended to help developers of analytical tools in the data integration and consolidation process.

The problem statement for this project was designed according to the requirement to redevelop the existing Moldova OCDS Contract Data Visualisation tool. The input data that should be used for redevelopment are as follows:

- [A repository on the GitHub platform of the old version of the Moldova OCDS Contract Data Visualisation tool](https://github.com/younginnovations/opencontracting-moldova) (the structure of the old version of the tool is described in Figure 1 and for the example of the [OCDS](https://standard.open-contracting.org/latest/en/) data structure of the tool see ["OCDS data structure of the Moldova OCDS Contract Data Visualisation tool"](https://github.com/EBRD-MoldovaOpenContracting/Moldova-Data-Load/tree/master/OCDS%20structures/OCDS%20data%20structure%20of%20the%20Moldova%20OCDS%20Contract%20Data%20Visualisation%20tool));
- [DB-1](https://public.mtender.gov.md/tenders) and [DB-2](https://public.api.mepps.openprocurement.net/api/0/tenders) OCDS Application Programming Interfaces (API) as well as their procurement data mapping rules (for the examples of the [OCDS](https://standard.open-contracting.org/latest/en/) structure of the [DB-1](https://public.mtender.gov.md/tenders) and [DB-2](https://public.api.mepps.openprocurement.net/api/0/tenders) see ["OCDS data structure of the DB-1 API"](https://github.com/EBRD-MoldovaOpenContracting/Moldova-Data-Load/tree/master/OCDS%20structures/OCDS%20data%20structure%20of%20the%20MTender%20system) and ["OCDS data structure of the DB-2 API"](https://github.com/EBRD-MoldovaOpenContracting/Moldova-Data-Load/tree/master/OCDS%20structures/OCDS%20data%20structure%20of%20the%20Meapps%20system) respectively);
- [The web-interface](https://opencontracting.eprocurement.systems/).

_Figure 1 Old structure of the Moldova OCDS Contract Data Visualisation tool_

![](https://github.com/EBRD-MoldovaOpenContracting/Moldova-Data-Load/blob/master/Figures/Figure%201%20Old%20structure%20of%20the%20Moldova%20OCDS%20Contract%20Data%20Visualisation%20tool.png)

The following list of tasks was formulated for the purpose of the assignment to redevelop the old version of the Moldova OCDS Contract Data Visualisation tool:

- Review the source code from the [GitHub repository](https://github.com/younginnovations/opencontracting-moldova) of the old version of the Moldova OCDS Contract Data Visualisation tool, which was designed by another technical team. Consider whether it can be adjusted to receive new data from the [DB-1](https://public.mtender.gov.md/tenders) and [DB-2](https://public.api.mepps.openprocurement.net/api/0/tenders) databases along with the old ones, thus operating in the multi-platform environment.
- Map the data from new data sources to the [OCDS](https://standard.open-contracting.org/latest/en/) format that is used on the portal to display the correct data.
- Develop a data import module that aims to extract data from the [DB-1](https://public.mtender.gov.md/tenders) and [DB-2](https://public.api.mepps.openprocurement.net/api/0/tenders) OCDS APIs, transform it to the required [OCDS](https://standard.open-contracting.org/latest/en/) format used on the web portal and further export these [OCDS](https://standard.open-contracting.org/latest/en/) formatted data as a JSON or comma-separated values (CSV) file.
- Deploy a web portal prototype based on the multi-platform system to the new server.

## Technological solution and implementation
### The initial stage development issues

At the very beginning of the redevelopment process, the technical team pulled the source code from the original repository. As soon as the database (DB) was not initialised when the application starts, the technical team had to find a backup copy of the DB in the source code. Then, a number of issues were also faced that, certainly, could cause a delay in completing the assignment. The project team discovered these issues, identified ways to resolve them and fixed defined issues where possible. The list of issues, as mentioned earlier, is the following:

1. **Issue** : the source code of the portal didn&#39;t allow viewing data after the 2017 year.\
**Possible reasons** : all variables were hard coded for the period until the 2017 year and didn&#39;t display new data.\
**Actions performed to fix** : all &quot;2017&quot; values in the code were removed and updated to a dynamically calculated variable based on data for the last year in the database.

1. **Issue** : Etender import scripts failure while scraping data from the [website](https://opencontracting.eprocurement.systems/).\
**Possible reasons** : errors in the import scripts.\
**Actions performed to fix** :added exception handlers for errors when the source formatting differs from what the scraping tool expects.

1. **Issue** : no documentation on what data is stored in the collections and used for each query on the [website](https://opencontracting.eprocurement.systems/).\
**Possible reasons** : the documentation wasn&#39;t prepared during the development of the old version of the [website](https://opencontracting.eprocurement.systems/).\
**Actions performed to fix** : all the necessary data from the website were manually mapped to the data from the APIs.

The technical team successfully found the scripts, fixed the issues, and initialised the DB. So, the data from the Etender was pulled to the DB.

### Redevelopment steps
Several steps were performed in order to redevelop the existing old version of the Moldova OCDS Contract Data Visualisation tool. The list below summarises all the steps mentioned above. 
- Development of the Import-Module to grep  data from the [DB-1](https://public.mtender.gov.md/tenders) and [DB-2](https://public.api.mepps.openprocurement.net/api/0/tenders) APIs;
- Development of the fetched data grouping module to remove duplicate data;
- Creation of the adjusted DB (MongoDB), with collections from different sources;
- Adding dynamic variables to take the last year in the database;
- Redevelopment of the visual part of the [website](https://opencontracting.eprocurement.systems).

## Results and further steps
The differences between the systems and approaches to the publication of data in the [OCDS](https://standard.open-contracting.org/latest/en/) format led to our decision to consolidate the multi-platform system data at the database level. We created a dedicated [OCDS](https://standard.open-contracting.org/latest/en/) database using the MongoDB database and organised the storage of all data in the special collections to consolidate the multi-platform system data. We consider this approach the most suitable in similar situations.

For future development, when the amount of data will have increased, the queries for the tables should be updated. The current request is time-consuming and needs to be updated, as well as a pagination method, which can sometimes cause the freezing due to a memory leak.

Also, it is better to have similar public procurement data within one data source to avoid data intersection, duplication and ambiguous data.

Three issues were determined among the key information on tenderers during the data quality evaluation:

- Invalid or missing identifier for a tenderer;
- Invalid or missing legal name for a tenderer;
- Invalid or missing country of registration for a tenderer.

The main reason for all the issues is wrong, unverified data input. We recommend the following steps to eliminate the issues above:

- **Add validation to existing input systems available on platforms**: check out the number of spaces entered, the format of identifiers, empty input, etc.;
- **Integrate available official registers and catalogues with the existing input systems**: it means that the tenderer can be authorised in the system only with the identifier and legal name contained in the official register and a drop-down selection menu provided to enable the selection of the proper values from the official catalogues.

Another issue was that the **registry of CAs data isn&#39;t publicly available**. To resolve this issue, we recommend that all official registers and catalogues are made available for public use.

## Leasons learnt
Data standardisation is an undeniable way to improve the interaction between people, understanding the processes and analysing them anywhere in the world. The public procurement sphere isn&#39;t an exception. The [OCDS](https://standard.open-contracting.org/latest/en/) standard is presented by the [Open Contracting Partnership](https://www.open-contracting.org/) and aims to unify public procurement processes worldwide. This can lead to better transparency, efficiency, the effectiveness of public procurement procedures and increase the international trading potential.

During the redevelopment of the tool we worked with the scrapped data from the [website](https://opencontracting.eprocurement.systems) and two [OCDS](https://standard.open-contracting.org/latest/en/) databases and had to transform this data into another OCDS structure available for the web tool. Having two OCDS databases and data that has already been mapped to the web tool OCDS structure, it seems there is no problem converting this data to another OCDS format. Unfortunately, we discovered a lot of bottlenecks while working with published data in the OCDS format.

The [OCDS](https://standard.open-contracting.org/latest/en/) standard allows different structures for the data that is to be published. It is very convenient for depicting local public procurement processes, but it leads to difficulties in operating with, and understanding, the published data and its structure. It also leads to the technical issues, for example: when it is necessary to get data from the release packages of one data source and record packages of another data source, we cannot use the same import scripts, and they have to be adjusted for each data source.

Another difficulty is associated with the semantic meaning that each variable is endowed with – each publisher has to provide a unified semantic meaning for each variable published. Also, we encountered the issue with a different process representation: in one system contracting process ends on the contracting stage, while in another system, it ends up on the awarding stage.

All these issues should be addressed and resolved to avoid issues with data consolidation and integration in the future.

In addition, it is very important to provide analytical tools, analysts, government organisations, Non-Governmental Organisations and other stakeholders interested in public procurement with the data of high quality. As a result of data preparation, processing and cleansing, as well as data quality assessment, the data quality issues were detected and investigated. The reasons for data quality issues occurrence were determined and addressed, and the possible impact of data quality issues on analytics was defined. Our analytical team developed recommendations for the elimination of the issues identified. Moreover, for a better understanding and interpretation of the data quality analysis results, a dedicated [BI tool](https://github.com/EBRD-MoldovaOpenContracting/Moldova-Data-Load/wiki/Dedicated-BI-tool-for-data-quality-assessment) was developed.
