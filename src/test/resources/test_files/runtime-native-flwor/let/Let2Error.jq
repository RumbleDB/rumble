(:JIQS: ShouldCrash; ErrorCode="XPST0005"; ErrorMetadata="LINE:6:COLUMN:10:" :)
declare type local:mytype as {"foo": ["integer"]};
for $x in validate type local:mytype* {
  {"foo":[ 2, 3 ] }
}
let $y := $x.foo.foobar
return $y
