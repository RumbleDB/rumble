(:JIQS: ShouldRun; Output="(test1, test2, test3, test4, test5)" :)
normalize-unicode("test1", "NFC")
normalize-unicode("test2", "NFD")
normalize-unicode("test3", "NFKC")
normalize-unicode("test4", "NFKD")
normalize-unicode("test5", "FULLY-NORMALIZED")

(: general tests :)
