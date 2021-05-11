(:JIQS: ShouldRun; Output="(false, false, false)" :)
declare type local:x as { "foo" : "integer" };
declare type local:y as { "foo" : [ "integer" ] };
declare type local:z as { "!foo" : [ "integer" ] };
(2+2) instance of local:x,
[] instance of local:y,
{ "foo" : "bar" } instance of local:z
