(:JIQS: ShouldCrash; ErrorCode="XPTY0004"; ErrorMetadata="LINE:3:COLUMN:0:" :)
for $j as base64Binary in (base64Binary("0123456789abcdef"), base64Binary("AaBb"), base64Binary("aAbB"), base64Binary("0FB80F+9"), base64Binary("0 FB8 0F+9"), base64Binary("0F+40A=="), base64Binary(()))
order by $j
return $j