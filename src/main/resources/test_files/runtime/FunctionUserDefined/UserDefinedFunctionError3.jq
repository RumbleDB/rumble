(:JIQS: ShouldCrash; ErrorCode="XPST0017"; ErrorMetadata="LINE:2:COLUMN:0:" :)
declare function foo ($m as integer) { $m };
declare function foo ($n as integer) { $n };
foo(4)

(: Function already exists :)
