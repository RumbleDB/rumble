(:JIQS: ShouldRun; Output="{ "Square of 1" : 1, "Square of 2" : 4, "Square of 3" : 9, "Square of 4" : 16, "Square of 5" : 25, "Square of 6" : 36, "Square of 7" : 49, "Square of 8" : 64, "Square of 9" : 81, "Square of 10" : 100 }" :)
{|for $i in 1 to 10 return { concat("Square of ", $i) : $i * $i } |}

(: dynamic object constructor :)
