(:JIQS: ShouldRun; Output="100010000" :)
declare function local:twice($value as integer) as integer {
  $value * 2
};

sum(
  for $value in 1 to 10000
  return local:twice($value)
)
