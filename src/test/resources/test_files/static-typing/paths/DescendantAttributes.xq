(:JIQS: ShouldRun; Output="(2, 3)" :)
(: E//@a abbreviates E/descendant-or-self::node()/attribute::a, whose context node() may be an element. :)
let $tree := <a b="1"><c d="2"/><c d="3"/></a>
return data($tree//@d)
