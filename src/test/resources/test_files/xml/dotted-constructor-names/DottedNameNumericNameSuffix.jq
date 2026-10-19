(:JIQS: ShouldRun; Output="true" :)
let $empty := <a.123b a.123b="value"/>
let $paired := <a.123b a.123b="value"></a.123b>
return every $element in ($empty, $paired) satisfies (
    local-name($element) eq "a.123b"
    and local-name($element/@*) eq "a.123b"
    and string($element/@*) eq "value"
)
