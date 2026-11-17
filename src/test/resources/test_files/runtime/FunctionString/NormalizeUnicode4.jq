(:JIQS: ShouldRun; Output="רּשּ" :)
normalize-unicode("רּשּ", "FULLY-NORMALIZED")

(: U+FB48 is not a composing character, so FULLY-NORMALIZED must not prepend a space.
   Being listed in CompositionExclusions.txt does not make a character a composing character.
   See F&O 3.1 section 5.4.6. :)
