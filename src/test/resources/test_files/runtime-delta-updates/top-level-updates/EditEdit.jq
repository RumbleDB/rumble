(:JIQS: ShouldCrash; UpdateDim=[6,14]; Output="" :)
declare type local:t as { "key" : "integer" };
(edit table("upcollect")[8] by (validate type local:t* { { "key" : 500 }, {"key" : 501 }})[position() lt 2] from collection ,
edit table("upcollect")[8] by (validate type local:t* { { "key" : 500 }, {"key" : 501 }})[position() lt 2] from collection );