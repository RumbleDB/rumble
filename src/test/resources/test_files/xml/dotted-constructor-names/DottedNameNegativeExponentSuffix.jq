(:JIQS: ShouldRun; Output="true" :)
let $empty := <a.1e-2 a.1e-2="value"/>
let $paired := <a.1e-2 a.1e-2="value"></a.1e-2>
return every $element in ($empty, $paired) satisfies (
    local-name($element) eq "a.1e-2"
    and local-name($element/@*) eq "a.1e-2"
    and string($element/@*) eq "value"
)
