(:JIQS: ShouldRun; Output="(none, foo)" :)
switch (())
case "bar" return "foo"
case "foo" return "bar"
default return "none",
switch (())
case () return "foo"
case "foo" return "bar"
default return "none",
switch ("no-match")
case "bar" return "foo"
case "foo" return "bar"
default return (())

(: empty sequences :)
