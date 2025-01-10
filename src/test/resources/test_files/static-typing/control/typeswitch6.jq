jsoniq version "3.1";
(:JIQS: ShouldRun :)
(typeswitch(3)
case decimal return "asd"
case integer return 1
default return 12.2) is statically anyAtomicType
