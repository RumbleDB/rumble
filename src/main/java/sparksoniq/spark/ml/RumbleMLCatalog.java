package sparksoniq.spark.ml;

import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.UnrecognizedRumbleMLClassReferenceException;
import org.rumbledb.exceptions.UnrecognizedRumbleMLParamReferenceException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Maps within these catalog are auto generated (also the hashmap.put(...) statements) with the SparkML scraper python
 * tool
 */
public class RumbleMLCatalog {
    private static final HashMap<String, String> estimatorFullClassNames;
    private static final HashMap<String, String> transformerFullClassNames;

    private static final HashMap<String, String> paramJavaTypeNames;

    private static final HashMap<String, List<String>> estimatorParams;
    private static final HashMap<String, List<String>> transformerParams;

    static {
        estimatorFullClassNames = new HashMap<>();
        transformerFullClassNames = new HashMap<>();

        estimatorFullClassNames.put("RandomForestRegressor", "org.apache.spark.ml.regression.RandomForestRegressor");
        estimatorFullClassNames.put("ALS", "org.apache.spark.ml.recommendation.ALS");
        estimatorFullClassNames.put("FPGrowth", "org.apache.spark.ml.fpm.FPGrowth");
        estimatorFullClassNames.put("TrainValidationSplit", "org.apache.spark.ml.tuning.TrainValidationSplit");
        estimatorFullClassNames.put("CrossValidator", "org.apache.spark.ml.tuning.CrossValidator");
        estimatorFullClassNames.put("LinearRegression", "org.apache.spark.ml.regression.LinearRegression");
        estimatorFullClassNames.put("IsotonicRegression", "org.apache.spark.ml.regression.IsotonicRegression");
        estimatorFullClassNames.put(
            "GeneralizedLinearRegression",
            "org.apache.spark.ml.regression.GeneralizedLinearRegression"
        );
        estimatorFullClassNames.put("GBTRegressor", "org.apache.spark.ml.regression.GBTRegressor");
        estimatorFullClassNames.put("DecisionTreeRegressor", "org.apache.spark.ml.regression.DecisionTreeRegressor");
        estimatorFullClassNames.put("AFTSurvivalRegression", "org.apache.spark.ml.regression.AFTSurvivalRegression");
        estimatorFullClassNames.put("KMeans", "org.apache.spark.ml.clustering.KMeans");
        estimatorFullClassNames.put("LDA", "org.apache.spark.ml.clustering.LDA");
        estimatorFullClassNames.put("GaussianMixture", "org.apache.spark.ml.clustering.GaussianMixture");
        estimatorFullClassNames.put("Pipeline", "org.apache.spark.ml.Pipeline");
        estimatorFullClassNames.put(
            "RandomForestClassifier",
            "org.apache.spark.ml.classification.RandomForestClassifier"
        );
        estimatorFullClassNames.put("OneVsRest", "org.apache.spark.ml.classification.OneVsRest");
        estimatorFullClassNames.put("NaiveBayes", "org.apache.spark.ml.classification.NaiveBayes");
        estimatorFullClassNames.put(
            "MultilayerPerceptronClassifier",
            "org.apache.spark.ml.classification.MultilayerPerceptronClassifier"
        );
        estimatorFullClassNames.put("LinearSVC", "org.apache.spark.ml.classification.LinearSVC");
        estimatorFullClassNames.put("GBTClassifier", "org.apache.spark.ml.classification.GBTClassifier");
        estimatorFullClassNames.put(
            "DecisionTreeClassifier",
            "org.apache.spark.ml.classification.DecisionTreeClassifier"
        );
        estimatorFullClassNames.put("LogisticRegression", "org.apache.spark.ml.classification.LogisticRegression");
        estimatorFullClassNames.put("Word2Vec", "org.apache.spark.ml.feature.Word2Vec");
        estimatorFullClassNames.put("VectorIndexer", "org.apache.spark.ml.feature.VectorIndexer");
        estimatorFullClassNames.put("StringIndexer", "org.apache.spark.ml.feature.StringIndexer");
        estimatorFullClassNames.put("StandardScaler", "org.apache.spark.ml.feature.StandardScaler");
        estimatorFullClassNames.put("RFormula", "org.apache.spark.ml.feature.RFormula");
        estimatorFullClassNames.put("QuantileDiscretizer", "org.apache.spark.ml.feature.QuantileDiscretizer");
        estimatorFullClassNames.put("BisectingKMeans", "org.apache.spark.ml.clustering.BisectingKMeans");
        estimatorFullClassNames.put("PCA", "org.apache.spark.ml.feature.PCA");
        estimatorFullClassNames.put("OneHotEncoderEstimator", "org.apache.spark.ml.feature.OneHotEncoderEstimator");
        estimatorFullClassNames.put("MinMaxScaler", "org.apache.spark.ml.feature.MinMaxScaler");
        estimatorFullClassNames.put("MinHashLSH", "org.apache.spark.ml.feature.MinHashLSH");
        estimatorFullClassNames.put("MaxAbsScaler", "org.apache.spark.ml.feature.MaxAbsScaler");
        estimatorFullClassNames.put("Imputer", "org.apache.spark.ml.feature.Imputer");
        estimatorFullClassNames.put("IDF", "org.apache.spark.ml.feature.IDF");
        estimatorFullClassNames.put("CountVectorizer", "org.apache.spark.ml.feature.CountVectorizer");
        estimatorFullClassNames.put("ChiSqSelector", "org.apache.spark.ml.feature.ChiSqSelector");
        estimatorFullClassNames.put(
            "BucketedRandomProjectionLSH",
            "org.apache.spark.ml.feature.BucketedRandomProjectionLSH"
        );

        transformerFullClassNames.put("ALSModel", "org.apache.spark.ml.recommendation.ALSModel");
        transformerFullClassNames.put(
            "RandomForestRegressionModel",
            "org.apache.spark.ml.regression.RandomForestRegressionModel"
        );
        transformerFullClassNames.put("FPGrowthModel", "org.apache.spark.ml.fpm.FPGrowthModel");
        transformerFullClassNames.put(
            "TrainValidationSplitModel",
            "org.apache.spark.ml.tuning.TrainValidationSplitModel"
        );
        transformerFullClassNames.put("CrossValidatorModel", "org.apache.spark.ml.tuning.CrossValidatorModel");
        transformerFullClassNames.put(
            "IsotonicRegressionModel",
            "org.apache.spark.ml.regression.IsotonicRegressionModel"
        );
        transformerFullClassNames.put("LinearRegressionModel", "org.apache.spark.ml.regression.LinearRegressionModel");
        transformerFullClassNames.put(
            "GeneralizedLinearRegressionModel",
            "org.apache.spark.ml.regression.GeneralizedLinearRegressionModel"
        );
        transformerFullClassNames.put("GBTRegressionModel", "org.apache.spark.ml.regression.GBTRegressionModel");
        transformerFullClassNames.put(
            "AFTSurvivalRegressionModel",
            "org.apache.spark.ml.regression.AFTSurvivalRegressionModel"
        );
        transformerFullClassNames.put(
            "DecisionTreeRegressionModel",
            "org.apache.spark.ml.regression.DecisionTreeRegressionModel"
        );
        transformerFullClassNames.put("KMeansModel", "org.apache.spark.ml.clustering.KMeansModel");
        transformerFullClassNames.put("LocalLDAModel", "org.apache.spark.ml.clustering.LocalLDAModel");
        transformerFullClassNames.put("GaussianMixtureModel", "org.apache.spark.ml.clustering.GaussianMixtureModel");
        transformerFullClassNames.put("DistributedLDAModel", "org.apache.spark.ml.clustering.DistributedLDAModel");
        transformerFullClassNames.put("PipelineModel", "org.apache.spark.ml.PipelineModel");
        transformerFullClassNames.put(
            "RandomForestClassificationModel",
            "org.apache.spark.ml.classification.RandomForestClassificationModel"
        );
        transformerFullClassNames.put("OneVsRestModel", "org.apache.spark.ml.classification.OneVsRestModel");
        transformerFullClassNames.put("NaiveBayesModel", "org.apache.spark.ml.classification.NaiveBayesModel");
        transformerFullClassNames.put(
            "MultilayerPerceptronClassificationModel",
            "org.apache.spark.ml.classification.MultilayerPerceptronClassificationModel"
        );
        transformerFullClassNames.put(
            "LogisticRegressionModel",
            "org.apache.spark.ml.classification.LogisticRegressionModel"
        );
        transformerFullClassNames.put("LinearSVCModel", "org.apache.spark.ml.classification.LinearSVCModel");
        transformerFullClassNames.put(
            "GBTClassificationModel",
            "org.apache.spark.ml.classification.GBTClassificationModel"
        );
        transformerFullClassNames.put(
            "DecisionTreeClassificationModel",
            "org.apache.spark.ml.classification.DecisionTreeClassificationModel"
        );
        transformerFullClassNames.put("Word2VecModel", "org.apache.spark.ml.feature.Word2VecModel");
        transformerFullClassNames.put("VectorSlicer", "org.apache.spark.ml.feature.VectorSlicer");
        transformerFullClassNames.put("VectorSizeHint", "org.apache.spark.ml.feature.VectorSizeHint");
        transformerFullClassNames.put("VectorIndexerModel", "org.apache.spark.ml.feature.VectorIndexerModel");
        transformerFullClassNames.put("VectorAttributeRewriter", "org.apache.spark.ml.feature.VectorAttributeRewriter");
        transformerFullClassNames.put("VectorAssembler", "org.apache.spark.ml.feature.VectorAssembler");
        transformerFullClassNames.put("Tokenizer", "org.apache.spark.ml.feature.Tokenizer");
        transformerFullClassNames.put("StringIndexerModel", "org.apache.spark.ml.feature.StringIndexerModel");
        transformerFullClassNames.put("StopWordsRemover", "org.apache.spark.ml.feature.StopWordsRemover");
        transformerFullClassNames.put("StandardScalerModel", "org.apache.spark.ml.feature.StandardScalerModel");
        transformerFullClassNames.put("SQLTransformer", "org.apache.spark.ml.feature.SQLTransformer");
        transformerFullClassNames.put("RFormulaModel", "org.apache.spark.ml.feature.RFormulaModel");
        transformerFullClassNames.put("RegexTokenizer", "org.apache.spark.ml.feature.RegexTokenizer");
        transformerFullClassNames.put("PolynomialExpansion", "org.apache.spark.ml.feature.PolynomialExpansion");
        transformerFullClassNames.put("PCAModel", "org.apache.spark.ml.feature.PCAModel");
        transformerFullClassNames.put("BisectingKMeansModel", "org.apache.spark.ml.clustering.BisectingKMeansModel");
        transformerFullClassNames.put("OneHotEncoderModel", "org.apache.spark.ml.feature.OneHotEncoderModel");
        transformerFullClassNames.put("OneHotEncoder", "org.apache.spark.ml.feature.OneHotEncoder");
        transformerFullClassNames.put("Normalizer", "org.apache.spark.ml.feature.Normalizer");
        transformerFullClassNames.put("NGram", "org.apache.spark.ml.feature.NGram");
        transformerFullClassNames.put("MinMaxScalerModel", "org.apache.spark.ml.feature.MinMaxScalerModel");
        transformerFullClassNames.put("MinHashLSHModel", "org.apache.spark.ml.feature.MinHashLSHModel");
        transformerFullClassNames.put("MaxAbsScalerModel", "org.apache.spark.ml.feature.MaxAbsScalerModel");
        transformerFullClassNames.put("Interaction", "org.apache.spark.ml.feature.Interaction");
        transformerFullClassNames.put("IndexToString", "org.apache.spark.ml.feature.IndexToString");
        transformerFullClassNames.put("ImputerModel", "org.apache.spark.ml.feature.ImputerModel");
        transformerFullClassNames.put("IDFModel", "org.apache.spark.ml.feature.IDFModel");
        transformerFullClassNames.put("HashingTF", "org.apache.spark.ml.feature.HashingTF");
        transformerFullClassNames.put("FeatureHasher", "org.apache.spark.ml.feature.FeatureHasher");
        transformerFullClassNames.put("ElementwiseProduct", "org.apache.spark.ml.feature.ElementwiseProduct");
        transformerFullClassNames.put("DCT", "org.apache.spark.ml.feature.DCT");
        transformerFullClassNames.put("CountVectorizerModel", "org.apache.spark.ml.feature.CountVectorizerModel");
        transformerFullClassNames.put("ColumnPruner", "org.apache.spark.ml.feature.ColumnPruner");
        transformerFullClassNames.put("ChiSqSelectorModel", "org.apache.spark.ml.feature.ChiSqSelectorModel");
        transformerFullClassNames.put("Bucketizer", "org.apache.spark.ml.feature.Bucketizer");
        transformerFullClassNames.put(
            "BucketedRandomProjectionLSHModel",
            "org.apache.spark.ml.feature.BucketedRandomProjectionLSHModel"
        );
        transformerFullClassNames.put("Binarizer", "org.apache.spark.ml.feature.Binarizer");
    }

