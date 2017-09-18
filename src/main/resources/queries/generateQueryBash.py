# -*- coding: utf-8 -*-
"""
Spyder Editor
"""
from os import walk


#
#files= ["Where-Confusion1-300", "Where-Confusion1000-300", "Where-Confusion10000-300", 
#        "Where-Confusion100000-300", "Where-Confusion1000000-300", "Where-Confusion2000000-300", \
#       "Where-Confusion4000000-300", "Where-Confusion8000000-300", \
#       "WhereOrderBy3-Confusion1-300",  "WhereOrderBy3-Confusion1000-300", "WhereOrderBy3-Confusion10000-300", "WhereOrderBy3-Confusion100000-300", \
#"WhereOrderBy3-Confusion1000000-300", "WhereOrderBy3-Confusion2000000-300", "WhereOrderBy3-Confusion4000000-300", \
#"WhereOrderBy3-Confusion8000000-300"]

#files =["GroupOrderBy-RedditS1-500"]

#files= ["Where-Confusion1-300", "WhereOrderBy3-Confusion1-300", "WhereGroup2-Confusion1-300"]

#files = ["WhereOrderBy3-Confusion1000-300", "WhereOrderBy3-Confusion10000-300", "WhereOrderBy3-Confusion100000-300", \
#"WhereOrderBy3-Confusion1000000-300", "WhereOrderBy3-Confusion2000000-300", "WhereOrderBy3-Confusion4000000-300", \
#"WhereOrderBy3-Confusion8000000-300"]

#files = ["WhereGroupSimple-Confusion1000-300", "WhereGroupSimple-Confusion10000-300", "WhereGroupSimple-Confusion100000-300",
#         "WhereGroupSimple-Confusion1000000-300", "WhereGroupSimple-Confusion2000000-300", "WhereGroupSimple-Confusion4000000-300",
#         "WhereGroupSimple-Confusion8000000-300",  "WhereGroupSimple-Confusion1-300"]
#
#files = ["WhereGroup2-Confusion1000-300", "WhereGroup2-Confusion10000-300", "WhereGroup2-Confusion100000-300",
#         "WhereGroup2-Confusion1000000-300", "WhereGroup2-Confusion2000000-300", "WhereGroup2-Confusion4000000-300",
#         "WhereGroup2-Confusion8000000-300",  "WhereGroup2-Confusion1-300"]



#files = ["Where-RedditS1-500", "Where-RedditS600000-500", "Where-RedditS15600000-500",
#"OrderBy2-RedditS1-500", "OrderBy2-RedditS600000-500", "OrderBy2-RedditS15600000-500", "OrderBy2-RedditS31200000-500",
#"GroupOrderBy-RedditS1-500", "GroupOrderBy-RedditS600000-500", "GroupOrderBy-RedditS15600000-500", "GroupOrderBy-RedditS31200000-500"]

#files = ["Where-Confusion1-300"]

#files = ["Where-RedditS1-1000", "Where-RedditS1-300", "Where-RedditS1-500", "Where-RedditS1-750"] 
#files = ["Where-RedditS1-500", "Where-RedditS600000-500", "Where-RedditS15600000-500", "Where-RedditS31200000-500"]                                                      
 

#files = ["Where-Reddit200-3000", "Where-Reddit490-3000","Where-Reddit864-3000", "Where-Reddit1390-3000"]
#files = [ "Where-Reddit1600-3000","Where-Reddit1390-3000", "Where-Reddit864-3000",  "Where-Reddit490-3000", "Where-Reddit200-3000"]
#files = ["WhereGroupBy-Reddit1600-3000","WhereGroupBy-Reddit1390-3000", "WhereGroupBy-Reddit864-3000", "WhereGroupBy-Reddit200-3000"]                                    
#files = ["WhereGroupBy-Reddit3200-3000"]#, 
#files = ["Where-Reddit6400-15000"]
files = ["WhereGroupBy-Reddit6400-15000"]
#executors = [1,3 ,5, 10, 15, 20, 30, 40, 50, 60, 75, 100, 125]
#executors = [180, 240, 400, 500, 600]
executors=[550]

#executors = [45]

def generateBash():
    partitions = [500]
    repetitions = 3   
    for file in files:
        for executor in executors:
            for partition in partitions:
                for rep in range(repetitions):
                    print("hadoop dfs -rm -r /user/istefan/output")
        #            print("bsub -n " + str(core) + " -o delta" + str(i) + "-" + file + "-" \
        #            + str(core) + "-" + str(kernels) + ".gr mpirun ./yen ../../data/"  \
        #            + file + " " + str(25 if file.startswith("rome") else 5 if "NY" in file else 2) \
        #            + " " + str(3000 if file.startswith("rome") else (5000  if "NY" in file else 10000)) + " " \
        #            + str(kernels) + " 0 1;")
                    print('time LD_LIBRARY_PATH=/usr/hdp/2.4.3.0-227/hadoop/lib/native  spark-submit --class jiqs.Main     --master yarn-cluster ' \
                    + '--deploy-mode cluster --num-executors ' + str(executor) +   \
                    ' --conf spark.yarn.maxAppAttempts=1 --conf spark.executor.memory=10g ' + \
                    ' --conf spark.network.timeout=3600s --conf spark.speculation=true  ' + \
                    #' --conf spark.yarn.maxAppAttempts=1 ' + \
                    #' jsoniq-spark-app-1.0-jar-with-dependencies-IO.jar  no yarn-cluster ' + \
                    ' jsoniq-spark-app-1.0-jar-with-dependencies.jar  no yarn-cluster ' + \
                    ' "hdfs://dco-node153-mgt.dco.ethz.ch:8020/user/istefan/output" ' + \
                    ' "hdfs://dco-node153-mgt.dco.ethz.ch:8020/user/istefan/queries/' + \
                    str(file) +  '.iq" ' + \
                    '"hdfs://dco-node153-mgt.dco.ethz.ch:8020/user/istefan/time/LOG-' +
                    file + '-' + str(executor) + '_IT'+ str(rep) +'"')             

generateBash()

#            
#f = []
#mypath = "/home/stefan/Desktop/media_sdb1/Scoala/ETH/Big_Data/conf/c1/"
#for (dirpath, dirnames, filenames) in walk(mypath):
#    f.extend(filenames)
#    break
#HR
#print(f)
#f= ["Redditaa","Redditac","Redditae","Redditag","Redditai","Redditak","Redditam","Redditao","Redditaq","Redditas","Redditau","Redditaw","Redditay",
#"Redditab","Redditad","Redditaf","Redditah","Redditaj","Reddital","Redditan","Redditap","Redditar","Redditat","Redditav","Redditax","Redditaz"]


#print(len(f))
#for fileName in f:
#    print("curl -s -XPOST 'http://dco-node153.dco.ethz.ch:9200/redditfull2/question/_bulk' --data-binary @\"" + fileName + "\"")
    #print("split -l 100000 " + fileName+ " split/" + fileName+ "p")
    #print("sed -e 's/^/{ \"index\" : {} }\\n/' -i " + fileName)