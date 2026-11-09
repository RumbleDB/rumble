(:JIQS: ShouldCrash; ErrorCode="FOTY0013"; ErrorMetadata="LINE:2:COLUMN:0:" :)
switch ("no-match")
case { "foo" : "bar" } return "foo"
case "foo" return "bar"
default return "none"

