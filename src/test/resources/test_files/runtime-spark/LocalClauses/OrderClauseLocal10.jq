(:JIQS: ShouldRun; Output="({ "product" : "toaster", "store number" : 1 }, { "product" : "broiler", "store number" : 1, "quantity" : 20 })":)
for $i in (
   { "product" : "toaster", "store number" : 1  },
   { "product" : "broiler", "store number" : 1, "quantity" : 20  })
order by $i."store number", $i."quantity" empty least
return $i

(: explicit empty field least:)
