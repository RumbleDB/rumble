(:JIQS: ShouldRun; Output="({ "name" : "test@test.com", "failed" : false, "email_verified" : true, "access-attempt" : "2016-11-28T16:00:47.203Z" }, { "name" : "test1@test.com", "failed" : false, "email_verified" : true, "access-attempt" : "2016-11-28T16:00:47.203Z" }, { "name" : "aaa@aaa.com", "failed" : false, "email_verified" : true, "access-attempt" : "2016-11-28T16:00:47.203Z" }, { "name" : "a@a.com", "failed" : false, "email_verified" : true, "access-attempt" : "2016-11-28T16:00:47.203Z" }, { "name" : "test9999@test.com", "failed" : false, "email_verified" : true, "access-attempt" : "2016-11-28T16:00:47.203Z" })" :)
declare %an:assignable variable $res as item* := ();
declare function local:validate-and-return($username as xs:string) {
    variable $user-doc := json-doc("../../../queries/user-names.json");
    variable $log := {};
    variable $user-entry := {
        "access-attempt": fn:current-time(),
        "name": $username,
        "email_verified": false,
        "failed": false
    };
    variable $changed := false;
    variable $counter := 1;
    while ($counter lt (size($user-doc) + 1)) {
        if ($username eq $user-doc[[$counter]].name) then {
            replace value of json $user-entry.email_verified with true;
            replace value of json $user-entry.access-attempt with "2016-11-28T16:00:47.203Z";
            $changed := true;
            break loop;
        } else {
            $counter := $counter + 1;
            continue loop;
        }
    }
    if ($changed eq true) then {
        insert json $user-entry into $log;
    } else {
        exit returning {"failure": true};
    }
    $res := ($res, $log);
    $log
};
variable $usernames := ["test@test.com", "test1@test.com", "aaa@aaa.com", "a@a.com", "test9999@test.com"];
variable $counter := 1;
while ($counter lt (size($usernames) + 1)) {
    variable $change := local:validate-and-return($usernames[[$counter]]);
    if (keys($change)[[1]] eq ["failure"]) then {
        exit returning "failure";
    } else ();
    $counter := $counter + 1;
}
$res