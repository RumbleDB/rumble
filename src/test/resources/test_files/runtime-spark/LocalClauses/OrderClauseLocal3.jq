(:JIQS: ShouldRun; Output="({ "product" : "broiler", "quantity" : 20 }, { "product" : "broiler3", "quantity" : 20 }, { "product" : "broiler2", "quantity" : 20 })":)
for $i in (
   { "product" : "broiler", "quantity" : 20  },
   { "product" : "broiler3", "quantity" : 20  },
   { "product" : "broiler2", "quantity" : 20  })
order by $i."quantity" descending
return $i


(: equal key ordering - descending (gives same result as ascending) :)
