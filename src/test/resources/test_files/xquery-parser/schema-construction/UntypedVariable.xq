(:JIQS: ShouldCrash; ErrorCode="XPTY0004" :)
declare variable $v as element(*, xs:untyped?)+ := <e/>;
exists($v/*)
