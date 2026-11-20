(:JIQS: ShouldRun; UpdateDim=[2,6]; Output="({ "FIRSTTTT" : "at the very beginning" }, { "before_1" : "right BEFORE row 1" }, { "should_be_first" : 9789 }, { "after_1" : "right after row 1" }, { "before_2" : "should be before row 2" }, { "index" : 1 }, { "after_2" : "should be AFTER row 2" }, { "before_3" : "should be before row 3" }, { "latest" : "laster" }, { "after_3" : "test after row 3" }, { "LASTTTT" : "does it work??" })" :)
(insert {"before_2": "should be before row 2"} before hudi-table("hoodie")[2] into collection,
insert {"after_1": "right after row 1"} after hudi-table("hoodie")[1] into collection,
insert {"after_3": "test after row 3"} after hudi-table("hoodie")[3] into collection,
insert {"before_1": "right BEFORE row 1"} before hudi-table("hoodie")[1] into collection,
insert {"after_2": "should be AFTER row 2"} after hudi-table("hoodie")[2] into collection,
insert {"before_3": "should be before row 3"} before hudi-table("hoodie")[3] into collection,
insert {"LASTTTT": "does it work??"} last into collection hudi-table("hoodie"),
insert {"FIRSTTTT": "at the very beginning"} first into collection hudi-table("hoodie"));
hudi-table("hoodie")
