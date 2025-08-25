(:JIQS: ShouldCompile :)
declare %nonsequential function foo() {
    exit returning 1;
 };

(: declared nonsequential with exit statement :)