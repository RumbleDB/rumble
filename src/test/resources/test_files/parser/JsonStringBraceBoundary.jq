(:JIQS: ShouldRun; Output="(true, true, true, true, true, true, true, true, true)" :)
(
    fn:matches("abracadabra{abracadabra", "\\{"),
    fn:replace("abracadabra{abracadabra", "\\{", "with") eq "abracadabrawithabracadabra",
    deep-equal(
        fn:tokenize("abracadabra{abracadabra", "\\{"),
        ("abracadabra", "abracadabra")
    ),
    parse-json("{\"a\":" || year-from-date(current-date()) || "}")?a gt 2000,
    concat(
        "{",
        namespace-uri-from-QName(xs:QName("xs:integer")),
        "}",
        local-name-from-QName(xs:QName("xs:integer"))
    ) eq "{http://www.w3.org/2001/XMLSchema}integer",
    1 < 2 and "{" eq "{",
    "{value} &custom;" eq "{value} &custom;",
    data(<e value="before {1} after"/>/@value) eq "before 1 after",
    data(<e value="{concat("{", "}")}"/>/@value) eq "{}"
)
