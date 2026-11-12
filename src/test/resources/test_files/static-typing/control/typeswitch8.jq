(:JIQS: ShouldRun; Output="5" :)
(: A case variable of a typeswitch statement is typed, also when the return statement has a static context of its own. :)
typeswitch (5)
    case $i as integer return variable $j := $i + 1 is statically integer;
    default return ();
5