    static {
        paramJavaTypeNames = new HashMap<>();

        paramJavaTypeNames.put("inputCol", "String");
        paramJavaTypeNames.put("outputCol", "String");
        paramJavaTypeNames.put("parent", "Estimator<M>");
        paramJavaTypeNames.put("numHashTables", "int");
        paramJavaTypeNames.put("seed", "long");
        paramJavaTypeNames.put("inputCols", "String[]");
        paramJavaTypeNames.put("labels", "String[]");
        paramJavaTypeNames.put("collectSubModels", "boolean");
        paramJavaTypeNames.put("estimator", "Estimator<?>");
        paramJavaTypeNames.put("estimatorParamMaps", "ParamMap[]");
        paramJavaTypeNames.put("evaluator", "Evaluator");
        paramJavaTypeNames.put("parallelism", "int");
        paramJavaTypeNames.put("trainRatio", "double");
        paramJavaTypeNames.put("numFolds", "int");
        paramJavaTypeNames.put("maxIter", "int");
        paramJavaTypeNames.put("maxSentenceLength", "int");
        paramJavaTypeNames.put("minCount", "int");
        paramJavaTypeNames.put("numPartitions", "int");
        paramJavaTypeNames.put("stepSize", "double");
        paramJavaTypeNames.put("vectorSize", "int");
        paramJavaTypeNames.put("windowSize", "int");
        paramJavaTypeNames.put("indices", "int[]");
        paramJavaTypeNames.put("names", "String[]");
        paramJavaTypeNames.put("handleInvalid", "String");
        paramJavaTypeNames.put("size", "int");
        paramJavaTypeNames.put("maxCategories", "int");
        paramJavaTypeNames.put("stringOrderType", "String");
        paramJavaTypeNames.put("caseSensitive", "boolean");
        paramJavaTypeNames.put("locale", "String");
        paramJavaTypeNames.put("stopWords", "String[]");
        paramJavaTypeNames.put("withMean", "boolean");
        paramJavaTypeNames.put("withStd", "boolean");
        paramJavaTypeNames.put("statement", "String");
        paramJavaTypeNames.put("featuresCol", "String");
        paramJavaTypeNames.put("forceIndexLabel", "boolean");
        paramJavaTypeNames.put("formula", "String");
        paramJavaTypeNames.put("labelCol", "String");
        paramJavaTypeNames.put("stringIndexerOrderType", "String");
        paramJavaTypeNames.put("gaps", "boolean");
        paramJavaTypeNames.put("minTokenLength", "int");
        paramJavaTypeNames.put("pattern", "String");
        paramJavaTypeNames.put("toLowercase", "boolean");
        paramJavaTypeNames.put("numBuckets", "int");
        paramJavaTypeNames.put("numBucketsArray", "int[]");
        paramJavaTypeNames.put("outputCols", "String[]");
        paramJavaTypeNames.put("relativeError", "double");
        paramJavaTypeNames.put("degree", "int");
        paramJavaTypeNames.put("k", "int");
        paramJavaTypeNames.put("dropLast", "boolean");
        paramJavaTypeNames.put("p", "double");
        paramJavaTypeNames.put("n", "int");
        paramJavaTypeNames.put("max", "double");
        paramJavaTypeNames.put("min", "double");
        paramJavaTypeNames.put("missingValue", "double");
        paramJavaTypeNames.put("strategy", "String");
        paramJavaTypeNames.put("minDocFreq", "int");
        paramJavaTypeNames.put("binary", "boolean");
        paramJavaTypeNames.put("numFeatures", "int");
        paramJavaTypeNames.put("scalingVec", "Vector");
        paramJavaTypeNames.put("inverse", "boolean");
        paramJavaTypeNames.put("minTF", "double");
        paramJavaTypeNames.put("categoricalCols", "String[]");
        paramJavaTypeNames.put("maxDF", "double");
        paramJavaTypeNames.put("minDF", "double");
        paramJavaTypeNames.put("vocabSize", "int");
        paramJavaTypeNames.put("fdr", "double");
        paramJavaTypeNames.put("fpr", "double");
        paramJavaTypeNames.put("fwe", "double");
        paramJavaTypeNames.put("numTopFeatures", "int");
        paramJavaTypeNames.put("percentile", "double");
        paramJavaTypeNames.put("selectorType", "String");
        paramJavaTypeNames.put("splits", "double[]");
        paramJavaTypeNames.put("splitsArray", "double[][]");
        paramJavaTypeNames.put("bucketLength", "double");
        paramJavaTypeNames.put("threshold", "double");
        paramJavaTypeNames.put("stages", "PipelineStage[]");
        paramJavaTypeNames.put("itemsCol", "String");
        paramJavaTypeNames.put("minConfidence", "double");
        paramJavaTypeNames.put("predictionCol", "String");
        paramJavaTypeNames.put("minSupport", "double");
        paramJavaTypeNames.put("topicDistributionCol", "String");
        paramJavaTypeNames.put("distanceMeasure", "String");
        paramJavaTypeNames.put("initMode", "String");
        paramJavaTypeNames.put("initSteps", "int");
        paramJavaTypeNames.put("tol", "double");
        paramJavaTypeNames.put("checkpointInterval", "int");
        paramJavaTypeNames.put("docConcentration", "double");
        paramJavaTypeNames.put("keepLastCheckpoint", "boolean");
        paramJavaTypeNames.put("learningDecay", "double");
        paramJavaTypeNames.put("learningOffset", "double");
        paramJavaTypeNames.put("optimizeDocConcentration", "boolean");
        paramJavaTypeNames.put("optimizer", "String");
        paramJavaTypeNames.put("subsamplingRate", "double");
        paramJavaTypeNames.put("topicConcentration", "double");
        paramJavaTypeNames.put("probabilityCol", "String");
        paramJavaTypeNames.put("minDivisibleClusterSize", "double");
        paramJavaTypeNames.put("cacheNodeIds", "boolean");
        paramJavaTypeNames.put("featureSubsetStrategy", "String");
        paramJavaTypeNames.put("impurity", "String");
        paramJavaTypeNames.put("maxBins", "int");
        paramJavaTypeNames.put("maxDepth", "int");
        paramJavaTypeNames.put("maxMemoryInMB", "int");
        paramJavaTypeNames.put("minInfoGain", "double");
        paramJavaTypeNames.put("minInstancesPerNode", "int");
        paramJavaTypeNames.put("numTrees", "int");
        paramJavaTypeNames.put("rawPredictionCol", "String");
        paramJavaTypeNames.put("thresholds", "double[]");
        paramJavaTypeNames.put("classifier", "Classifier<?,?,?>");
        paramJavaTypeNames.put("weightCol", "String");
        paramJavaTypeNames.put("blockSize", "int");
        paramJavaTypeNames.put("initialWeights", "Vector");
        paramJavaTypeNames.put("layers", "int[]");
        paramJavaTypeNames.put("solver", "String");
        paramJavaTypeNames.put("aggregationDepth", "int");
        paramJavaTypeNames.put("elasticNetParam", "double");
        paramJavaTypeNames.put("family", "String");
        paramJavaTypeNames.put("fitIntercept", "boolean");
        paramJavaTypeNames.put("lowerBoundsOnCoefficients", "Matrix");
        paramJavaTypeNames.put("lowerBoundsOnIntercepts", "Vector");
        paramJavaTypeNames.put("regParam", "double");
        paramJavaTypeNames.put("standardization", "boolean");
        paramJavaTypeNames.put("upperBoundsOnCoefficients", "Matrix");
        paramJavaTypeNames.put("upperBoundsOnIntercepts", "Vector");
        paramJavaTypeNames.put("modelType", "String");
        paramJavaTypeNames.put("smoothing", "double");
        paramJavaTypeNames.put("lossType", "String");
        paramJavaTypeNames.put("validationIndicatorCol", "String");
        paramJavaTypeNames.put("coldStartStrategy", "String");
        paramJavaTypeNames.put("itemCol", "String");
        paramJavaTypeNames.put("userCol", "String");
        paramJavaTypeNames.put("alpha", "double");
        paramJavaTypeNames.put("finalStorageLevel", "String");
        paramJavaTypeNames.put("implicitPrefs", "boolean");
        paramJavaTypeNames.put("intermediateStorageLevel", "String");
        paramJavaTypeNames.put("nonnegative", "boolean");
        paramJavaTypeNames.put("numBlocks", "int");
        paramJavaTypeNames.put("numItemBlocks", "int");
        paramJavaTypeNames.put("numUserBlocks", "int");
        paramJavaTypeNames.put("rank", "int");
        paramJavaTypeNames.put("ratingCol", "String");
        paramJavaTypeNames.put("linkPredictionCol", "String");
        paramJavaTypeNames.put("epsilon", "double");
        paramJavaTypeNames.put("loss", "String");
        paramJavaTypeNames.put("featureIndex", "int");
        paramJavaTypeNames.put("isotonic", "boolean");
        paramJavaTypeNames.put("link", "String");
        paramJavaTypeNames.put("linkPower", "double");
        paramJavaTypeNames.put("offsetCol", "String");
        paramJavaTypeNames.put("variancePower", "double");
        paramJavaTypeNames.put("varianceCol", "String");
        paramJavaTypeNames.put("quantileProbabilities", "double[]");
        paramJavaTypeNames.put("quantilesCol", "String");
        paramJavaTypeNames.put("censorCol", "String");
    }

