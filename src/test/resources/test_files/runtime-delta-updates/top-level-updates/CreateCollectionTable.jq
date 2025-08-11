(:JIQS: ShouldRun; UpdateDim=[6,0]; Output="" :)
declare type local:t as { "key" : "integer" };
create collection table("upcollect") with validate type local:t* { (1 to 10) ! { "key" : $$ }};