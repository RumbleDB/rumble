(:JIQS: ShouldCrash; ErrorCode="XPDY0130"; :)
for $i in (
   { "product" : "broiler", "store number" : 1, "quantity" : 20  },
   { "product" : "toaster", "store number" : 2, "quantity" : "aaaa"  })
order by $i."quantity"
return $i

(: mismatch in order types:)
