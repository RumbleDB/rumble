(:JIQS: ShouldParse :)
let $stores :=
[
{ "store number" : 1, "state" : "MA" },
{ "store number" : 2, "state" : "MA" },
{ "store number" : 3, "state" : "CA" },
{ "store number" : 4, "state" : "CA" }
]
let $sales := [
{ "product" : "broiler", "store number" : 1, "quantity" : 20  },
{ "product" : "toaster", "store number" : 2, "quantity" : 100 },
{ "product" : "toaster", "store number" : 2, "quantity" : 50 },
{ "product" : "toaster", "store number" : 3, "quantity" : 50 },
{ "product" : "blender", "store number" : 3, "quantity" : 100 },
{ "product" : "blender", "store number" : 3, "quantity" : 150 },
{ "product" : "socks", "store number" : 1, "quantity" : 500 },
{ "product" : "socks", "store number" : 2, "quantity" : 10 },
{ "product" : "shirt", "store number" : 3, "quantity" : 10 }
]
let $join :=
for $store in $stores[], $sale in $sales[]
where $store."store number" eq $sale."store number"
return {
"nb" : $store."store number",
"state" : $store.state,
"sold" : $sale.product
}
return [$join]