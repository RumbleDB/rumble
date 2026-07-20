(:JIQS: ShouldRun; Output="true" :)
let $element := <root status="active"/>
return
  $element instance of element(root, xs:untyped)
  and $element instance of element(*, xs:anyType)
  and $element/@status instance of attribute(status, xs:untypedAtomic)
  and $element/attribute::attribute(*, xs:anyAtomicType) = "active"
