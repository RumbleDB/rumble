(:JIQS: ShouldRun; Output="(false, true, false)" :)
(() and 3 and 3.12 and 3e4 and false and null) is statically boolean, ("qwe" or "" or (12 treat as integer?) or ( [1, 2], {"a" : "b"} )) is statically boolean, (not [1, 4]) is statically boolean

(: arrays and objects, along with sequences where first item is array or object had effective boolean value true in jsoniq1.0 :)