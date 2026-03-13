(:JIQS: ShouldRun; Output="(17, 36, 8, 28, 24)" :)
declare type local:item as {
  "l_quantity" : "integer",
  "l_returnflag" : "string"
};

let $df as local:item* := validate type local:item* {
    {
        "l_quantity": 17,
        "l_returnflag": "N"
    },
    {
        "l_quantity": 36,
        "l_returnflag": "N"
    },
    {
        "l_quantity": 8,
        "l_returnflag": "N"
    },
    {
        "l_quantity": 28,
        "l_returnflag": "N"
    },
    {
        "l_quantity": 24,
        "l_returnflag": "N"
    },
    {
        "l_quantity": 32,
        "l_returnflag": "N"
    }
}

for $i in 1 to 5
let $l := $df[$i]
let $returnflag := $l.l_returnflag
group by $returnflag
return $l.l_quantity