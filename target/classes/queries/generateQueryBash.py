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


#files = ["Where-RedditS1-300", "Where-RedditS600000-300", "Where-RedditS15600000-300"]
#files =["OrderBy2-RedditS1-500", "OrderBy2-RedditS600000-500", "OrderBy2-RedditS15600000-500", "OrderBy2-RedditS31200000-500"]
#files = ["GroupOrderBy-RedditS1-500", "GroupOrderBy-RedditS600000-500", "GroupOrderBy-RedditS15600000-500", "GroupOrderBy-RedditS31200000-500"]

files = ["GroupHighFilterRedditS"]

#files = ["Where-RedditS1-1000", "Where-RedditS1-300", "Where-RedditS1-500", "Where-RedditS1-750"] 
#files = ["Where-RedditS1-500", "Where-RedditS600000-500", "Where-RedditS15600000-500", "Where-RedditS31200000-500"]                                                      
                                       
#executors = [10, 15, 20, 30, 40, 50, 60, 75, 100, 125]
#executors = [30]


executors = [75]

def generateBash():
    partitions = [500]
    repetitions = 2    
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


#f = ["Redditaapaa","Redditacpac","Redditaepae","Redditahpab","Redditajpad","Redditampaa","Redditaopac","Redditaqpae","Redditatpab","Redditavpad","Redditaypaa",
#"Redditaapab","Redditacpad","Redditafpaa","Redditahpac","Redditajpae","Redditampab","Redditaopad","Redditarpaa","Redditatpac","Redditavpae","Redditaypab",
#"Redditaapac","Redditacpae","Redditafpab","Redditahpad","Redditakpaa","Redditampac","Redditaopae","Redditarpab","Redditatpad","Redditawpaa","Redditaypac",
#"Redditaapad","Redditadpaa","Redditafpac","Redditahpae","Redditakpab","Redditampad","Redditappaa","Redditarpac","Redditatpae","Redditawpab","Redditaypad",
#"Redditaapae","Redditadpab","Redditafpad","Redditaipaa","Redditakpac","Redditampae","Redditappab","Redditarpad","Redditaupaa","Redditawpac","Redditaypae",
#"Redditabpaa","Redditadpac","Redditafpae","Redditaipab","Redditakpad","Redditanpaa","Redditappac","Redditarpae","Redditaupab","Redditawpad","Redditazpaa",
#"Redditabpab","Redditadpad","Redditagpaa","Redditaipac","Redditakpae","Redditanpab","Redditappad","Redditaspaa","Redditaupac","Redditawpae","Redditazpab",
#"Redditabpac","Redditadpae","Redditagpab","Redditaipad","Redditalpaa","Redditanpac","Redditappae","Redditaspab","Redditaupad","Redditaxpaa","Redditazpac",
#"Redditabpad","Redditaepaa","Redditagpac","Redditaipae","Redditalpab","Redditanpad","Redditaqpaa","Redditaspac","Redditaupae","Redditaxpab","Redditazpad",
#"Redditabpae","Redditaepab","Redditagpad","Redditajpaa","Redditalpac","Redditanpae","Redditaqpab","Redditaspad","Redditavpaa","Redditaxpac","Redditazpae",
#"Redditacpaa","Redditaepac","Redditagpae","Redditajpab","Redditalpad","Redditaopaa","Redditaqpac","Redditaspae","Redditavpab","Redditaxpad",
#"Redditacpab","Redditaepad","Redditahpaa","Redditajpac","Redditalpae","Redditaopab","Redditaqpad","Redditatpaa","Redditavpac","Redditaxpae"]

