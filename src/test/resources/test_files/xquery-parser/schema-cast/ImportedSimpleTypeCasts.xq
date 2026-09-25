(:JIQS: ShouldRun; Output="(true, true, true, true, true, true, true, true, true, true)" :)
import schema namespace t = "urn:cast-test" at "ImportedSimpleTypes.xsd";

(
    let $value := "42" cast as t:Count
    return $value eq 42 and $value instance of t:Count,
    (42 cast as t:Count) instance of t:Count,
    not("-1" castable as t:Count),
    not((-1) castable as t:Count),
    let $value := "AB" cast as t:CodeOrInteger
    return $value instance of t:Code and $value instance of t:CodeOrInteger,
    let $values := "a b xs:integer" cast as t:ListOfUnions
    return count($values) eq 3
        and $values[1] instance of xs:NCName
        and $values[1] instance of t:SensitiveUnion
        and $values[3] instance of xs:QName,
    "A B" castable as t:TwoCodes,
    not("A B C" castable as t:TwoCodes),
    empty(() cast as t:Count?),
    () castable as t:Count? and not((1, 2) castable as t:Count)
)
