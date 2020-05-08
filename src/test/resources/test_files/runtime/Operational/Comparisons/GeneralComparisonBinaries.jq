(:JIQS: ShouldRun; Output="(true, false, false, false, false, true)" :)
base64Binary("0 FB8 0F+9") = base64Binary("0FB80F+9"),
base64Binary(()) = base64Binary(""),
base64Binary(()) != base64Binary(""),
base64Binary("abcdEFGH") = base64Binary("ABCDefgh"),
base64Binary("abcdEFGH") = null,
hexBinary("ab88") != null

(: general tests :)
