(:JIQS: ShouldRun; Output="(These-are-some-words, abc, Now is the time ..., Blow, blow, thou winter wind!)" :)
string-join(("These", "are", "some", "words"), "-"),
string-join(("a","b","c")),
string-join(("Now", "is", "the", "time", "..."), " "),
string-join(("Blow, ", "blow, ", "thou ", "winter ", "wind!"), "")
