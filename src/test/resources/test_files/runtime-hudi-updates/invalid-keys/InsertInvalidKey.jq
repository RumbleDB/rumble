(:JIQS: ShouldCrash; ErrorCode="RBDY0009"; UpdateDim=[4,2] :)
insert { "bad-key" : 2 } last into collection hudi-table("hoodiekeys");
