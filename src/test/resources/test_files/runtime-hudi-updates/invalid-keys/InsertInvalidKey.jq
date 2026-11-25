(:JIQS: ShouldCrash; ErrorCode="cannot be stored in a Hudi table"; UpdateDim=[4,2] :)
insert { "bad-key" : 2 } last into collection hudi-table("hoodiekeys");
