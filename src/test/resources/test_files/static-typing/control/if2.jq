jsoniq version "3.1";
(:JIQS: ShouldRun :)
(: The exact inferred union is checked in IsStaticallyTypeInferenceTest. :)
(if(true)
then 3
else "str")