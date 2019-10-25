(:JIQS: ShouldRun; Output="(true, false, false, false)" :)
hexBinary("AaBb") = hexBinary("AABB"),
hexBinary(()) = hexBinary(""),
hexBinary(()) != hexBinary(""),
hexBinary("0000") = hexBinary("00")

(: general tests :)