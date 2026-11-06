jsoniq version "3.1";
(:JIQS: ShouldRun :)
(: The exact inferred union is checked in IsStaticallyTypeInferenceTest. :)
(try { 3 }
catch * { "str" })