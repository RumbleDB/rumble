(:JIQS: ShouldCrash; ErrorCode="is not a Hudi table"; UpdateDim=[6,1] :)
insert { "id" : 2 } last into collection hudi-table("nothudi");
