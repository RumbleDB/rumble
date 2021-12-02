(:JIQS: ShouldRun; Output="({ "foo" : "1", "bar" : "1" }, { "foo" : "2", "bar" : "2" }, { "foo" : "3", "bar" : "3" }, { "foo" : "4", "bar" : "4" }, { "foo" : "5", "bar" : "5" }, { "foo" : "6", "bar" : "6" }, { "foo" : "7", "bar" : "7" }, { "foo" : "8", "bar" : "8" }, { "foo" : "9", "bar" : "9" }, { "foo" : "10", "bar" : "10" }, { "foo" : "1", "bar" : "1" }, { "foo" : "1", "bar" : "2" }, { "foo" : "10", "bar" : "9" }, { "foo" : "10", "bar" : "10" })" :)
declare type local:t as {
  "foo" : "string",
  "bar" : "string"
};

let $data := validate type local:t* {
  for $i in 1 to 10
  for $j in 1 to 10
  return { "foo" : string($i), "bar" : string($j) }
}
return (
  $data[$$.foo eq $$.bar],
  $data[position() le 2],
  $data[position() ge last() - 1]
)