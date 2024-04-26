(:JIQS: ShouldCompile :)
variable $log := json-file("log.json");
variable $username := "username";
variable $entry := {
    "access-attempt": {
      "timestamp": current-dateTime(),
      "user-name": $username
    }
  };
variable $result as xs:boolean;
if ($username = json-file("log.json").current-users.user.name)
  then {
    replace json value of $entry.access-allowed with "Yes";
    $result := true();
    exit returning  insert json $entry into $log;
  }
else {
    replace json value of $entry.access-allowed with "No";
    $result := false();
    exit returning  $result;
 }