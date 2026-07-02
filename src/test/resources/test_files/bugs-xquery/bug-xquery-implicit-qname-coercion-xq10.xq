(:JIQS: ShouldCrash; ErrorCode="XPTY0004" :)
xquery version "1.0";

declare function local:clarkname($q as xs:QName) as xs:string {
  concat("{", namespace-uri-from-QName($q), "}", local-name-from-QName($q))
};

let $var := <e>xml:space</e>
return (
  local:clarkname(node-name($var)),
  local:clarkname(xs:untypedAtomic($var))
)
