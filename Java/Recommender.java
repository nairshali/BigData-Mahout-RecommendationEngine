package test;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.eval.IRStatistics;
import org.apache.mahout.cf.taste.eval.RecommenderBuilder;
import org.apache.mahout.cf.taste.eval.RecommenderEvaluator;
import org.apache.mahout.cf.taste.eval.RecommenderIRStatsEvaluator;
import org.apache.mahout.cf.taste.impl.eval.AverageAbsoluteDifferenceRecommenderEvaluator;
import org.apache.mahout.cf.taste.impl.eval.GenericRecommenderIRStatsEvaluator;
import org.apache.mahout.cf.taste.impl.eval.RMSRecommenderEvaluator;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.impl.neighborhood.ThresholdUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericItemBasedRecommender;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.EuclideanDistanceSimilarity;
import org.apache.mahout.cf.taste.impl.similarity.LogLikelihoodSimilarity;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.impl.similarity.SpearmanCorrelationSimilarity;
import org.apache.mahout.cf.taste.impl.similarity.TanimotoCoefficientSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.similarity.ItemSimilarity;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;

public class main {
    public static void main(String[] args) throws IOException, TasteException {
        
        try {
            // set variable
            File ratingFile = new File("/home/hadoop/ratingnew.txt");
            //File ratingFile = new File("/home/hadoop/ratings.txt");
            
            // read new rating file
            DataModel model = new FileDataModel(ratingFile,",");
            //DataModel model = new FileDataModel(ratingFile," ");
            
            // recommender builder
            RecommenderBuilder recommenderBuilder = new RecommenderBuilder() {
                public Recommender buildRecommender(DataModel model) throws TasteException {
                    UserSimilarity similarity = new PearsonCorrelationSimilarity(model);
                    //UserSimilarity similarity = new SpearmanCorrelationSimilarity(model);
                    //UserSimilarity similarity = new EuclideanDistanceSimilarity(model);
                    //UserSimilarity similarity = new TanimotoCoefficientSimilarity(model);
                    //UserSimilarity similarity = new LogLikelihoodSimilarity(model);
                    UserNeighborhood neighborhood = new ThresholdUserNeighborhood(0.1, similarity, model);
                    
                    return new GenericUserBasedRecommender(model, neighborhood, similarity);
                    }
                };

                // check two recommendation suggested for user ID 4
                Recommender recommender = recommenderBuilder.buildRecommender(model);
                
                List<RecommendedItem> recommendations = recommender.recommend(4, 2);
                
                for (RecommendedItem recommendation : recommendations) {
                    System.out.println("Recommendation: " + recommendation);
                    }
                
                // Recommender Evaluation -- Average Absolute Difference Evaluator
                RecommenderEvaluator scoreEvaluator = new AverageAbsoluteDifferenceRecommenderEvaluator();
                double score = scoreEvaluator.evaluate(recommenderBuilder, null, model, 0.7, 0.1);
                System.out.println("Average Absolute Score: " + score);
                
                // Recommender Evaluation -- RMS Evaluator
                RecommenderEvaluator rmsEvaluator = new RMSRecommenderEvaluator();
                double rmsscore = rmsEvaluator.evaluate(recommenderBuilder, null,
                        model, 0.9, 0.1);
                System.out.println("ALS-based Recommender RMS Score is:" + rmsscore);
                
                // Recommender Evaluation -- IRStats Evaluator
                RecommenderIRStatsEvaluator recPrecEvaluator = new GenericRecommenderIRStatsEvaluator();
                IRStatistics stats = recPrecEvaluator.evaluate(recommenderBuilder, null, model, null, 2, GenericRecommenderIRStatsEvaluator.CHOOSE_THRESHOLD, 1.0);
                System.out.println("Precision: " + stats.getPrecision());
                System.out.println("Recall: " + stats.getRecall());    
            
            
        }catch (FileNotFoundException e) {
            System.out.println("File not found.");
            e.printStackTrace();   
        }catch (IOException e) {
            e.printStackTrace();
        }
    }
}