(:JIQS: ShouldCrash; ErrorCode="RBDY0009"; UpdateDim=[4,3] :)
insert json { "bad key" : 3 } into hudi-table("hoodiekeys");
