
import matplotlib.pyplot as plt
import os
import numpy
from operator import itemgetter

zorbaWhere= [[0.043, 0.032, 0.032, 0.030, 0.036], \
             [0.148, 0.132, 0.138, 0.160, 0.137],
             [1.216, 1.251, 1.179, 1.389, 1.271],
             [11.122, 11.270, 10.859, 10.866, 11.550, 10.839, 11.125],
             [22.139, 23.296, 21.785, 22.137, 21.983, 21.606],
             [44.419, 44.610, 45.024, 45.164, 44.414, 44.557, 45.041],
             [92.271, 91.357, 92.656, 96.573, 91.567],
             [191.864, 184.989, 193.123, 188.194, 186.351]]
             
zorbaOrderBy =[[0.045, 0.044, 0.049, 0.044, 0.051],
               [0.314, 0.291, 0.296, 0.299, 0.293],
               [3.004, 3.053, 3.136, 3.329, 3.039],
               [32.621, 32.623, 32.789, 33.071, 32.997],
               [67.244, 68.159, 70.256, 67.912, 68.653],
               [133.759, 137.610, 137.383, 132.436, 128.079],
               [263.111, 263.020, 264.179, 264.061, 257.218]]

zorbaGroupBy = [[0.064, 0.042, 0.036, 0.042, 0.033, 0.043],
                [0.189, 0.190, 0.182, 0.178, 0.181, 0.193],
                [1.539, 1.554, 1.635, 1.543, 1.555],
                [15.101, 14.849, 15.026, 15.035, 14.970],
                [30.181, 29.779, 29.836, 30.171, 30.416],
                [60.251, 59.781, 60.073, 59.987, 58.888],
                [746.981, 722.009, 706.668, 742.227, 672.833]]






def computeStd(arr):
    time = []
    std = []
    for ar in arr:
        time.append(numpy.mean(ar))
        std.append(numpy.std(ar))
    return (time,std)
        

zorbaWhereTime, zorbaWhereStd = computeStd(zorbaWhere)
zorbaOrderByTime, zorbaOrderByStd = computeStd(zorbaOrderBy)
zorbaGroupByTime, zorbaGroupByStd = computeStd(zorbaGroupBy)

             


def breakEven(logDir, files, names, title, breakEvenSizes, oneSize, partIt = "-300-45", plotZorba = False):
    #break even analysis
    iterations = 5    
    partitions = 300
    zorbaTime = [zorbaWhereTime, zorbaOrderByTime, zorbaGroupByTime]
    zorbaErr = [zorbaWhereStd, zorbaOrderByStd, zorbaGroupByStd]
    index = 0
    for currentFile in files:
        sizes = []
        zorbaSizes = []
        times = []
        stds = []
        if index > 0:
            iterations = 5
        for size in breakEvenSizes:
            finalSize = 1    
            if size == 1:
                sizes.append(oneSize)
                if(index == 0):
                    zorbaSizes.append(oneSize)
            else:
                sizes.append(size/1000000.0)
                zorbaSizes.append(size/1000000.0)
                
                
            rawResults = []
            for iteration in range(iterations):
                filePath = logDir + 'LOG-' + currentFile + str(size) + partIt  +'_IT' + \
                            str(iteration)
                with open(filePath, "r") as ins:
                    for line in ins:
                        if line.startswith("[ExecTime]"):
                            line = line.replace("[ExecTime]", "")
                            time = float(line)
                            time = time /1000.0
                            rawResults.append(time)
            times.append(numpy.mean(rawResults))
            stds.append(numpy.std(rawResults))
            #plot
        plt.xlabel('#millions of JSON objects', fontsize=12)
        plt.ylabel('Runtime in s', fontsize=12)
        plt.tick_params(axis='both', which='major', labelsize=12)
        plt.tick_params(axis='both', which='minor', labelsize=12)
        axes = plt.gca()
        #axes.set_xlim([0,16000])
        #axes.set_ylim([0,450])
        plt.suptitle(title + names[index] + ' Query', fontsize=12)
        plt.errorbar(sizes, times, stds, marker='s' , label='jsoniq-on-spark',markersize=7)
        if plotZorba :        
            print(len(zorbaSizes))
            print(len(zorbaTime[index]))
            print(len(zorbaErr[index]))
            plt.errorbar(zorbaSizes, zorbaTime[index],zorbaErr[index] , label='Zorba Local', marker='^', markersize=9)
        legend = plt.legend(loc='upper c', shadow=True)  
        plt.grid('on')
        plt.show()
        index = index + 1
        

        
def perfromanceWithStdDeviation(logDir, fileNames, size, partitions, iterations, \
    executors, xMessage, yMessage, title):
    for currentFile in fileNames:
        units = []
        times = []
        stds = []
        for executorNumber in executors:
            units.append(executorNumber)
            filePath = logDir + 'LOG-' + currentFile + str(size) + '-' + str(partitions) \
            + '-' + str(executorNumber) + "_IT"
            rawResults = []
            for iteration in range(iterations):
                currentFilePath = filePath + str(iteration)
                with open(currentFilePath, "r") as ins:
                    for line in ins:
                        if line.startswith("[ExecTime]"):
                            line = line.replace("[ExecTime]", "")
                            time = float(line)
                            time = time /1000.0
                            rawResults.append(time)
            times.append(numpy.mean(rawResults))
            stds.append(numpy.std(rawResults))
            
                #plot
        plt.xlabel(xMessage, fontsize=12)
        plt.ylabel(yMessage, fontsize=12)
        plt.tick_params(axis='both', which='major', labelsize=12)
        plt.tick_params(axis='both', which='minor', labelsize=12)
        axes = plt.gca()
        axes.set_xlim([0,100])
        #axes.set_ylim([0,450])
        plt.suptitle(title, fontsize=12)
        plt.errorbar(units, times, yerr=stds, marker='s', label='Runtime for 16 million objects',markersize=7)
        legend = plt.legend(loc='upper c', shadow=True)  
        plt.grid('on')
        plt.show()
        
        
