(:JIQS: ShouldRun; Output="type-error" :)
declare function local:value() as xs:positiveInteger {
    -xs:positiveInteger("1")
};

try {
    local:value()
} catch err:XPTY0004 {
    "type-error"
}

(: Negation must not retain the operand's positiveInteger type and suppress return validation. :)
