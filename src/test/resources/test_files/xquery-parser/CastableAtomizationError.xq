(:JIQS: ShouldRun; Output="true" :)
(: An atomization error must propagate, rather than becoming a false castability result. :)
try {
    map { 'a': 1 } castable as xs:integer
} catch err:FOTY0013 {
    true()
}
