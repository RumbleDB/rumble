(:JIQS: ShouldRun; Output="":)
import module namespace pandas = "jsoniq_pandas.jq";

pandas:fillna(json-file("../../../queries/sample-na-data.json"), {"value": 1})