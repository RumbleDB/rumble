(:JIQS: ShouldRun; Output="(true, true, true, true, true, true, true, true)" :)
import schema namespace t = "urn:cast-test" at "ImportedSimpleTypes.xsd";

(
    (: A same-type cast preserves the value even if its canonical form fails the original lexical pattern. :)
    let $value := "042" cast as t:LeadingZeroInteger
    return ($value cast as t:LeadingZeroInteger) eq 42,
    (: A restricted union must check its own pattern for non-string inputs too. :)
    42 castable as t:TwoDigitUnion,
    not(7 castable as t:TwoDigitUnion),
    not(123 castable as t:TwoDigitUnion),
    (: Integer is a casting primitive: crossing to decimal checks its canonical form "42.0". :)
    42 castable as t:DecimalPointRequired,
    42 castable as t:DecimalPointUnion,
    (xs:decimal("42") cast as t:DecimalPointRequired) eq 42,
    ("42.0" cast as t:DecimalPointRequired) eq 42
)
