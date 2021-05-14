(:JIQS: ShouldCrash; ErrorCode="XPST0005"; ErrorMetadata="LINE:6:COLUMN:10:" :)
declare type local:mytype as {"foo":{"bar": "integer" }};
for $x in validate type local:mytype* {
  {"foo":{ "bar" : 2}}
}
let $y := $x.foo.foobar
return $y