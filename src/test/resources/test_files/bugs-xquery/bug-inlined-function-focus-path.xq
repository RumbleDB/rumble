(:JIQS: ShouldCrash; ErrorCode="XPDY0002" :)
(: A function body has no focus, so it does not read the context item of a path step that calls it. :)
declare function local:myFunc() { e };
<e/>/local:myFunc()/1
