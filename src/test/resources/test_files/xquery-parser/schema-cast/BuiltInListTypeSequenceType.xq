(:JIQS: ShouldNotCompile; ErrorCode="XPST0051" :)
(: A list is a cast target, not an XDM item type, even after its catalog has been loaded. :)
let $values := "a b" cast as xs:IDREFS
return $values instance of xs:IDREFS
