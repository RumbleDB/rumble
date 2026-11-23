(:JIQS: ShouldRun; Output="(1, 2)" :)
variable $result := ();
typeswitch ((1, 2))
    case $value as xs:integer return $result := $value;
    case $values as xs:integer+ return $result := $values;
    default return $result := 0;
$result

(: Both input items belong to the sequence case; selecting a singleton case loses the second item. :)
