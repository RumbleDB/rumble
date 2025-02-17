(:JIQS: ShouldRun; Output="({ "guess" : "Latvian", "target" : "Russian", "country" : "AU", "choices" : [ "Lao", "Latvian", "Russian", "Swahili" ], "sample" : "b7df3f9d67cef259fbcaa5abcad9d774", "date" : "2013-08-20" }, { "guess" : "Russian", "target" : "Russian", "country" : "AU", "choices" : [ "Croatian", "Nepali", "Russian", "Slovenian" ], "sample" : "8a59d48e99e8a1df7e366c4648095e27", "date" : "2013-08-20" })" :)
let $guess := "Russian"
let $pos := 1
return
  for $i in json-file("../../../queries/conf-ex.json", 10)
  where $i.target eq $guess
  return $i[$pos]
  
