(:JIQS: ShouldRun; Output="6" :)
(: A case variable is typed, also when the return expression has a static context of its own. :)
string(
  typeswitch (5)
  case $i as xs:integer return <wrap>{ $i + 1 is statically xs:integer }</wrap>
  default return ()
)
