jsoniq version "3.1";
(:JIQS: ShouldRun :)
(try{ 3 }
catch XPTY0004 { 12.3 }
catch * { "asd" }) is statically anyAtomicType