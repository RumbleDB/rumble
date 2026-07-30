(:JIQS: ShouldRun; Output="(none, foo, empty, default, nan, string)" :)
switch (())
case "bar" return "foo"
case "foo" return "bar"
default return "none",
switch (())
case () return "foo"
case "foo" return "bar"
default return "none",
switch (())
case "non-empty" return "wrong"
case () return "empty"
default return "wrong",
switch ("value")
case () return "wrong"
default return "default",
switch (xs:double("NaN"))
case xs:double("NaN") return "nan"
default return "wrong",
switch (xs:untypedAtomic("1"))
case 1 return "wrong"
case "1" return "string"
default return "wrong",
switch ("no-match")
case "bar" return "foo"
case "foo" return "bar"
default return (())

(: empty sequences :)
