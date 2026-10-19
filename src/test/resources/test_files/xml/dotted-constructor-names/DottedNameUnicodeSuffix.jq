(:JIQS: ShouldRun; Output="true" :)
let $empty := <a.é a.é="value"/>
let $paired := <a.é a.é="value"></a.é>
return every $element in ($empty, $paired) satisfies (
    local-name($element) eq "a.é"
    and local-name($element/@*) eq "a.é"
    and string($element/@*) eq "value"
)