    static {
        estimatorParams = new HashMap<>();
        transformerParams = new HashMap<>();

        estimatorParams.put(
            "FPGrowth",
            new ArrayList<>(Arrays.asList("itemsCol", "minConfidence", "minSupport", "numPartitions", "predictionCol"))
        );
        estimatorParams.put(
            "TrainValidationSplit",
            new ArrayList<>(
                    Arrays.asList(
                        "collectSubModels",
                        "estimator",
                        "estimatorParamMaps",
                        "evaluator",
                        "parallelism",
                        "seed",
                        "trainRatio"
                    )
            )
        );
        estimatorParams.put(
            "CrossValidator",
            new ArrayList<>(
                    Arrays.asList(
                        "collectSubModels",
                        "estimator",
                        "estimatorParamMaps",
                        "evaluator",
                        "numFolds",
                        "parallelism",
                        "seed"
                    )
            )
        );
        estimatorParams.put(
            "RandomForestClassifier",
            new ArrayList<>(
                    Arrays.asList(
                        "cacheNodeIds",
                        "checkpointInterval",
                        "featuresCol",
                        "featureSubsetStrategy",
                        "impurity",
                        "labelCol",
                        "maxBins",
                        "maxDepth",
                        "maxMemoryInMB",
                        "minInfoGain",
                        "minInstancesPerNode",
                        "numTrees",
                        "predictionCol",
                        "probabilityCol",
                        "rawPredictionCol",
                        "seed",
                        "subsamplingRate",
                        "thresholds"
                    )
            )
        );
        estimatorParams.put(
            "OneVsRest",
            new ArrayList<>(
                    Arrays.asList(
                        "classifier",
                        "featuresCol",
                        "labelCol",
                        "parallelism",
                        "predictionCol",
                        "rawPredictionCol",
                        "weightCol"
                    )
            )
        );
        estimatorParams.put(
            "NaiveBayes",
            new ArrayList<>(
                    Arrays.asList(
                        "featuresCol",
                        "labelCol",
                        "modelType",
                        "predictionCol",
                        "probabilityCol",
                        "rawPredictionCol",
                        "smoothing",
                        "thresholds",
                        "weightCol"
                    )
            )
        );
        estimatorParams.put(
            "MultilayerPerceptronClassifier",
            new ArrayList<>(
                    Arrays.asList(
                        "blockSize",
                        "featuresCol",
                        "initialWeights",
                        "labelCol",
                        "layers",
                        "maxIter",
                        "predictionCol",
                        "probabilityCol",
                        "rawPredictionCol",
                        "seed",
                        "solver",
                        "stepSize",
                        "thresholds",
                        "tol"
                    )
            )
        );
        estimatorParams.put(
            "DecisionTreeClassifier",
            new ArrayList<>(
                    Arrays.asList(
                        "cacheNodeIds",
                        "checkpointInterval",
                        "featuresCol",
                        "impurity",
                        "labelCol",
                        "maxBins",
                        "maxDepth",
                        "maxMemoryInMB",
                        "minInfoGain",
                        "minInstancesPerNode",
                        "predictionCol",
                        "probabilityCol",
                        "rawPredictionCol",
                        "seed",
                        "thresholds"
                    )
            )
        );
        estimatorParams.put(
            "LogisticRegression",
            new ArrayList<>(
                    Arrays.asList(
                        "aggregationDepth",
                        "elasticNetParam",
                        "family",
                        "featuresCol",
                        "fitIntercept",
                        "labelCol",
                        "lowerBoundsOnCoefficients",
                        "lowerBoundsOnIntercepts",
                        "maxIter",
                        "predictionCol",
                        "probabilityCol",
                        "rawPredictionCol",
                        "regParam",
                        "standardization",
                        "threshold",
                        "thresholds",
                        "tol",
                        "upperBoundsOnCoefficients",
                        "upperBoundsOnIntercepts",
                        "weightCol"
                    )
            )
        );
        estimatorParams.put(
            "LinearSVC",
            new ArrayList<>(
                    Arrays.asList(
                        "aggregationDepth",
                        "featuresCol",
                        "fitIntercept",
                        "labelCol",
                        "maxIter",
                        "predictionCol",
                        "rawPredictionCol",
                        "regParam",
                        "standardization",
                        "threshold",
                        "tol",
                        "weightCol"
                    )
            )
        );
        estimatorParams.put(
            "GBTClassifier",
            new ArrayList<>(
                    Arrays.asList(
                        "cacheNodeIds",
                        "checkpointInterval",
                        "featuresCol",
                        "featureSubsetStrategy",
                        "impurity",
                        "labelCol",
                        "lossType",
                        "maxBins",
                        "maxDepth",
                        "maxIter",
                        "maxMemoryInMB",
                        "minInfoGain",
                        "minInstancesPerNode",
                        "predictionCol",
                        "probabilityCol",
                        "rawPredictionCol",
                        "seed",
                        "stepSize",
                        "subsamplingRate",
                        "thresholds",
                        "validationIndicatorCol"
                    )
            )
        );
        estimatorParams.put(
            "LDA",
            new ArrayList<>(
                    Arrays.asList(
                        "checkpointInterval",
                        "docConcentration",
                        "docConcentration",
                        "featuresCol",
                        "k",
                        "keepLastCheckpoint",
                        "learningDecay",
                        "learningOffset",
                        "maxIter",
                        "optimizeDocConcentration",
                        "optimizer",
                        "seed",
                        "subsamplingRate",
                        "topicConcentration",
                        "topicDistributionCol"
                    )
            )
        );
        estimatorParams.put(
            "KMeans",
            new ArrayList<>(
                    Arrays.asList(
                        "distanceMeasure",
                        "featuresCol",
                        "initMode",
                        "initSteps",
                        "k",
                        "maxIter",
                        "predictionCol",
                        "seed",
                        "tol"
                    )
            )
        );
        estimatorParams.put(
            "GaussianMixture",
            new ArrayList<>(
                    Arrays.asList("featuresCol", "k", "maxIter", "predictionCol", "probabilityCol", "seed", "tol")
            )
        );
        estimatorParams.put(
            "BisectingKMeans",
            new ArrayList<>(
                    Arrays.asList(
                        "distanceMeasure",
                        "featuresCol",
                        "k",
                        "maxIter",
                        "minDivisibleClusterSize",
                        "predictionCol",
                        "seed"
                    )
            )
        );
        estimatorParams.put(
            "Word2Vec",
            new ArrayList<>(
                    Arrays.asList(
                        "inputCol",
                        "maxIter",
                        "maxSentenceLength",
                        "minCount",
                        "numPartitions",
                        "outputCol",
                        "seed",
                        "stepSize",
                        "vectorSize",
                        "windowSize"
                    )
            )
        );
        estimatorParams.put(
            "VectorIndexer",
            new ArrayList<>(Arrays.asList("handleInvalid", "inputCol", "maxCategories", "outputCol"))
        );
        estimatorParams.put(
            "StringIndexer",
            new ArrayList<>(Arrays.asList("handleInvalid", "inputCol", "outputCol", "stringOrderType"))
        );
        estimatorParams.put(
            "StandardScaler",
            new ArrayList<>(Arrays.asList("inputCol", "outputCol", "withMean", "withStd"))
        );
        estimatorParams.put(
            "RFormula",
            new ArrayList<>(
                    Arrays.asList(
                        "featuresCol",
                        "forceIndexLabel",
                        "formula",
                        "handleInvalid",
                        "labelCol",
                        "stringIndexerOrderType"
                    )
            )
        );
        estimatorParams.put(
            "QuantileDiscretizer",
            new ArrayList<>(
                    Arrays.asList(
                        "handleInvalid",
                        "inputCol",
                        "inputCols",
                        "numBuckets",
                        "numBucketsArray",
                        "outputCol",
                        "outputCols",
                        "relativeError"
                    )
            )
        );
        estimatorParams.put("PCA", new ArrayList<>(Arrays.asList("inputCol", "k", "outputCol")));
        estimatorParams.put(
            "OneHotEncoderEstimator",
            new ArrayList<>(Arrays.asList("dropLast", "handleInvalid", "inputCols", "outputCols"))
        );
        estimatorParams.put("MinMaxScaler", new ArrayList<>(Arrays.asList("inputCol", "max", "min", "outputCol")));
        estimatorParams.put(
            "MinHashLSH",
            new ArrayList<>(Arrays.asList("inputCol", "numHashTables", "outputCol", "seed"))
        );
        estimatorParams.put("MaxAbsScaler", new ArrayList<>(Arrays.asList("inputCol", "outputCol")));
        estimatorParams.put(
            "Imputer",
            new ArrayList<>(Arrays.asList("inputCols", "missingValue", "outputCols", "strategy"))
        );
        estimatorParams.put("IDF", new ArrayList<>(Arrays.asList("inputCol", "minDocFreq", "outputCol")));
        estimatorParams.put(
            "BucketedRandomProjectionLSH",
            new ArrayList<>(Arrays.asList("bucketLength", "inputCol", "numHashTables", "outputCol", "seed"))
        );
        estimatorParams.put(
            "CountVectorizer",
            new ArrayList<>(Arrays.asList("binary", "inputCol", "maxDF", "minDF", "minTF", "outputCol", "vocabSize"))
        );
        estimatorParams.put(
            "ChiSqSelector",
            new ArrayList<>(
                    Arrays.asList(
                        "fdr",
                        "featuresCol",
                        "fpr",
                        "fwe",
                        "labelCol",
                        "numTopFeatures",
                        "outputCol",
                        "percentile",
                        "selectorType"
                    )
            )
        );
        estimatorParams.put(
            "ALS",
            new ArrayList<>(
                    Arrays.asList(
                        "alpha",
                        "checkpointInterval",
                        "coldStartStrategy",
                        "finalStorageLevel",
                        "implicitPrefs",
                        "intermediateStorageLevel",
                        "itemCol",
                        "maxIter",
                        "nonnegative",
                        "numBlocks",
                        "numItemBlocks",
                        "numUserBlocks",
                        "predictionCol",
                        "rank",
                        "ratingCol",
                        "regParam",
                        "seed",
                        "userCol"
                    )
            )
        );
        estimatorParams.put(
            "RandomForestRegressor",
            new ArrayList<>(
                    Arrays.asList(
                        "cacheNodeIds",
                        "checkpointInterval",
                        "featuresCol",
                        "featureSubsetStrategy",
                        "impurity",
                        "labelCol",
                        "maxBins",
                        "maxDepth",
                        "maxMemoryInMB",
                        "minInfoGain",
                        "minInstancesPerNode",
                        "numTrees",
                        "predictionCol",
                        "seed",
                        "subsamplingRate"
                    )
            )
        );
        estimatorParams.put(
            "IsotonicRegression",
            new ArrayList<>(
                    Arrays.asList("featureIndex", "featuresCol", "isotonic", "labelCol", "predictionCol", "weightCol")
            )
        );
        estimatorParams.put(
            "LinearRegression",
            new ArrayList<>(
                    Arrays.asList(
                        "aggregationDepth",
                        "elasticNetParam",
                        "epsilon",
                        "featuresCol",
                        "fitIntercept",
                        "labelCol",
                        "loss",
                        "maxIter",
                        "predictionCol",
                        "regParam",
                        "solver",
                        "standardization",
                        "tol",
                        "weightCol"
                    )
            )
        );
        estimatorParams.put(
            "GBTRegressor",
            new ArrayList<>(
                    Arrays.asList(
                        "cacheNodeIds",
                        "checkpointInterval",
                        "featuresCol",
                        "featureSubsetStrategy",
                        "impurity",
                        "labelCol",
                        "lossType",
                        "maxBins",
                        "maxDepth",
                        "maxIter",
                        "maxMemoryInMB",
                        "minInfoGain",
                        "minInstancesPerNode",
                        "predictionCol",
                        "seed",
                        "stepSize",
                        "subsamplingRate",
                        "validationIndicatorCol"
                    )
            )
        );
        estimatorParams.put(
            "GeneralizedLinearRegression",
            new ArrayList<>(
                    Arrays.asList(
                        "family",
                        "featuresCol",
                        "fitIntercept",
                        "labelCol",
                        "link",
                        "linkPower",
                        "linkPredictionCol",
                        "maxIter",
                        "offsetCol",
                        "predictionCol",
                        "regParam",
                        "solver",
                        "tol",
                        "variancePower",
                        "weightCol"
                    )
            )
        );
        estimatorParams.put(
            "DecisionTreeRegressor",
            new ArrayList<>(
                    Arrays.asList(
                        "cacheNodeIds",
                        "checkpointInterval",
                        "featuresCol",
                        "impurity",
                        "labelCol",
                        "maxBins",
                        "maxDepth",
                        "maxMemoryInMB",
                        "minInfoGain",
                        "minInstancesPerNode",
                        "predictionCol",
                        "seed",
                        "varianceCol"
                    )
            )
        );
        estimatorParams.put(
            "AFTSurvivalRegression",
            new ArrayList<>(
                    Arrays.asList(
                        "aggregationDepth",
                        "censorCol",
                        "featuresCol",
                        "fitIntercept",
                        "labelCol",
                        "maxIter",
                        "predictionCol",
                        "quantileProbabilities",
                        "quantilesCol",
                        "tol"
                    )
            )
        );
        estimatorParams.put("Pipeline", new ArrayList<>(Arrays.asList("stages")));


        transformerParams.put(
            "FPGrowthModel",
            new ArrayList<>(Arrays.asList("itemsCol", "minConfidence", "parent", "predictionCol"))
        );
        transformerParams.put("VectorIndexerModel", new ArrayList<>(Arrays.asList("inputCol", "outputCol", "parent")));
        transformerParams.put("TrainValidationSplitModel", new ArrayList<>(Arrays.asList("parent")));
        transformerParams.put("CrossValidatorModel", new ArrayList<>(Arrays.asList("parent")));
        transformerParams.put(
            "RandomForestClassificationModel",
            new ArrayList<>(
                    Arrays.asList(
                        "cacheNodeIds",
                        "checkpointInterval",
                        "featuresCol",
                        "featureSubsetStrategy",
                        "impurity",
                        "maxBins",
                        "maxDepth",
                        "maxMemoryInMB",
                        "minInfoGain",
                        "minInstancesPerNode",
                        "numTrees",
                        "parent",
                        "predictionCol",
                        "probabilityCol",
                        "rawPredictionCol",
                        "seed",
                        "subsamplingRate",
                        "thresholds"
                    )
            )
        );
        transformerParams.put(
            "NaiveBayesModel",
            new ArrayList<>(
                    Arrays.asList(
                        "featuresCol",
                        "parent",
                        "predictionCol",
                        "probabilityCol",
                        "rawPredictionCol",
                        "thresholds"
                    )
            )
        );
        transformerParams.put(
            "MultilayerPerceptronClassificationModel",
            new ArrayList<>(
                    Arrays.asList(
                        "featuresCol",
                        "parent",
                        "predictionCol",
                        "probabilityCol",
                        "rawPredictionCol",
                        "thresholds"
                    )
            )
        );
        transformerParams.put(
            "OneVsRestModel",
            new ArrayList<>(Arrays.asList("featuresCol", "parent", "predictionCol", "rawPredictionCol"))
        );
        transformerParams.put(
            "GBTClassificationModel",
            new ArrayList<>(
                    Arrays.asList(
                        "cacheNodeIds",
                        "checkpointInterval",
                        "featuresCol",
                        "featureSubsetStrategy",
                        "impurity",
                        "maxBins",
                        "maxDepth",
                        "maxIter",
                        "maxMemoryInMB",
                        "minInfoGain",
                        "minInstancesPerNode",
                        "parent",
                        "predictionCol",
                        "probabilityCol",
                        "rawPredictionCol",
                        "seed",
                        "stepSize",
                        "subsamplingRate",
                        "thresholds"
                    )
            )
        );
        transformerParams.put(
            "LinearSVCModel",
            new ArrayList<>(
                    Arrays.asList(
                        "featuresCol",
                        "parent",
                        "predictionCol",
                        "rawPredictionCol",
                        "threshold",
                        "weightCol"
                    )
            )
        );
        transformerParams.put(
            "DecisionTreeClassificationModel",
            new ArrayList<>(
                    Arrays.asList(
                        "cacheNodeIds",
                        "checkpointInterval",
                        "featuresCol",
                        "impurity",
                        "maxBins",
                        "maxDepth",
                        "maxMemoryInMB",
                        "minInfoGain",
                        "minInstancesPerNode",
                        "parent",
                        "predictionCol",
                        "probabilityCol",
                        "rawPredictionCol",
                        "seed",
                        "thresholds"
                    )
            )
        );
        transformerParams.put(
            "LogisticRegressionModel",
            new ArrayList<>(
                    Arrays.asList(
                        "featuresCol",
                        "parent",
                        "predictionCol",
                        "probabilityCol",
                        "rawPredictionCol",
                        "threshold",
                        "thresholds"
                    )
            )
        );
        transformerParams.put(
            "LocalLDAModel",
            new ArrayList<>(Arrays.asList("featuresCol", "parent", "seed", "topicDistributionCol"))
        );
        transformerParams.put("KMeansModel", new ArrayList<>(Arrays.asList("featuresCol", "parent", "predictionCol")));
        transformerParams.put(
            "GaussianMixtureModel",
            new ArrayList<>(Arrays.asList("featuresCol", "parent", "predictionCol", "probabilityCol"))
        );
        transformerParams.put(
            "DistributedLDAModel",
            new ArrayList<>(Arrays.asList("featuresCol", "parent", "seed", "topicDistributionCol"))
        );
        transformerParams.put(
            "BisectingKMeansModel",
            new ArrayList<>(Arrays.asList("featuresCol", "parent", "predictionCol"))
        );
        transformerParams.put("VectorAttributeRewriter", new ArrayList<>(Arrays.asList()));
        transformerParams.put("Word2VecModel", new ArrayList<>(Arrays.asList("inputCol", "outputCol", "parent")));
        transformerParams.put(
            "VectorSlicer",
            new ArrayList<>(Arrays.asList("indices", "inputCol", "names", "outputCol"))
        );
        transformerParams.put("VectorSizeHint", new ArrayList<>(Arrays.asList("handleInvalid", "inputCol", "size")));
        transformerParams.put(
            "VectorAssembler",
            new ArrayList<>(Arrays.asList("handleInvalid", "inputCols", "outputCol"))
        );
        transformerParams.put("Tokenizer", new ArrayList<>(Arrays.asList("inputCol", "outputCol")));
        transformerParams.put("StandardScalerModel", new ArrayList<>(Arrays.asList("inputCol", "outputCol", "parent")));
        transformerParams.put("SQLTransformer", new ArrayList<>(Arrays.asList("statement")));
        transformerParams.put(
            "StringIndexerModel",
            new ArrayList<>(Arrays.asList("handleInvalid", "inputCol", "outputCol", "parent"))
        );
        transformerParams.put(
            "StopWordsRemover",
            new ArrayList<>(Arrays.asList("caseSensitive", "inputCol", "locale", "outputCol", "stopWords"))
        );
        transformerParams.put("RFormulaModel", new ArrayList<>(Arrays.asList("parent")));
        transformerParams.put(
            "OneHotEncoderModel",
            new ArrayList<>(Arrays.asList("dropLast", "handleInvalid", "inputCols", "outputCols", "parent"))
        );
        transformerParams.put(
            "RegexTokenizer",
            new ArrayList<>(Arrays.asList("gaps", "inputCol", "minTokenLength", "outputCol", "pattern", "toLowercase"))
        );
        transformerParams.put("PolynomialExpansion", new ArrayList<>(Arrays.asList("degree", "inputCol", "outputCol")));
        transformerParams.put("PCAModel", new ArrayList<>(Arrays.asList("inputCol", "outputCol", "parent")));
        transformerParams.put("OneHotEncoder", new ArrayList<>(Arrays.asList("dropLast", "inputCol", "outputCol")));
        transformerParams.put("Normalizer", new ArrayList<>(Arrays.asList("inputCol", "outputCol", "p")));
        transformerParams.put("NGram", new ArrayList<>(Arrays.asList("inputCol", "n", "outputCol")));
        transformerParams.put(
            "MinMaxScalerModel",
            new ArrayList<>(Arrays.asList("inputCol", "max", "min", "outputCol", "parent"))
        );
        transformerParams.put("MinHashLSHModel", new ArrayList<>(Arrays.asList("inputCol", "outputCol", "parent")));
        transformerParams.put("MaxAbsScalerModel", new ArrayList<>(Arrays.asList("inputCol", "outputCol", "parent")));
        transformerParams.put("Interaction", new ArrayList<>(Arrays.asList("inputCols", "outputCol")));
        transformerParams.put("IndexToString", new ArrayList<>(Arrays.asList("inputCol", "labels", "outputCol")));
        transformerParams.put("ImputerModel", new ArrayList<>(Arrays.asList("inputCols", "outputCols", "parent")));
        transformerParams.put(
            "CountVectorizerModel",
            new ArrayList<>(Arrays.asList("binary", "inputCol", "minTF", "outputCol", "parent"))
        );
        transformerParams.put("IDFModel", new ArrayList<>(Arrays.asList("inputCol", "outputCol", "parent")));
        transformerParams.put(
            "HashingTF",
            new ArrayList<>(Arrays.asList("binary", "inputCol", "numFeatures", "outputCol"))
        );
        transformerParams.put(
            "FeatureHasher",
            new ArrayList<>(Arrays.asList("categoricalCols", "inputCols", "inputCols", "numFeatures", "outputCol"))
        );
        transformerParams.put(
            "ElementwiseProduct",
            new ArrayList<>(Arrays.asList("inputCol", "outputCol", "scalingVec"))
        );
        transformerParams.put("DCT", new ArrayList<>(Arrays.asList("inputCol", "inverse", "outputCol")));
        transformerParams.put("ColumnPruner", new ArrayList<>(Arrays.asList()));
        transformerParams.put(
            "Bucketizer",
            new ArrayList<>(
                    Arrays.asList(
                        "handleInvalid",
                        "inputCol",
                        "inputCols",
                        "outputCol",
                        "outputCols",
                        "parent",
                        "splits",
                        "splitsArray"
                    )
            )
        );
        transformerParams.put(
            "ChiSqSelectorModel",
            new ArrayList<>(Arrays.asList("featuresCol", "outputCol", "parent"))
        );
        transformerParams.put(
            "BucketedRandomProjectionLSHModel",
            new ArrayList<>(Arrays.asList("inputCol", "outputCol", "parent"))
        );
        transformerParams.put("Binarizer", new ArrayList<>(Arrays.asList("inputCol", "outputCol", "threshold")));
        transformerParams.put(
            "ALSModel",
            new ArrayList<>(Arrays.asList("coldStartStrategy", "itemCol", "parent", "predictionCol", "userCol"))
        );
        transformerParams.put(
            "RandomForestRegressionModel",
            new ArrayList<>(
                    Arrays.asList(
                        "cacheNodeIds",
                        "checkpointInterval",
                        "featuresCol",
                        "featureSubsetStrategy",
                        "impurity",
                        "maxBins",
                        "maxDepth",
                        "maxMemoryInMB",
                        "minInfoGain",
                        "minInstancesPerNode",
                        "numTrees",
                        "parent",
                        "predictionCol",
                        "seed",
                        "subsamplingRate"
                    )
            )
        );
        transformerParams.put(
            "LinearRegressionModel",
            new ArrayList<>(Arrays.asList("featuresCol", "parent", "predictionCol"))
        );
        transformerParams.put(
            "IsotonicRegressionModel",
            new ArrayList<>(Arrays.asList("featureIndex", "featuresCol", "parent", "predictionCol"))
        );
        transformerParams.put(
            "GeneralizedLinearRegressionModel",
            new ArrayList<>(Arrays.asList("featuresCol", "linkPredictionCol", "parent", "predictionCol"))
        );
        transformerParams.put(
            "GBTRegressionModel",
            new ArrayList<>(
                    Arrays.asList(
                        "cacheNodeIds",
                        "checkpointInterval",
                        "featuresCol",
                        "featureSubsetStrategy",
                        "impurity",
                        "maxBins",
                        "maxDepth",
                        "maxIter",
                        "maxMemoryInMB",
                        "minInfoGain",
                        "minInstancesPerNode",
                        "parent",
                        "predictionCol",
                        "seed",
                        "stepSize",
                        "subsamplingRate"
                    )
            )
        );
        transformerParams.put(
            "DecisionTreeRegressionModel",
            new ArrayList<>(
                    Arrays.asList(
                        "cacheNodeIds",
                        "checkpointInterval",
                        "featuresCol",
                        "impurity",
                        "maxBins",
                        "maxDepth",
                        "maxMemoryInMB",
                        "minInfoGain",
                        "minInstancesPerNode",
                        "parent",
                        "predictionCol",
                        "seed",
                        "varianceCol"
                    )
            )
        );
        transformerParams.put(
            "AFTSurvivalRegressionModel",
            new ArrayList<>(
                    Arrays.asList("featuresCol", "parent", "predictionCol", "quantileProbabilities", "quantilesCol")
            )
        );
        transformerParams.put("PipelineModel", new ArrayList<>(Arrays.asList("parent")));


    }

