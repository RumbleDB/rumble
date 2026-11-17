(:JIQS: ShouldRun; Output="{ "int" : 43 }" :)
declare type local:a as { "int" : "integer=43" };

exactly-one(validate type local:a* { { } })
