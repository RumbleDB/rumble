(:JIQS: ShouldCrash; ErrorCode="XQST0089"; ErrorMetadata="LINE:5:COLUMN:12:" :)

for $a in parallelize((true, false))
return
  for $i at $i in 10 to 19
  return { "a" : $a, "i" : $i, "x" : $x }
