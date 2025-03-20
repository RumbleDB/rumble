jsoniq version "3.1";
(:JIQS: ShouldRun :)
(switch(3)
case 12 return 12
default return "not 12"
) is statically anyAtomicType