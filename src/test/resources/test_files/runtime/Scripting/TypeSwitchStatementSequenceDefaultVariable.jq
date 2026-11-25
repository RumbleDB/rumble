(:JIQS: ShouldRun; Output="(1, 2)" :)
variable $result := ();
typeswitch ((1, 2))
    case xs:string return $result := "unexpected";
    default $values return $result := $values;
$result

(: The default variable must receive the entire input sequence. :)
