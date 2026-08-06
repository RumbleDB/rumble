(:JIQS: ShouldRun; Output="true" :)
string(<e attr=" before {concat("a", "b")} after ""quoted"" "/>/@attr)
    eq ' before ab after "quoted" '
and string(<e attr=' before {concat('a', 'b')} after ''quoted'' '/>/@attr)
    eq " before ab after 'quoted' "
