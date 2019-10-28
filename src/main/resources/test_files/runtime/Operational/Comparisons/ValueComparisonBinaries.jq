(:JIQS: ShouldRun; Output="(true, false, false, true)" :)
base64Binary("0 FB8 0F+9") eq base64Binary("0FB80F+9"),
base64Binary(()) eq base64Binary(""),
base64Binary(()) ne base64Binary(""),
base64Binary("abcdEFGH") eq base64Binary("ABCDefgh"),
base64Binary("abcdEFGH") eq null,
hexBinary("ab88") ne null