    public static String getEstimatorFullClassName(String name, ExceptionMetadata metadata) {
        if (!estimatorFullClassNames.containsKey(name)) {
            throw new UnrecognizedRumbleMLClassReferenceException(
                    "Unrecognized RumbleML class; Make sure \"" + name + "\" is a valid estimator of RumbleML API.",
                    metadata
            );
        }
        return estimatorFullClassNames.get(name);
    }

    public static String getTransformerFullClassName(String name, ExceptionMetadata metadata) {
        if (!transformerFullClassNames.containsKey(name)) {
            throw new UnrecognizedRumbleMLClassReferenceException(
                    "Unrecognized RumbleML class; Make sure \"" + name + "\" is a valid transformer of RumbleML API.",
                    metadata
            );
        }
        return transformerFullClassNames.get(name);
    }

    public static List<String> getEstimatorParams(String name, ExceptionMetadata metadata) {
        if (!estimatorParams.containsKey(name)) {
            throw new UnrecognizedRumbleMLClassReferenceException(
                    "Unrecognized RumbleML class; Make sure \"" + name + "\" is a valid estimator of RumbleML API.",
                    metadata
            );
        }
        return estimatorParams.get(name);
    }

    public static List<String> getTransformerParams(String name, ExceptionMetadata metadata) {
        if (!transformerParams.containsKey(name)) {
            throw new UnrecognizedRumbleMLClassReferenceException(
                    "Unrecognized RumbleML class; Make sure \"" + name + "\" is a valid transformer of RumbleML API.",
                    metadata
            );
        }
        return transformerParams.get(name);
    }

