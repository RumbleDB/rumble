jsoniq version "3.1";
(:JIQS: ShouldRun :)
(typeswitch(3)
case integer return 1
default return "asd") is statically anyAtomicType