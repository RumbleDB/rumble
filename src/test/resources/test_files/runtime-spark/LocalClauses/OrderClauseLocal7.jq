(:JIQS: ShouldRun; Output="({ "product" : "toaster", "store number" : 2 }, { "product" : "toaster2", "store number2" : 2 }, { "product" : "broiler", "store number" : 1, "quantity" : 30 }, { "product" : "broiler", "store number" : 1, "quantity" : 20 })":)
for $i in (
   { "product" : "broiler", "store number" : 1, "quantity" : 20  },
   { "product" : "broiler", "store number" : 1, "quantity" : 30  },
   { "product" : "toaster", "store number" : 2  },
   { "product" : "toaster2", "store number2" : 2  })
order by $i."quantity" descending empty greatest
return $i

(: single field ordering - descending - empty greatest :)