#
#f= ["Redditaa","Redditam","Redditay","Redditbk","Redditbw","Redditci","Redditcu","Redditdg",
#"Redditab","Redditan","Redditaz","Redditbl","Redditbx","Redditcj","Redditcv","Redditdh",
#"Redditac","Redditao","Redditba","Redditbm","Redditby","Redditck","Redditcw","Redditdi",
#"Redditad","Redditap","Redditbb","Redditbn","Redditbz","Redditcl","Redditcx","Redditdj",
#"Redditae","Redditaq","Redditbc","Redditbo","Redditca","Redditcm","Redditcy","Redditdk",
#"Redditaf","Redditar","Redditbd","Redditbp","Redditcb","Redditcn","Redditcz","Redditdl",
#"Redditag","Redditas","Redditbe","Redditbq","Redditcc","Redditco","Redditda",
#"Redditah","Redditat","Redditbf","Redditbr","Redditcd","Redditcp","Redditdb",
#"Redditai","Redditau","Redditbg","Redditbs","Redditce","Redditcq","Redditdc",
#"Redditaj","Redditav","Redditbh","Redditbt","Redditcf","Redditcr","Redditdd",
#"Redditak","Redditaw","Redditbi","Redditbu","Redditcg","Redditcs","Redditde",
#"Reddital","Redditax","Redditbj","Redditbv","Redditch","Redditct","Redditdf"
#]


