(:JIQS: ShouldRun; UpdateDim=[5,0]; Output="" :)
create collection hudi-table("hoodiemeta") with (
    { "id" : 1, "info" : { "v" : "a" } },
    { "id" : 2, "info" : { "v" : "b" } },
    { "id" : 3, "info" : { "v" : "c" } }
);
