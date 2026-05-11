variable $log := json-lines("log.json");
variable $username := "username";
variable $entry := {
    "access-attempt": {
      "timestamp": current-dateTime(),
      "user-name": $username
    }
  };
variable $result as xs:boolean;
$result;