    public static void validateParameterForTransformer(
            String transformerShortName,
            String paramName,
            ExceptionMetadata metadata
    ) {
        if (!transformerParams.containsKey(transformerShortName)) {
            throw new UnrecognizedRumbleMLClassReferenceException(
                    "Unrecognized RumbleML class; Make sure \""
                        + transformerShortName
                        + "\" is a valid transformer of RumbleML API.",
                    metadata
            );
        }
        if (!transformerParams.get(transformerShortName).contains(paramName)) {
            throw new UnrecognizedRumbleMLParamReferenceException(
                    "Make sure \""
                        + paramName
                        + "\" is a valid parameter of \""
                        + transformerShortName
                        + "\".",
                    metadata
            );
        }
    }

    public static void validateParameterForEstimator(
            String estimatorName,
            String paramName,
            ExceptionMetadata metadata
    ) {
        if (!estimatorParams.containsKey(estimatorName)) {
            throw new UnrecognizedRumbleMLClassReferenceException(
                    "Unrecognized RumbleML class; Make sure \""
                        + estimatorName
                        + "\" is a valid estimator of RumbleML API.",
                    metadata
            );
        }
        if (!estimatorParams.get(estimatorName).contains(paramName)) {
            throw new UnrecognizedRumbleMLParamReferenceException(
                    "Make sure \""
                        + paramName
                        + "\" is a valid parameter of \""
                        + estimatorName
                        + "\".",
                    metadata
            );
        }
    }

    public static String getParamJavaTypeName(String name, ExceptionMetadata metadata) {
        if (!paramJavaTypeNames.containsKey(name)) {
            throw new UnrecognizedRumbleMLParamReferenceException(
                    "Parameter \""
                        + name
                        + "\" does not exist in in RumbleML API.\"",
                    metadata
            );
        }
        return paramJavaTypeNames.get(name);
    }

    public static String getRumbleMLShortName(String javaFullClassName) {
        int indexOfLastDot = javaFullClassName.lastIndexOf(".");
        return javaFullClassName.substring(indexOfLastDot + 1);
    }
}
