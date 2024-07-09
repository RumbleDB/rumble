(:JIQS: ShouldRun; Output="{ "name" : "aaa@aaa.com", "user_id" : "583c57672c7686377d2f66c9", "failed" : false, "email_verified" : true, "access-attempt" : "2016-11-28T16:00:47.203Z" }" :)
declare function local:validate-and-log($username as xs:string) {
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
    if ($username eq $user-doc[[3]].name) then {
        replace json value of $user-entry.email_verified with true;
        replace json value of $user-entry.access-attempt with "2016-11-28T16:00:47.203Z";
        $changed := true;
    } else {
        replace json value of $user-entry.failed with true;
    }
    if ($changed eq true) then {
        insert json $user-entry into $log;
    } else {
        exit returning {"failure": true};
    }
    $log
};
local:validate-and-log("aaa@aaa.com")