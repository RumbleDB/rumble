(:JIQS: ShouldRun; Output="true" :)
declare namespace z = "http://example.com/z";
(: Keep the untyped value's type unknown statically, including on master. :)
declare variable $p as xs:anyAtomicType external := xs:untypedAtomic('z:local');

$p = (<xs:element/>, <z:local/>, <fn:function/>)!node-name(.)
