(:JIQS: ShouldRun; Output="{ "int": 10 }" :)
let $data := {"bool" : true, "int": 10}
return delete json $data.bool

(: attempt to modify immutable variable :)