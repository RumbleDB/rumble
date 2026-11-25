(:JIQS: ShouldCrash; ErrorCode="cannot be stored in a Hudi table"; UpdateDim=[4,0] :)
create collection hudi-table("hoodiekeys") with { "bad-key" : 1 };
