(:JIQS: ShouldNotCompile; ErrorCode="XQST0125" :)
let $f := %public function($arg as xs:integer) as xs:integer { $arg + 1 }
return $f(1)
