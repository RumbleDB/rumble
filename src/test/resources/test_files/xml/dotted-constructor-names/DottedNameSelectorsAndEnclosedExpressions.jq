(:JIQS: ShouldRun; Output="true" :)
let $object := {"field": 42}
let $element := <a.b attr.name="{$object.field}">{$object.field}<c.d/></a.b>
return (
    $object.field eq 42
    and $object."field" eq 42
    and string($element/@*) eq "42"
)
