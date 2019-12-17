(:JIQS: ShouldCrash; ErrorCode="RBST0003"; ErrorMetadata="LINE:3:COLUMN:0:" :)
for $i in json-file("./src/main/resources/queries/conf-ex.json")
let $j := for $k in json-file("./src/main/resources/queries/conf-ex.json")
          return $k.target
return $j

(: Job within job :)
