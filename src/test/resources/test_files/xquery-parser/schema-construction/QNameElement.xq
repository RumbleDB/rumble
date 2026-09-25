(:JIQS: ShouldCrash; ErrorCode="XQTY0086" :)
(: Validation gives the element an xs:QName value, p:code. Construction preserve
   retains that typed value, but copy-namespaces no-preserve drops the p binding,
   which is used only in the value. This combination must raise XQTY0086. :)
declare construction preserve;
declare copy-namespaces no-preserve, inherit;
import schema namespace t = "urn:construction" at "Construction.xsd";
<wrapper>{ validate strict { <t:qname xmlns:p="urn:value">p:code</t:qname> } }</wrapper>
