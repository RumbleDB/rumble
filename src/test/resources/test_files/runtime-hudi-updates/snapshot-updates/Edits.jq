(:JIQS: ShouldCrash; ErrorCode="JNUP0012"; UpdateDim=[2,9]; ErrorMetadata="LINE:2:COLUMN:0:" :)
edit hudi-table("hoodie")[1] into { "index" : 100 } in collection,
edit hudi-table("hoodie")[1] into { "index" : 1 } in collection,
edit hudi-table("hoodie")[1] into { "index" : 1000 } in collection,
edit hudi-table("hoodie")[1] into { "index" : 0 } in collection