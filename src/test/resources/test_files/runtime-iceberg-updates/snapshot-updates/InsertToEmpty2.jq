(:JIQS: ShouldRun; UpdateDim=[9,3]; Output="({ "FIRST" : 0.1 }, { "LAST" : 10 })" :)
(insert { "LAST" : 10 } last into collection iceberg-table("icyEmpty"),
insert { "FIRST" : 0.1 } first into collection iceberg-table("icyEmpty"));
iceberg-table("icyEmpty")