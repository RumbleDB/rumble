(:JIQS: ShouldRun; Output="(bar, b)" :)
switch ("foo")
case "a" case "bar" case "b" return "foo"
case "c" case "foo" case "d" return "bar"
default return "none",
switch(3)
case "a" return "a"
default return "b"

(: empty sequences :)
