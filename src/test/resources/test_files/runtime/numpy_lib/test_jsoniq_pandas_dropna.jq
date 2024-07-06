(:JIQS: ShouldRun; Output="":)
import module namespace pandas = "jsoniq_pandas.jq";

pandas:dropna(json-file("../../../queries/sample-na-data.json"), {"axis": 1, "how": "any"})