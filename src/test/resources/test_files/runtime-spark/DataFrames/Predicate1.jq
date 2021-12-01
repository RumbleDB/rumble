(:JIQS: ShouldRun; Output="(175, 91)" :)
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