#'/home/stefan/Work/ETH/Thesis/benchmarks/time/num-exec-bench-2/num-executors-bench-2/time'
    
#directory = '/home/stefan/Work/ETH/Thesis/benchmarks/time/num-exec-bench-3/time2/time/'    
#files = ['Where-Confusion']
#perfromanceWithStdDeviation(directory,files,1, 300, 5, \
# [10, 15, 20,25, 30, 35,45, 50,60, 75, 100, 125, 150], "#executors", "Runtime in s", \
# "Number of executors - performance analysis")    
directory = '/home/stefan/Work/ETH/Thesis/benchmarks/time/exec-reddit/'
files = ['Where-RedditS']
perfromanceWithStdDeviation(directory,files, 1, 500, 5, \
 [10, 15, 20,30,40,50,60,75,100], "#executors", "Runtime in s", \
 "Number of executors - performance analysis")    

     
#/home/stefan/Work/ETH/Thesis/benchmarks/time/reddit-small/time
#logDir = '/home/stefan/Work/ETH/Thesis/benchmarks/time/break-even-new/time/'  
#files = ['Where-Confusion', 'WhereOrderBy3-Confusion', 'WhereGroup2-Confusion']
#names = ['WhereOnly', 'OrderBy3', 'GroupBy2']   
#    breakEvenSizes = [1000,10000,100000, 1000000, 2000000, 4000000, 8000000, 1]#, 1]

logDir = '/home/stefan/Work/ETH/Thesis/benchmarks/time/break-even-reddit/'
files = ['Where-RedditS', 'OrderBy2-RedditS', 'GroupOrderBy-RedditS']
names = ['WhereOnly', 'OrderBy2', 'GroupBy-OrderBy']
breakEvenSizes = [600000, 15600000, 31200000, 1]#, 1]     

breakEven(logDir, files, names, "Break-Even Point ", breakEvenSizes, 52, "-500-75")
   
 
sparsonicOrderBy1000 = [[12.584, 13.008, 12.852, 12.813, 13.079],
                        [34.229, 34.700, 44.160, 43.271, 32.516, 32.506],
                        [90.306, 87.905, 90.842, 81.364]]
                        
sparsonicGroupBy1000 = [[9.229, 9.368, 8.787,  9.035],
                        [54.136, 56.288, 55.328, 57.359],
                        [126.003, 129.481, 135.371, 129.559 ]]
                        
esOrderNoCache = [[0.283, 0.374, 0.324, 0.291, 0.327],
                  [1.501, 1.790, 1.487, 1.575, 1.436],
                  [5.511, 5.293, 5.878, 5.164, 5.382]
                  ]
                  
esGroupNoCache = [[0.433, 0.413, 0.436, 0.453, 0.405],
                  [2.338, 2.520, 2.518, 2.411, 2.565],
                  [7.049, 6.718, 7.240, 6.553, 6.928]]
                  
                  
esGroupNoCacheFilter = [[0.181, 0.19, 0.175, 0.167, 0.175],
                  [2.093, 2.333, 2.086, 2.232, 2.108],
                  [6.131, 6.625, 6.723, 6.410, 6.223]]
                  
sparsonicGroupByFilter1000 = [[5.18, 5.475, 5.997, 5.903, 6.223],
                        [12.823, 12.274, 11.249, 12.459, 12.365],
                        [36.211, 39.091, 36.876, 38.381, 36.831 ]]

#print(computeStd(sparsonicOrderBy1000))

def baseline(pair, title, sizes):
    #plot
    firstValues, firstStd = computeStd(pair[0])
    secondValues, secondStd = computeStd(pair[1])
    plt.xlabel('#millions of JSON objects', fontsize=12)
    plt.ylabel('Runtime in s', fontsize=12)
    plt.tick_params(axis='both', which='major', labelsize=12)
    plt.tick_params(axis='both', which='minor', labelsize=12)
    axes = plt.gca()
    #axes.set_xlim([0,16000])
    #axes.set_ylim([0,450])
    plt.suptitle(title, fontsize=12)
    plt.errorbar(sizes, firstValues, firstStd, marker='s' , label='jsoniq-on-spark',markersize=7)
    plt.errorbar(sizes, secondValues, secondStd , label='Elasticsearch (no cache)', marker='^', markersize=9)
    legend = plt.legend(loc='upper c', shadow=True)  
    plt.grid('on')
    plt.show()


baseline([sparsonicOrderBy1000, esOrderNoCache], "Order-By-2 Comparison", [0.6, 15, 53])
baseline([sparsonicGroupBy1000, esGroupNoCache], "Group-By-Order-by Comparison", [0.6, 15, 53])
baseline([sparsonicGroupByFilter1000, esGroupNoCacheFilter], "High-Filter Query Comparison", [0.6, 15, 53])