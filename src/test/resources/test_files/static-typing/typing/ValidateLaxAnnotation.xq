(:JIQS: ShouldRun; Output="(true, true)" :)
(: Only in strip mode is a constructed element an element(a, xs:untyped). :)
declare construction strip;
(: Validation annotates its copy anew, so the copy of an element(a, xs:untyped) is only known to be an element(a). :)
declare function local:validate($e as element(a, xs:untyped)) as element(a) {
    validate lax { $e } is statically element(a)
};
declare function local:validate-document($d as document-node(element(a, xs:untyped))) as document-node(element(a)) {
    validate lax { $d } is statically document-node(element(a))
};
local:validate(<a/>) instance of element(a),
local:validate-document(document { <a/> } treat as document-node(element(a, xs:untyped))) instance of document-node(element(a))
