(:JIQS: ShouldRun; Output="false" :)
exists(map:get(map:remove({"http://example.com":"bar", "x":"y"}, xs:anyURI("http://example.com")), "http://example.com"))
