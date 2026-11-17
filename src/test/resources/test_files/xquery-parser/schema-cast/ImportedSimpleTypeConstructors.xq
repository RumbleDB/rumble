(:JIQS: ShouldRun; Output="(true, true, true, true, true, true, true)" :)
import schema namespace t = "urn:cast-test" at "ImportedSimpleTypes.xsd";

(
    let $value := t:Count("42")
    return $value eq 42 and $value instance of t:Count,
    let $value := t:CodeOrInteger("AB")
    return $value instance of t:Code and $value instance of t:CodeOrInteger,
    deep-equal(t:TwoCodes("A B"), ("A", "B")),
    empty(t:Count(())),
    let $constructor := t:Count#1
    return $constructor("42") instance of t:Count,
    let $constructor := function-lookup(QName("urn:cast-test", "TwoCodes"), 1)
    return deep-equal($constructor("A B"), ("A", "B")),
    let $constructor := t:CodeOrInteger(?)
    return $constructor("42") instance of xs:integer
)
