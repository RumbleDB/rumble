(:JIQS: ShouldRun; Output="({ "repo" : "r1", "count" : 1 }, { "repo" : "r2", "count" : 1 })" :)
for $i in parallelize((
    { "commits" : [ { "author" : "Einstein" } ], "repo":"r2"},
    { "commits" : [ { "author" : "Goedel" }, { "author" : "Ramanujan" } ], "repo": "r1"}
))
let $k := $i.commits[].author
group by $r := $i.repo
return {"repo":$r, "count":count($k)}