jsoniq version "3.1";
(:JIQS: ShouldRun :)
(: Exact inferred union members are checked in IsStaticallyTypeInferenceTest. :)
(12 is statically integer, 12.33 is statically decimal, 12e4 is statically double, true is statically boolean, false is statically boolean, null is statically null, "qwerty" is statically string)
