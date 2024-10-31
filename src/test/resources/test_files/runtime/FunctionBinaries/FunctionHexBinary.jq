(:JIQS: ShouldRun; Output="(0123456789ABCDEF, AABB, 0123456789ABCDEF, AA==)" :)
hexBinary("0123456789abcdef"),
hexBinary("AaBb"),
hexBinary(()),
hexBinary(base64Binary(hexBinary("0123456789abcdef"))),
base64Binary(hexBinary("00"))

(: general tests :)
