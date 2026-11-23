(:JIQS: ShouldRun; Output="(empty-error, type-error, 42, 1, 2, 0, 7)" :)
declare function local:empty() as xs:integer {
    exit returning ();
    0
};
declare function local:wrong-type() as xs:integer {
    exit returning "wrong";
    0
};
declare function local:node() as xs:integer {
    exit returning <n>42</n>;
    0
};
declare function local:sequence() as xs:integer+ {
    exit returning (1, 2);
    0
};
declare function local:optional() as xs:integer? {
    exit returning ();
    0
};
declare function local:normal($exit as xs:boolean) as xs:integer {
    if ($exit) then exit returning (1, 2); else ();
    7
};

(
    try { let $value := local:empty() return $value }
    catch err:XPTY0004 { "empty-error" },
    try { let $value := local:wrong-type() return $value }
    catch err:XPTY0004 { "type-error" },
    local:node(),
    local:sequence(),
    count(local:optional()),
    local:normal(false)
)
