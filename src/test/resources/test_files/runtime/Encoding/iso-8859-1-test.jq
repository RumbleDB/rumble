(:JIQS: ShouldRun; Output="(99, 97, 102, 233)" :)
jsoniq version "1.0" encoding "ISO-8859-1";
(: Test that ISO-8859-1 encoding is honored - é is codepoint 233 in ISO-8859-1 :)
string-to-codepoints("café")
