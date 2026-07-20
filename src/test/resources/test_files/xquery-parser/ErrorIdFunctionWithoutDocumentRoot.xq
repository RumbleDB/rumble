(:JIQS: ShouldCrash; ErrorCode="FODC0001" :)
let $element := <root xml:id="root-id"/>
return fn:id("root-id", $element)
