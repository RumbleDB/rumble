(:JIQS: ShouldRun; Output="":)
import module namespace pandas = "jsoniq_pandas.jq";

let $data := structured-json-file("../../../queries/sample-na-data-2.json")
return $data=>pandas:dropna({"axis": 0, "how": "any"})