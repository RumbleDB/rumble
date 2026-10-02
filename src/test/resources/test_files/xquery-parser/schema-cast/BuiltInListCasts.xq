(:JIQS: ShouldRun; Output="(true, true, true, true, true, true, true, true)" :)
(: Built-in list targets need no schema import and produce typed atomic sequences. :)
(
    let $values := " a  b " cast as xs:IDREFS
    return deep-equal($values, ("a", "b")) and $values instance of xs:IDREF+,
    let $values := "a 1:b" cast as xs:NMTOKENS
    return count($values) eq 2 and $values instance of xs:NMTOKEN+,
    (: ENTITY values do not need unparsed entity declarations when created by casting. :)
    let $values := "a b" cast as xs:ENTITIES
    return deep-equal($values, ("a", "b")) and $values instance of xs:ENTITY+,
    not("1 2" castable as xs:IDREFS) and not("1 2" castable as xs:ENTITIES),
    not("a ;" castable as xs:NMTOKENS),
    (: '?' accepts absent input, but does not make an empty lexical list valid. :)
    () castable as xs:NMTOKENS? and empty(() cast as xs:IDREFS?),
    not(() castable as xs:ENTITIES) and not(" " castable as xs:NMTOKENS?),
    not(("a", "b") castable as xs:IDREFS) and not(42 castable as xs:NMTOKENS)
)
