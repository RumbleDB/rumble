(:JIQS: ShouldRun; Output="({ "product" : "toaster", "store number" : 1 }, { "product" : "broiler", "store number" : 1, "quantity" : 20 }, { "product" : "broiler", "quantity" : 20 })":)
for $i in (
{ "product" : "toaster", "store number" : 1 },
{ "product" : "broiler", "store number" : 1, "quantity" : 20 },
{ "product" : "broiler", "quantity" : 20 })
order by $i."store number" descending empty least, $i."quantity" descending empty greatest
return $i

(: Missing items(empty) at different positions :)
