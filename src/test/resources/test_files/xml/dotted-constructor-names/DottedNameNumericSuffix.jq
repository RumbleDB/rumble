(:JIQS: ShouldRun; Output="true" :)
let $empty := <a.123 a.123="value"/>
let $paired := <a.123 a.123="value"></a.123>
return every $element in ($empty, $paired) satisfies (
    local-name($element) eq "a.123"
    and local-name($element/@*) eq "a.123"
    and string($element/@*) eq "value"
)
