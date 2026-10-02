(:JIQS: ShouldRun; Output="(I, O, x)" :)
replace("I", "[^i]", "x", "i"),
replace("O", "[A-Z-[OI]]", "x", "i"),
replace(codepoints-to-string(8490), "[A-Z]", "x", "i")
