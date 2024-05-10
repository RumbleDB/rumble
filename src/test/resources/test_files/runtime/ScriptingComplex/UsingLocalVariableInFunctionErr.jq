(:JIQS: ShouldNotCompile; Error="XPST0008"; ErrorMetadata="LINE:14:COLUMN:27:" :)
declare function local:validate-and-return($username as xs:string) {
    variable $user-doc := json-doc("../../../queries/user-names.json");
    variable $log as object := {};
    variable $user-entry := {
        "access-attempt": fn:current-time(),
        "name": "aaa@aaa.com",
        "user_id": "583c57672c7686377d2f66c9",
        "email_verified": false,
        "failed": false
    };
    variable $changed := false;
    variable $counter := 1;
    while ($counter < size($usernames)) {
        if ($username eq $user-doc[[$counter]].name) then {
            replace json value of $user-entry.email_verified with true;
            replace json value of $user-entry.access-attempt with "2016-11-28T16:00:47.203Z";
            $changed := true;
        } else {
            replace json value of $user-entry.failed with true;
        }
        $counter := $counter + 1;
    }
    if ($changed eq true) then {
        insert json $user-entry into $log;
    } else {
        exit returning {"failure": true};
    }
    $log
};
variable $usernames := ["test@test.com", "test1@test.com", "aaa@aaa.com", "a@a.com", "test9999@test.com"];
variable $counter := 1;
variable $res := ();
while ($counter < size($usernames)) {
    variable $change := local:validate-and-return($usernames[[$counter]]);
    if (keys($change)[[1]] eq ["failure"]) then {
        exit returning "failure";
    } else $res := ($res, $change);
    $counter := $counter + 1;
}
$res