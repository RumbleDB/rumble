jsoniq version "3.1";
(:JIQS: ShouldRun :)
(try { (1,2) }
catch * { "str" }) is statically anyAtomicType+