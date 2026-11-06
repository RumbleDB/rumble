jsoniq version "3.1";
(:JIQS: ShouldRun :)
(: The exact inferred union is checked in IsStaticallyTypeInferenceTest. :)
(switch(3)
case 2 case 3 return "a"
case 4 return 12
default return 12.4)