(:JIQS: ShouldRun; Output="true" :)
matches("$$9", "(.)\\19")
and matches("#abc#1", "^(#)abc\\11$")
and matches("$$9", "(((((((((((.)))))))))))\\119")
and matches(
  "abcdefghiabcdefghia0a1",
  "(a)(b)(c)(d)(e)(f)(g)(h)(i)\\1\\2\\3\\4\\5\\6\\7\\8\\9\\10\\11"
)
