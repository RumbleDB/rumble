(:JIQS: ShouldRun; Output="":)

import module namespace pandas = "jsoniq_pandas.jq";
pandas:sample(json-file("../../../queries/sample-na-data.json"), 4)