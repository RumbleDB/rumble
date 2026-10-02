(:JIQS: ShouldParse :)
(
  {"quotes\"" : 1}."quotes\"",
  {"braces" : "{$p}"}.braces,
  "&amp;&quot;",
  "{$not-an-expression}",
  "{unbalanced",
  "brace with quote: {\"}",
  'brace with apostrophe: {\'text\'}',
  1 < 2 and "ordinary" = "ordinary"
)
