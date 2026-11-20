(:JIQS: ShouldCrash; ErrorCode="JNUP0008";  UpdateDim=[2,2]; ErrorMetadata="LINE:2:COLUMN:0:" :)
insert { "BEFORE" : "works" } before hudi-table("hoodieEmpty")[1] into collection,
insert { "AFTER" : "well" } after hudi-table("hoodieEmpty")[1] into collection