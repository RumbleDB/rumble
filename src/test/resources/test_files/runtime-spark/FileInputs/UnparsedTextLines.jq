(:JIQS: ShouldRun; Output="(8, foo, bar, foobar)" :)
count(unparsed-text-lines("https://raw.githubusercontent.com/RumbleDB/rumble/master/src/test/resources/test_files/runtime/LibraryModules/modulerepeatedimport.jq")),
unparsed-text-lines("../../../queries/file.txt")
