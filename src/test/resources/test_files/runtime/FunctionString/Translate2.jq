(:JIQS: ShouldRun; Output="(DDAAADD, bar, BAr, )" :)
translate("--aaa--","abc-","ABCDE"),

(: if transString is longer than mapString, all excess characters are ignored :)

translate("bar", "", "ABC"),

(: if mapString is empty, return input unchanged :)

translate("bar", "bba", "BCA"),

(: if mapString contains a duplicate character, only the first occurrence in mapString is used :)

translate((), "abcdefg", "foo")

(: the empty sequence as the input will result in the zero-length string :)
