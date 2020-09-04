(:JIQS: ShouldCrash; ErrorCode="XQST0034"; ErrorMetadata="LINE:3:COLUMN:0:" :)
declare function foo ($m as integer) { $m };
declare function foo ($n as integer) { $n };
foo(4)

(: Function already exists :)
