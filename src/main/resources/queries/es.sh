{ time curl -XGET 'localhost:9200/confusions1000/lang/_search' -H 'Content-Type: application/json' -d'
{
 "query": {
    "bool": {
      "must": [
        { "match": { "guess":  "Russian" }},
        { "match": { "target": "Russian"   }}
      ]
    }
  }
}
' ; } 2>> Where1000


{ time curl -XGET 'localhost:9200/confusions10000/lang/_search' -H 'Content-Type: application/json' -d'
{
 "query": {
    "bool": {
      "must": [
        { "match": { "guess":  "Russian" }},
        { "match": { "target": "Russian"   }}
      ]
    }
  }
}
' ; } 2>> Where10000


{ time curl -XGET 'localhost:9200/confusions100000/lang/_search' -H 'Content-Type: application/json' -d'
{
 "query": {
    "bool": {
      "must": [
        { "match": { "guess":  "Russian" }},
        { "match": { "target": "Russian"   }}
      ]
    }
  }
}
' ; } 2>> Where100000

{ time curl -XGET 'localhost:9200/confusions1000000/lang/_search' -H 'Content-Type: application/json' -d'
{
 "query": {
    "bool": {
      "must": [
        { "match": { "guess":  "Russian" }},
        { "match": { "target": "Russian"   }}
      ]
    }
  }
}
' ; } 2>> Where1000000

{ time curl -XGET 'localhost:9200/confusions2000000/lang/_search' -H 'Content-Type: application/json' -d'
{
 "query": {
    "bool": {
      "must": [
        { "match": { "guess":  "Russian" }},
        { "match": { "target": "Russian"   }}
      ]
    }
  }
}
' ; } 2>> Where2000000

{ time curl -XGET 'localhost:9200/confusions4000000/lang/_search' -H 'Content-Type: application/json' -d'
{
 "query": {
    "bool": {
      "must": [
        { "match": { "guess":  "Russian" }},
        { "match": { "target": "Russian"   }}
      ]
    }
  }
}
' ; } 2>> Where4000000

{ time curl -XGET 'localhost:9200/confusions8000000/lang/_search' -H 'Content-Type: application/json' -d'
{
 "query": {
    "bool": {
      "must": [
        { "match": { "guess":  "Russian" }},
        { "match": { "target": "Russian"   }}
      ]
    }
  }
}
' ; } 2>> Where8000000

{ time curl -XGET 'localhost:9200/confusions1/lang/_search' -H 'Content-Type: application/json' -d'
{
 "query": {
    "bool": {
      "must": [
        { "match": { "guess":  "Russian" }},
        { "match": { "target": "Russian"   }}
      ]
    }
  }
}
' ; } 2>> Where1




curl -XPUT 'localhost:9200/confusions2/_mapping/lang' -H 'Content-Type: application/json' -d'
{
  "properties": {
    "target": { 
      "type":     "text",
      "fielddata": true
    }
  }
}
'

curl -XPUT 'localhost:9200/confusions2/_mapping/lang' -H 'Content-Type: application/json' -d'
{
  "properties": {
    "country": { 
      "type":     "text",
      "fielddata": true
    }
  }
}
'

{ time curl -XGET 'localhost:9200/confusions1000/lang/_search?pretty' -H 'Content-Type: application/json' -d'
{
  "aggs": {
    "target": {
      "terms": {
        "field": "target"
      },
      "aggs": {
        "country": {
          "terms": {
            "field": "country"
          }        
        }
      }
    }
  }
}
' ; } 2>> Group1000

{ time curl -XGET 'localhost:9200/confusions10000/lang/_search?pretty' -H 'Content-Type: application/json' -d'
{
  "aggs": {
    "target": {
      "terms": {
        "field": "target"
      },
      "aggs": {
        "country": {
          "terms": {
            "field": "country"
          }        
        }
      }
    }
  }
}
' ; } 2>> Group10000


{ time curl -XGET 'localhost:9200/confusions100000/lang/_search?pretty' -H 'Content-Type: application/json' -d'
{
  "aggs": {
    "target": {
      "terms": {
        "field": "target"
      },
      "aggs": {
        "country": {
          "terms": {
            "field": "country"
          }        
        }
      }
    }
  }
}
' ; } 2>> Group100000

{ time curl -XGET 'localhost:9200/confusions1000000/lang/_search?pretty' -H 'Content-Type: application/json' -d'
{
  "aggs": {
    "target": {
      "terms": {
        "field": "target"
      },
      "aggs": {
        "country": {
          "terms": {
            "field": "country"
          }        
        }
      }
    }
  }
}
' ; } 2>> Group1000000


{ time curl -XGET 'localhost:9200/confusions2000000/lang/_search?pretty' -H 'Content-Type: application/json' -d'
{
  "aggs": {
    "target": {
      "terms": {
        "field": "target"
      },
      "aggs": {
        "country": {
          "terms": {
            "field": "country"
          }        
        }
      }
    }
  }
}
' ; } 2>> Group2000000


{ time curl -XGET 'localhost:9200/confusions4000000/lang/_search?pretty' -H 'Content-Type: application/json' -d'
{
  "aggs": {
    "target": {
      "terms": {
        "field": "target"
      },
      "aggs": {
        "country": {
          "terms": {
            "field": "country"
          }        
        }
      }
    }
  }
}
' ; } 2>> Group4000000

{ time curl -XGET 'localhost:9200/confusions8000000/lang/_search?pretty' -H 'Content-Type: application/json' -d'
{
  "aggs": {
    "target": {
      "terms": {
        "field": "target"
      },
      "aggs": {
        "country": {
          "terms": {
            "field": "country"
          }        
        }
      }
    }
  }
}
' ; } 2>> Group8000000


{ time curl -XGET 'localhost:9200/confusions1/lang/_search?pretty' -H 'Content-Type: application/json' -d'
{
  "aggs": {
    "target": {
      "terms": {
        "field": "target"
      },
      "aggs": {
        "country": {
          "terms": {
            "field": "country"
          }        
        }
      }
    }
  }
}
' ; } 2>> Group1

curl -XGET 'http://dco-node153.dco.ethz.ch:9200/redditaa/_search?pretty' -H 'Content-Type: application/json' -d'
{
 "query": {
    "bool": {
      "must": [
        { "match": { "subreddit":  "AskReddit" }}
      ]
    }, 
    "range" : {
              "score" : {
                  "gte" : 16
              }
          }
  }
}
'

{ time curl -XGET 'http://dco-node153.dco.ethz.ch:9200/redditaa/_search?pretty' -H 'Content-Type: application/json' -d'
{
  "from" : 0, "size" : 10000,
 "query": {
    "bool": {
      "must": [
          { "match": { "subreddit":  "AskReddit" }},
        {
          "range": {
            "score": {
              "gte": "16"
            }
          }
        }
      ]
    }
  }
}
' > OutputWhereaa ; } 2>> Whereaa



{ time curl -XGET 'http://dco-node153.dco.ethz.ch:9200/redditfull2/_search?pretty' -H 'Content-Type: application/json' -d'
{
  "from" : 0, "size" : 10000,
 "query": {
    "bool": {
      "must": [
          { "match": { "subreddit":  "AskReddit" }},
        {
          "range": {
            "score": {
              "gte": "16"
            }
          }
        }
      ]
    }
  }
}
'  ; } 2>> Wherea-all



{ time curl -XGET 'http://dco-node153.dco.ethz.ch:9200/redditaa/_search?pretty' -H 'Content-Type: application/json' -d'
{
  "from" : 0, "size" : 10000,
     "query": {
        "bool": {
          "must": [
              { "match": { "subreddit":  "AskReddit" }}
                ]
          }
      },

      "sort" : [
      { "score" : {"order" : "desc"}},
      { "author" : {"order" : "asc"}}
      ]


}
' > OutputOrderaa ; } 2>> Order1

{ time curl -XGET 'http://dco-node153.dco.ethz.ch:9200/reddita2/_search?pretty' -H 'Content-Type: application/json' -d'
{
  "from" : 0, "size" : 10000,
     "query": {
        "bool": {
          "must": [
              { "match": { "subreddit":  "AskReddit" }}
                ]
          }
      },

      "sort" : [
      { "score" : {"order" : "desc"}},
      { "author" : {"order" : "asc"}}
      ]


}
' > OutputOrdera2 ; } 2>> Order2


{ time curl -XGET 'http://dco-node153.dco.ethz.ch:9200/redditaa/_search?pretty' -H 'Content-Type: application/json' -d'
{
  "from" : 0, "size" : 10000,
     "query": {
        "bool": {
          "must": [
              { "match": { "subreddit":  "AskReddit" }}
                ]
          }
      },

      "sort" : [
      { "score" : {"order" : "desc"}},
      { "author" : {"order" : "asc"}}
      ]


}
' > OutputOrderaa ; } 2>> Orderaa

{ time curl -XGET 'http://dco-node153.dco.ethz.ch:9200/redditaa/_search?pretty' -H 'Content-Type: application/json' -d'
{
  "aggs": {
    "author": {
      "terms": {
        "field": "author"
      },
      "aggs": {
        "total_score": {
          "sum": {
            "field": "score"
          }
        }
      }
    }
  }


}
' ; } 2>> Group1


{ time curl -XGET 'http://dco-node153.dco.ethz.ch:9200/redditfull2/_search?pretty' -H 'Content-Type: application/json' -d'
{
  "from" : 0, "size" : 10000,
 "query": {
    "bool": {
      "must": [
          { "match": { "subreddit":  "AskReddit" }},
        {
          "range": {
            "score": {
              "gte": "16"
            }
          }
        }
      ]
    }
  }
}
' > OutputWhereFull ; } 2>> WhereFull


{ time curl -XGET 'http://dco-node153.dco.ethz.ch:9200/redditfull2/_search?pretty' -H 'Content-Type: application/json' -d'
{
  "from" : 0, "size" : 10000,
     "query": {
        "bool": {
          "must": [
              { "match": { "subreddit":  "AskReddit" }}
                ]
          }
      },

      "sort" : [
      { "score" : {"order" : "desc"}},
      { "author" : {"order" : "asc"}}
      ]


}
' > OutputOrderFull ; } 2>> Order3


{ time curl -XGET 'http://dco-node153.dco.ethz.ch:9200/redditfull2/_search?pretty' -H 'Content-Type: application/json' -d'
{
  "from" : 0, "size" : 10000,
  "aggs": {
    "author": {
      "terms": {
        "field": "author",
        "order" : {
          "total_score" : "asc"
        }
      },
      "aggs": {
        "total_score": {
          "sum": {
            "field": "score"
          }
        }
      }
    }
  }
}
' ; } 2>> Group3

{ time curl -XGET 'http://dco-node153.dco.ethz.ch:9200/reddita2/_search?pretty' -H 'Content-Type: application/json' -d'
{
  "from" : 0, "size" : 10000,
  "aggs": {
    "author": {
      "terms": {
        "field": "author",
        "order" : {
          "total_score" : "asc"
        }
      },
      "aggs": {
        "total_score": {
          "sum": {
            "field": "score"
          }
        }
      }
    }
  }
}
' ; } 2>> Group2


{ time curl -XGET 'http://dco-node153.dco.ethz.ch:9200/redditfull2/_search?pretty' -H 'Content-Type: application/json' -d'
{
  "from" : 0, "size" : 1000,
  "query": {
        "bool": {
      "must": [
          { "match": { "subreddit":  "AskReddit" }},
        {
          "range": {
            "score": {
              "gte": "4501"
            }
          }
        }
      ]
    }
      },
  "aggs": {
    "author": {
      "terms": {
        "field": "author",
        "order" : {
          "total_score" : "desc"
        }
      },
      "aggs": {
        "total_score": {
          "sum": {
            "field": "score"
          }
        }
      }
    }
  }
}
'  ; } 2>> Group3