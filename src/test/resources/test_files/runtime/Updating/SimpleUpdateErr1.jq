(:JIQS: ShouldRun; Output="" :)
let $data := {"bool" : true, "int": 10}
return delete json $data.bool

(: attempt to modify immutable variable :)