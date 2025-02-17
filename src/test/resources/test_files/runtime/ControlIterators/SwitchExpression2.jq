(:JIQS: ShouldRun; Output="(foo, 1 + 1 is 2, 1)" :)
switch (2)
case 1 + 1 return "foo"
case 2 + 2 return "bar"
default return "none",
switch (true)
case 1 + 1 eq 2 return "1 + 1 is 2"
case 2 + 2 eq 5 return "2 + 2 is 5"
default return "none of the above is true",
switch (true)
case 1 + 1 eq 3 return "1 + 1 is 2"
default return "1"

(: boolean and arithmetic usage and default case:)
