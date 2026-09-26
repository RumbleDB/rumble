(:JIQS: ShouldRun; Output="true" :)
string-to-codepoints('&#x07;') eq 7
and string-to-codepoints(string(<e>&#x7;</e>)) eq 7
and string-to-codepoints(string(<e a="&#x7;"/>/@a)) eq 7
