(:JIQS: ShouldCrash; ErrorCode="XQTY0086" :)
(: Validation gives @q an xs:QName value whose prefix p is bound on its parent.
   Copying only the attribute does not copy that parent namespace context.
   Construction preserve must therefore reject this copy with XQTY0086. :)
declare construction preserve;

import schema namespace t = "urn:construction" at "Construction.xsd";
<wrapper>{ (validate strict { <t:holder xmlns:p="urn:value" q="p:code"/> })/@q }</wrapper>
