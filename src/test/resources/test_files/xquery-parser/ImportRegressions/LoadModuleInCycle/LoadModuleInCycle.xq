(:JIQS: ShouldRun; Output="(f, vb)" :)
(: Loading a module exports its own public declarations, not those of a module it shares a cycle with. :)
let $module := fn:load-xquery-module("urn:import-regression:load-b", map { "location-hints": "load-cycle-b.xq" })
return (
  map:keys($module?functions) ! fn:local-name-from-QName(.),
  map:keys($module?variables) ! fn:local-name-from-QName(.)
)
