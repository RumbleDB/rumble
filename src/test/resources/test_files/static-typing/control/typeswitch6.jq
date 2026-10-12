jsoniq version "3.1";
(:JIQS: ShouldRun :)
(: The exact inferred union is checked in IsStaticallyTypeInferenceTest. :)
(typeswitch(3)
case decimal return "asd"
case integer return 1
default return 12.2)