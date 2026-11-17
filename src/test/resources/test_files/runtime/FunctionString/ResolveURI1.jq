(:JIQS: ShouldRun; Output="(http://www.base-uri.com/test, true)" :)
resolve-uri("test", "http://www.base-uri.com/"),
resolve-uri("relative/uri.ext", "http://www.base-uri.com/") eq xs:anyURI("http://www.base-uri.com/relative/uri.ext"),
resolve-uri(())
(: base uri and empty sequence :)
