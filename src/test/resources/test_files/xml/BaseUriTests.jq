(:JIQS: ShouldRun; Output="(http://example.org/tests/, http://example.com/base/, http://example.com/base/, http://example.com/base/sub/, http://example.com/sub/, http://example.org/tests/, http://example.org/tests/, http://example.com/base/, http://example.com/café/résumé.xml)" :)
declare base-uri "http://example.org/tests/";

base-uri(<root/>),
base-uri(<root xml:base="http://example.com/base/"/>),
base-uri(<root xml:base="http://example.com/base/"><child/></root>/child),
base-uri(<root xml:base="http://example.com/base/"><child xml:base="sub/"/></root>/child),
base-uri(<root xml:base="http://example.com/base/"><child xml:base="../sub/"/></root>/child),
base-uri(document { <root/> }),
base-uri(parse-xml("<root/>")),
base-uri(<root xml:base="http://example.com/base/" attr="val"/>/@attr),
base-uri(<root xml:base="http://example.com/café/"><child xml:base="résumé.xml"/></root>/child)
