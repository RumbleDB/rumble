(:JIQS: ShouldRun; Output="true":)
normalize-space(" &#9;a&#13;&#10; b &#9;") eq "a b"
and normalize-space(()) eq ""
and normalize-space(codepoints-to-string((160, 8195, 128512))) eq codepoints-to-string((160, 8195, 128512))
and resolve-uri("", "http://example.com/a%20b?x=%2F") eq xs:anyURI("http://example.com/a%20b?x=%2F")
and resolve-uri("b", "http://example.com/a/") eq xs:anyURI("http://example.com/a/b")
and empty(resolve-uri((), "http://example.com/a"))
and (try { string(resolve-uri("", "relative/path")) } catch * { local-name-from-QName($err:code) }) eq "FORG0002"