f=["Redditaapaa","Redditajpaa","Redditaspaa","Redditbbpaa","Redditbkpaa","Redditbtpaa","Redditccpaa","Redditclpaa","Redditcupaa","Redditddpaa",
"Redditaapab","Redditajpab","Redditaspab","Redditbbpab","Redditbkpab","Redditbtpab","Redditccpab","Redditclpab","Redditcupab","Redditddpab",
"Redditaapac","Redditajpac","Redditaspac","Redditbbpac","Redditbkpac","Redditbtpac","Redditccpac","Redditclpac","Redditcupac","Redditddpac",
"Redditaapad","Redditajpad","Redditaspad","Redditbbpad","Redditbkpad","Redditbtpad","Redditccpad","Redditclpad","Redditcupad","Redditddpad",
"Redditaapae","Redditajpae","Redditaspae","Redditbbpae","Redditbkpae","Redditbtpae","Redditccpae","Redditclpae","Redditcupae","Redditddpae",
"Redditaapaf","Redditajpaf","Redditaspaf","Redditbbpaf","Redditbkpaf","Redditbtpaf","Redditccpaf","Redditclpaf","Redditcupaf","Redditddpaf",
"Redditabpaa","Redditakpaa","Redditatpaa","Redditbcpaa","Redditblpaa","Redditbupaa","Redditcdpaa","Redditcmpaa","Redditcvpaa","Redditdepaa",
"Redditabpab","Redditakpab","Redditatpab","Redditbcpab","Redditblpab","Redditbupab","Redditcdpab","Redditcmpab","Redditcvpab","Redditdepab",
"Redditabpac","Redditakpac","Redditatpac","Redditbcpac","Redditblpac","Redditbupac","Redditcdpac","Redditcmpac","Redditcvpac","Redditdepac",
"Redditabpad","Redditakpad","Redditatpad","Redditbcpad","Redditblpad","Redditbupad","Redditcdpad","Redditcmpad","Redditcvpad","Redditdepad",
"Redditabpae","Redditakpae","Redditatpae","Redditbcpae","Redditblpae","Redditbupae","Redditcdpae","Redditcmpae","Redditcvpae","Redditdepae",
"Redditabpaf","Redditakpaf","Redditatpaf","Redditbcpaf","Redditblpaf","Redditbupaf","Redditcdpaf","Redditcmpaf","Redditcvpaf","Redditdepaf",
"Redditacpaa","Redditalpaa","Redditaupaa","Redditbdpaa","Redditbmpaa","Redditbvpaa","Redditcepaa","Redditcnpaa","Redditcwpaa","Redditdfpaa",
"Redditacpab","Redditalpab","Redditaupab","Redditbdpab","Redditbmpab","Redditbvpab","Redditcepab","Redditcnpab","Redditcwpab","Redditdfpab",
"Redditacpac","Redditalpac","Redditaupac","Redditbdpac","Redditbmpac","Redditbvpac","Redditcepac","Redditcnpac","Redditcwpac","Redditdfpac",
"Redditacpad","Redditalpad","Redditaupad","Redditbdpad","Redditbmpad","Redditbvpad","Redditcepad","Redditcnpad","Redditcwpad","Redditdfpad",
"Redditacpae","Redditalpae","Redditaupae","Redditbdpae","Redditbmpae","Redditbvpae","Redditcepae","Redditcnpae","Redditcwpae","Redditdfpae",
"Redditacpaf","Redditalpaf","Redditaupaf","Redditbdpaf","Redditbmpaf","Redditbvpaf","Redditcepaf","Redditcnpaf","Redditcwpaf","Redditdfpaf",
"Redditadpaa","Redditampaa","Redditavpaa","Redditbepaa","Redditbnpaa","Redditbwpaa","Redditcfpaa","Redditcopaa","Redditcxpaa","Redditdgpaa",
"Redditadpab","Redditampab","Redditavpab","Redditbepab","Redditbnpab","Redditbwpab","Redditcfpab","Redditcopab","Redditcxpab","Redditdgpab",
"Redditadpac","Redditampac","Redditavpac","Redditbepac","Redditbnpac","Redditbwpac","Redditcfpac","Redditcopac","Redditcxpac","Redditdgpac",
"Redditadpad","Redditampad","Redditavpad","Redditbepad","Redditbnpad","Redditbwpad","Redditcfpad","Redditcopad","Redditcxpad","Redditdgpad",
"Redditadpae","Redditampae","Redditavpae","Redditbepae","Redditbnpae","Redditbwpae","Redditcfpae","Redditcopae","Redditcxpae","Redditdgpae",
"Redditadpaf","Redditampaf","Redditavpaf","Redditbepaf","Redditbnpaf","Redditbwpaf","Redditcfpaf","Redditcopaf","Redditcxpaf","Redditdgpaf",
"Redditaepaa","Redditanpaa","Redditawpaa","Redditbfpaa","Redditbopaa","Redditbxpaa","Redditcgpaa","Redditcppaa","Redditcypaa","Redditdhpaa",
"Redditaepab","Redditanpab","Redditawpab","Redditbfpab","Redditbopab","Redditbxpab","Redditcgpab","Redditcppab","Redditcypab","Redditdhpab",
"Redditaepac","Redditanpac","Redditawpac","Redditbfpac","Redditbopac","Redditbxpac","Redditcgpac","Redditcppac","Redditcypac","Redditdhpac",
"Redditaepad","Redditanpad","Redditawpad","Redditbfpad","Redditbopad","Redditbxpad","Redditcgpad","Redditcppad","Redditcypad","Redditdhpad",
"Redditaepae","Redditanpae","Redditawpae","Redditbfpae","Redditbopae","Redditbxpae","Redditcgpae","Redditcppae","Redditcypae","Redditdhpae",
"Redditaepaf","Redditanpaf","Redditawpaf","Redditbfpaf","Redditbopaf","Redditbxpaf","Redditcgpaf","Redditcppaf","Redditcypaf","Redditdhpaf",
"Redditafpaa","Redditaopaa","Redditaxpaa","Redditbgpaa","Redditbppaa","Redditbypaa","Redditchpaa","Redditcqpaa","Redditczpaa","Redditdipaa",
"Redditafpab","Redditaopab","Redditaxpab","Redditbgpab","Redditbppab","Redditbypab","Redditchpab","Redditcqpab","Redditczpab","Redditdipab",
"Redditafpac","Redditaopac","Redditaxpac","Redditbgpac","Redditbppac","Redditbypac","Redditchpac","Redditcqpac","Redditczpac","Redditdipac",
"Redditafpad","Redditaopad","Redditaxpad","Redditbgpad","Redditbppad","Redditbypad","Redditchpad","Redditcqpad","Redditczpad","Redditdipad",
"Redditafpae","Redditaopae","Redditaxpae","Redditbgpae","Redditbppae","Redditbypae","Redditchpae","Redditcqpae","Redditczpae","Redditdipae",
"Redditafpaf","Redditaopaf","Redditaxpaf","Redditbgpaf","Redditbppaf","Redditbypaf","Redditchpaf","Redditcqpaf","Redditczpaf","Redditdipaf",
"Redditagpaa","Redditappaa","Redditaypaa","Redditbhpaa","Redditbqpaa","Redditbzpaa","Redditcipaa","Redditcrpaa","Redditdapaa","Redditdjpaa",
"Redditagpab","Redditappab","Redditaypab","Redditbhpab","Redditbqpab","Redditbzpab","Redditcipab","Redditcrpab","Redditdapab","Redditdjpab",
"Redditagpac","Redditappac","Redditaypac","Redditbhpac","Redditbqpac","Redditbzpac","Redditcipac","Redditcrpac","Redditdapac","Redditdjpac",
"Redditagpad","Redditappad","Redditaypad","Redditbhpad","Redditbqpad","Redditbzpad","Redditcipad","Redditcrpad","Redditdapad","Redditdjpad",
"Redditagpae","Redditappae","Redditaypae","Redditbhpae","Redditbqpae","Redditbzpae","Redditcipae","Redditcrpae","Redditdapae","Redditdjpae",
"Redditagpaf","Redditappaf","Redditaypaf","Redditbhpaf","Redditbqpaf","Redditbzpaf","Redditcipaf","Redditcrpaf","Redditdapaf","Redditdjpaf",
"Redditahpaa","Redditaqpaa","Redditazpaa","Redditbipaa","Redditbrpaa","Redditcapaa","Redditcjpaa","Redditcspaa","Redditdbpaa","Redditdkpaa",
"Redditahpab","Redditaqpab","Redditazpab","Redditbipab","Redditbrpab","Redditcapab","Redditcjpab","Redditcspab","Redditdbpab","Redditdkpab",
"Redditahpac","Redditaqpac","Redditazpac","Redditbipac","Redditbrpac","Redditcapac","Redditcjpac","Redditcspac","Redditdbpac","Redditdkpac",
"Redditahpad","Redditaqpad","Redditazpad","Redditbipad","Redditbrpad","Redditcapad","Redditcjpad","Redditcspad","Redditdbpad","Redditdkpad",
"Redditahpae","Redditaqpae","Redditazpae","Redditbipae","Redditbrpae","Redditcapae","Redditcjpae","Redditcspae","Redditdbpae","Redditdkpae",
"Redditahpaf","Redditaqpaf","Redditazpaf","Redditbipaf","Redditbrpaf","Redditcapaf","Redditcjpaf","Redditcspaf","Redditdbpaf","Redditdkpaf",
"Redditaipaa","Redditarpaa","Redditbapaa","Redditbjpaa","Redditbspaa","Redditcbpaa","Redditckpaa","Redditctpaa","Redditdcpaa","Redditdlpaa",
"Redditaipab","Redditarpab","Redditbapab","Redditbjpab","Redditbspab","Redditcbpab","Redditckpab","Redditctpab","Redditdcpab","Redditdlpab",
"Redditaipac","Redditarpac","Redditbapac","Redditbjpac","Redditbspac","Redditcbpac","Redditckpac","Redditctpac","Redditdcpac","Redditdlpac",
"Redditaipad","Redditarpad","Redditbapad","Redditbjpad","Redditbspad","Redditcbpad","Redditckpad","Redditctpad","Redditdcpad","Redditdlpad",
"Redditaipae","Redditarpae","Redditbapae","Redditbjpae","Redditbspae","Redditcbpae","Redditckpae","Redditctpae","Redditdcpae","Redditdlpae",
"Redditaipaf","Redditarpaf","Redditbapaf","Redditbjpaf","Redditbspaf","Redditcbpaf","Redditckpaf","Redditctpaf","Redditdcpaf"]

#print(len(f))
#for fileName in f:
#    print("curl -s -XPOST 'http://dco-node153.dco.ethz.ch:9200/redditfull2/question/_bulk' --data-binary @\"" + fileName + "\"")
    #print("split -l 100000 " + fileName+ " split/" + fileName+ "p")
    #print("sed -e 's/^/{ \"index\" : {} }\\n/' -i " + fileName)