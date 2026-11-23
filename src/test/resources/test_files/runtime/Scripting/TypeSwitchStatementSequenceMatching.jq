(:JIQS: ShouldRun; Output="(optional, 0, 1, two, 7, 0)" :)
variable $result := ();
typeswitch (())
    case $values as xs:integer? return $result := ("optional", count($values));
    default return $result := "unexpected";

(: Every item must match the same union alternative. :)
typeswitch ((1, "two"))
    case $values as xs:integer+ | xs:string+ return $result := "unexpected";
    default $values return $result := ($result, $values);

(: Keep the first matching case when a singleton also matches a sequence type. :)
typeswitch (7)
    case $values as xs:integer+ return $result := ($result, $values);
    case xs:integer return $result := "unexpected";
    default return $result := "unexpected";

(: An empty default binding must be an empty sequence, not a list containing null. :)
variable $input := ();
typeswitch ($input)
    case xs:string+ return $result := "unexpected";
    default $values return $result := ($result, count($values));
$result
