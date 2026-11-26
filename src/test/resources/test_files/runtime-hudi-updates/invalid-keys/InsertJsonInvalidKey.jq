(:JIQS: ShouldCrash; ErrorCode="cannot be stored in a Hudi table"; UpdateDim=[4,3] :)
insert json { "bad key" : 3 } into hudi-table("hoodiekeys");
