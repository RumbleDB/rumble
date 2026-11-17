(:JIQS: ShouldRun; Output="{ "repo" : 1, "count" : 3 }" :)
for $i in parallelize((
    { "commits" : [ { "author" : "Einstein" } ]},
    { "commits" : [ { "author" : "Goedel" }, { "author" : "Ramanujan" } ]}
  ))
let $k := $i.commits[].author
group by $r := 1
return {"repo":$r, "count":count($k)}