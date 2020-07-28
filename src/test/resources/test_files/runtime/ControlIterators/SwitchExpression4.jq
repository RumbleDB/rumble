(:JIQS: ShouldRun; Output="bar" :)
switch ("foo")
case "a" case "bar" case "b" return "foo"
case "c" case "foo" case "d" return "bar"
default return "none"

(: empty sequences :)
