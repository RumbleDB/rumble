(:JIQS: ShouldRun; Output="(20320, 22909)" :)
jsoniq version "1.0" encoding "UTF-8";
(: Test that UTF-8 encoding is honored - Chinese characters 你好 :)
string-to-codepoints("你好")
