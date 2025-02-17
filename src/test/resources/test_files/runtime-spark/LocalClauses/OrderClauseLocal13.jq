(:JIQS: ShouldRun; Output="({ "product" : "broiler", "store number" : 1, "quantity" : 20 }, { "product" : "toaster", "store number" : 1 })":)
for $i in (
   { "product" : "broiler", "store number" : 1, "quantity" : 20  },
   { "product" : "toaster", "store number" : 1  })
order by $i."store number", $i."quantity" empty greatest
return $i

(: empty field greatest  - expression with empty field given last :)
