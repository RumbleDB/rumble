(:JIQS: ShouldRun; Output="true":)
xml-to-json(
  <map xmlns="http://www.w3.org/2005/xpath-functions">
    <string escaped-key="true" key="\t">tab</string>
  </map>
) eq '{"\t":"tab"}'
