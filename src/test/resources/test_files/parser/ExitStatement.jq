(:JIQS: ShouldParse :)
exit returning local:error(concat("You cannot use the ",
                                  $request.method,
                                  " method with this URL."));