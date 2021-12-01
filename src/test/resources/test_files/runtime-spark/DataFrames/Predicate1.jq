(:JIQS: ShouldRun; Output="({ "foo" : "1", "bar" : "1" }, { "foo" : "2", "bar" : "2" }, { "foo" : "3", "bar" : "3" }, { "foo" : "4", "bar" : "4" }, { "foo" : "5", "bar" : "5" }, { "foo" : "6", "bar" : "6" }, { "foo" : "7", "bar" : "7" }, { "foo" : "8", "bar" : "8" }, { "foo" : "9", "bar" : "9" }, { "foo" : "10", "bar" : "10" })" :)
declare type local:t as {
  "foo" : "string",
  "bar" : "string"
};

let $data := validate type local:t* {
  for $i in 1 to 1000
  for $j in 1 to 300
  return { "foo" : string($i), "bar" : string($j) }
}
return count(
  $data[$$.foo eq $$.bar]
)