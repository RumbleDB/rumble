(:JIQS: ShouldCrash; ErrorCode="FOTY0013";  ErrorMetadata="LINE:2:COLUMN:78:" :)
function($i as string?, $j as double) as string* {$i || ($j cast as string)} ({"hello": "world!"}, 2e3)

(: Invalid argument. object cannot be atomized during function conversion. :)
