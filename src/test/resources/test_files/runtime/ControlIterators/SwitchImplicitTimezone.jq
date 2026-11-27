(:JIQS: ShouldRun; Output="true":)
let $local := dateTime("2012-05-30T12:00:00")
let $zoned := adjust-dateTime-to-timezone($local, implicit-timezone())
return switch ($local)
    case $zoned return true
    default return false
