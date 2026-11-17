module namespace m = "urn:preserve-module";
declare construction preserve;
declare function m:wrap($node as node()) { <wrapper>{ $node }</wrapper> };
