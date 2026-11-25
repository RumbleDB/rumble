(:JIQS: ShouldRun; Output="true":)
(: XQuery 3.1 §§3.9.3.5, 3.9.3.7; F&O 3.1 §10.1.
   Cover Unicode names, XML whitespace, and retained lexical/namespace errors. :)
every $ok in (
    name(processing-instruction {"&#9;é&#10;"} {}) eq "é",
    name(namespace {codepoints-to-string(65536)} {"urn:test"}) eq codepoints-to-string(65536),
    string(processing-instruction p {"&#9;&#10;&#13; x "}) eq "x ",
    string(<?p  x?>) eq codepoints-to-string((8195, 120)),
    local-name-from-QName(resolve-QName("&#9;p:é&#10;", <r xmlns:p="urn:p"/>)) eq "é",
    empty(resolve-QName((), <r/>)),
    try { string(processing-instruction {"a:b"} {}) eq "missing-error" } catch err:XQDY0041 { true() },
    try { string(processing-instruction {"XML"} {}) eq "missing-error" } catch err:XQDY0064 { true() },
    try { string(namespace {"a b"} {"urn:test"}) eq "missing-error" } catch err:XQDY0074 { true() },
    try { string(namespace {"xml"} {"urn:wrong"}) eq "missing-error" } catch err:XQDY0101 { true() },
    try { string(resolve-QName(" a b ", <r/>)) eq "missing-error" } catch err:FOCA0002 { true() },
    try { string(resolve-QName(" p:a ", <r/>)) eq "missing-error" } catch err:FONS0004 { true() }
) satisfies $ok
