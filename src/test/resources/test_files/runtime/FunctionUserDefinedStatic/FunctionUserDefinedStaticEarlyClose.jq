(:JIQS: ShouldRun; Output="500500" :)
declare function local:pair($value as integer) as integer+ {
  ($value, $value + 1)
};

sum(
  for $value in 1 to 1000
  return head(local:pair($value))
)
