(:JIQS: ShouldRun; Output="({ "product" : "socks", "store number" : 1, "quantity" : 500.56 }, { "product" : "blender", "store number" : 3, "quantity" : 150 }, { "product" : "toaster", "store number" : 2, "quantity" : 100.3 }, { "product" : "blender", "store number" : 3, "quantity" : 100 }, { "product" : "toaster", "store number" : 2, "quantity" : 50 }, { "product" : "toaster", "store number" : 3, "quantity" : 50 }, { "product" : "broiler", "store number" : 1, "quantity" : 20 }, { "product" : "socks", "store number" : 2, "quantity" : 10 }, { "product" : "shirt", "store number" : 3, "quantity" : 10 })" :)
for $i in (
   { "product" : "broiler", "store number" : 1, "quantity" : 20  },
   { "product" : "toaster", "store number" : 2, "quantity" : 100.3 },
   { "product" : "toaster", "store number" : 2, "quantity" : 50 },
   { "product" : "toaster", "store number" : 3, "quantity" : 50 },
   { "product" : "blender", "store number" : 3, "quantity" : 100 },
   { "product" : "blender", "store number" : 3, "quantity" : 150 },
   { "product" : "socks", "store number" : 1, "quantity" : 500.56 },
   { "product" : "socks", "store number" : 2, "quantity" : 10 },
   { "product" : "shirt", "store number" : 3, "quantity" : 10 })
order by $i."quantity" descending  , $i."store number"
return $i

(: general case ordering :)

