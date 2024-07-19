(:JIQS: ShouldRun; Output="":)
import module namespace pandas = "jsoniq_pandas.jq";

let $file_data := structured-json-file("../../../queries/sample-na-data-2.json")
return $file_data=>pandas:fillna({"value": null})