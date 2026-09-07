(:JIQS: ShouldRun; Output="(true, true, true, true, true, true, true, true)" :)
(: Constructors return typed atomic sequences, without requiring schema imports. :)
(
    let $values := xs:IDREFS(" a  b ")
    return deep-equal($values, ("a", "b")) and $values instance of xs:IDREF+,
    let $values := xs:NMTOKENS("a 1:b")
    return count($values) eq 2 and $values instance of xs:NMTOKEN+,
    (: ENTITY construction does not require document-level entity declarations. :)
    let $values := xs:ENTITIES("a b")
    return deep-equal($values, ("a", "b")) and $values instance of xs:ENTITY+,
    empty(xs:IDREFS(())) and empty(xs:NMTOKENS(())) and empty(xs:ENTITIES(())),
    deep-equal(xs:IDREFS(<value>a b</value>), ("a", "b")),
    let $constructor := xs:NMTOKENS#1
    return deep-equal($constructor("a b"), ("a", "b")),
    let $constructor := xs:ENTITIES(?)
    return deep-equal($constructor("a b"), ("a", "b")),
    xs:IDREFS#1 instance of function(xs:anyAtomicType?) as xs:IDREF*
)
