(:JIQS: ShouldParse :)
for $store in [
    { "store number" : 1, "state" : "MA" },
{ "store number" : 2, "state" : "MA" },
{ "store number" : 3, "state" : "CA" },
{ "store number" : 4, "state" : "CA" }
][]
where $store."state" eq "MA"
return $store
