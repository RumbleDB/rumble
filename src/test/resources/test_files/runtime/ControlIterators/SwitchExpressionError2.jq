(:JIQS: ShouldCrash; ErrorCode="XPTY0004"; ErrorMetadata="LINE:2:COLUMN:0:" :)
switch ("no-match")
case [1,2] return "foo"
case "foo" return "bar"
default return "none"

