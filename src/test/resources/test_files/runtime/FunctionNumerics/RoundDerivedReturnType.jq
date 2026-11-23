(:JIQS: ShouldRun; Output="type-error" :)
declare function local:value() as xs:unsignedByte {
    round(xs:unsignedByte("255"), -1)
};

try {
    local:value()
} catch err:XPTY0004 {
    "type-error"
}

(: Rounding can exceed the operand's restricted range: 260 is not an unsignedByte. :)
