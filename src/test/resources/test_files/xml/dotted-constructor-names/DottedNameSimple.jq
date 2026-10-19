(:JIQS: ShouldRun; Output="true" :)
let $empty := <a.b a.b="value"/>
let $paired := <a.b a.b="value"></a.b>
return every $element in ($empty, $paired) satisfies (
    local-name($element) eq "a.b"
    and local-name($element/@*) eq "a.b"
    and string($element/@*) eq "value"
)
