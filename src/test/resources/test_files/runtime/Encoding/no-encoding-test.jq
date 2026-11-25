(:JIQS: ShouldRun; Output="(20320, 22909)" :)
jsoniq version "1.0";
(: Test default UTF-8 encoding when no encoding is declared - Chinese characters 你好 :)
string-to-codepoints("你好")
