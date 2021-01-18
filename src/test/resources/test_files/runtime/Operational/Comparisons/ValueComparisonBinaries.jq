(:JIQS: ShouldRun; Output="(true, false, false, true, false, false, BBBB, BB, AA, 88, )" :)
base64Binary("0 FB8 0F+9") eq base64Binary("0FB80F+9"),
base64Binary(()) eq base64Binary(""),
base64Binary(()) ne base64Binary(""),
base64Binary("abcdEFGH") eq base64Binary("ABCDefgh"),
base64Binary("abcdEFGH") eq null,
hexBinary("ab88") ne null,
hexBinary("767479716c6a647663") gt hexBinary("767479716c6a647663"),
base64Binary("dnR5cWxqZHZj") gt base64Binary("dnR5cWxqZHZj"),
for $i in (hexBinary("aa"), hexBinary("bb"), hexBinary(""), hexBinary("88"), hexBinary("bbbb"))
order by $i descending
return string($i)
