(:JIQS: ShouldCompile :)
variable $log := json-lines("log.json");
variable $username := "username";
variable $entry := {
    "access-attempt": {
      "timestamp": current-dateTime(),
      "user-name": $username
    }
  };
variable $result as xs:boolean;
if ($username = json-lines("log.json").current-users.user.name)
  then {
    replace value of json $entry.access-allowed with "Yes";
    $result := true;
    exit returning  insert json $entry into $log;
  }
else {
    replace value of json $entry.access-allowed with "No";
    $result := false;
    exit returning  $result;
 }