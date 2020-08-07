# Legal Transition Programme

## Moldova: Policy and Business Advice and Support in Legislative Drafting for eProcurement Reforms

### Description:
OCDS-based visualisation tool and OCDS data feed are redeveloped to handle available data sources, data quality is assessed and data consolidation architecture is evaluated.

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

OCDS standard was created to enable the publication of information on public procurement processes in a structured, unambiguous way, ensuring data consistency and compatibility. OCDS usage is intended to help developers of analytical tools in the data integration and consolidation process.

The problem statement for this project was designed according to the requirement to redevelop the existing Moldova OCDS Contract Data Visualisation tool. The input data that should be used for redevelopment are as follows:

- [A repository on the GitHub platform of the old version of the Moldova OCDS Contract Data Visualisation tool](https://github.com/younginnovations/opencontracting-moldova) (the structure of the old version of the tool is described in Figure 1 and example of the OCDS data structure of the tool can be found in Annex 3);
- [MTender](https://public.mtender.gov.md/tenders) and [Meapps](https://public.api.mepps.openprocurement.net/api/0/tenders) OCDS Application Programming Interfaces (API) as well as their procurement data mapping rules (examples of the OCDS structure of the MTender and Meapps databases are given in Annexes 1 and 2 respectively);
- [The web-interface](https://opencontracting.eprocurement.systems/).

_Figure 1 Old structure of the Moldova OCDS Contract Data Visualisation tool_

![](https://github.com/EBRD-MoldovaOpenContracting/Moldova-Data-Load/blob/master/Figures/Figure%201%20Old%20structure%20of%20the%20Moldova%20OCDS%20Contract%20Data%20Visualisation%20tool.png)

The following list of tasks was formulated for the purpose of the assignment to redevelop the old version of the Moldova OCDS Contract Data Visualisation tool:

- Review the source code from the GitHub repository of the old version of the Moldova OCDS Contract Data Visualisation tool, which was designed by another technical team. Consider whether it can be adjusted to receive new data from the MTender and Meapps databases along with the old ones, thus operating in the multi-platform environment.
- Map the data from new data sources to the OCDS format that is used on the portal to display the correct data.
- Develop a data import module that aims to extract data from the MTender and Meapps OCDS APIs, transform it to the required OCDS format used on the web portal and further export these OCDS formatted data as a JSON or comma-separated values (CSV) file.
- Deploy a web portal prototype based on the multi-platform system to the new server.

## Technological solution and implementation
### The initial stage development issues

At the very beginning of the redevelopment process, the technical team pulled the source code from the original repository. As soon as the database (DB) was not initialised when the application starts, the technical team had to find a backup copy of the DB in the source code. Then, a number of issues were also faced that, certainly, could cause a delay in completing the assignment. The project team discovered these issues, identified ways to resolve them and fixed defined issues where possible. The list of issues, as mentioned earlier, is the following:

1. **Issue** : the source code of the portal didn&#39;t allow viewing data after the 2017 year.\
**Possible reasons** : all variables were hard coded for the period until the 2017 year and didn&#39;t display new data.\
**Actions performed to fix** : all &quot;2017&quot; values in the code were removed and updated to a dynamically calculated variable based on data for the last year in the database.

1. **Issue** : Etender import scripts failure while scraping data from the website.\
**Possible reasons** : errors in the import scripts.\
**Actions performed to fix** :added exception handlers for errors when the source formatting differs from what the scraping tool expects.

1. **Issue** : no documentation on what data is stored in the collections and used for each query on the website.\
**Possible reasons** : the documentation wasn&#39;t prepared during the development of the old version of the website.\
**Actions performed to fix** : all the necessary data from the website were manually mapped to the data from the APIs.

The technical team successfully found the scripts, fixed the issues, and initialised the DB. So, the data from the Etender was pulled to the DB.


