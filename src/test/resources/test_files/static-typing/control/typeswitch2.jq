jsoniq version "3.1";
(:JIQS: ShouldRun :)
(: The exact inferred union is checked in IsStaticallyTypeInferenceTest. :)
(typeswitch(3)
case integer return 1
default return "asd")