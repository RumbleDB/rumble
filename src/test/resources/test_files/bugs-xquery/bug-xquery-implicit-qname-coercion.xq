(:JIQS: ShouldCrash; ErrorCode="XPTY0117" :)
declare function local:clarkname($q as xs:QName) as xs:string {
  concat("{", namespace-uri-from-QName($q), "}", local-name-from-QName($q))
};

let $var := <e>xml:space</e>
return (
  local:clarkname(node-name($var)),
  local:clarkname(xs:untypedAtomic($var))
)
