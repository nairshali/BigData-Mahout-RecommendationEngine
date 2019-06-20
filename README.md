# BIgData-Mahout-RecommendationEngine
Mahout Recommendation Engine


# Design
•	Using the first python program the trustnew file is created. (File Pre-processing step 1)
•	Use trustnew file in the second python program to generate the ratingnew file. (File Pre-processing step 2)
•	Since I have a virtual machine on which hadoop and mahout is installed I need to copy this file there (Used gmail).If you have python,hadoop and mahout on the same machine then it is not required to transfer the file.
•	Start the virtual machine-Connect it .Open the hadoop terminal.Start all the services by start-all.sh
•	Put and get the ratingnew file to the hadoophdfs, hadoop fs -put ~/downloads/ratingnew.txt 
•	Get the file to the home directoryhadoop fs –get ratingnew.txt
•	Now create the recommender program using eclipse. Open Eclipse, Go to File -> New Project. Choose Java Project.
•	Copy the java program onto the class file .Now add all the external archives to the project .Make a right click on the project -> Build Path -> Add External Archives’; and then select the all jar files from the following folders /usr/lib/mahout
•	Here the following components are assembled: 
--Data model where the data file is read into, implemented via DataModel
--User-user similarity metric, implemented via UserSimilarity
--User neighbourhood definition, implemented via ThresholdUserNeighborhood. You may use Fixed-size neighbourhoods or Threshold-based neighbourhood. Here we have used threshold base neighbourhood and have kept it same for all the other algorithms tested along with the Pearson Correlation. The threshold should be between –1 and 1, because all similarity metrics return similarity values in this range. At the moment, our example uses a standard Pearson correlation as the similarity metric. It’s as simple as changing one line to instead instantiate a ThresholdUserNeighborhood: new ThresholdUserNeighborhood(0.1, similarity, model) Now the evaluator scores the recommender at 0.23.Here, distance illustrates similarity: farther means less similar. 
--Recommender engine, implemented via a Recommender (here, GenericUserBasedRecommender)
--Final parameter to evaluate () is 0.1. This means only 10 percent of all the data is used for evaluation. This is purely for convenience; evaluation is a time-consuming process, and using this full data set could take hours to complete. For purposes of quickly evaluating changes, it’s convenient to reduce this value. But using too little data might compromise the accuracy of the evaluation results. The parameter 0.7 simply says to build a model to evaluate with 70 percent of the data, and then test with the remaining 30 percent.
--Another important part of user-based recommenders is the User Similarity implementation
•	PearsonCorrelationSimilarity implementation, which is a similarity metric based on the Pearson correlation. The Pearson correlation is a number between –1 and 1 that measures the tendency of two series of numbers, paired up one-to-one, to move together. That is to say, it measures how likely a number in one series is to be relatively large when the corresponding number in the other series is high, and vice versa. It measures the tendency of the numbers to move together proportionally, such that there’s a roughly linear relationship between the values in one series and the other. When this tendency is high, the correlation is close to 1. When there appears to be little relationship at all, the value is near 0. When there appears to be an opposing relationship—one series’ numbers are high exactly when the other series’ numbers are low—the value is near –1.[4].

#Alternative approach
•	All the 4 similarity measures (Spearman Correlation, Euclidean distance, Tanimoto Co-efficient, Log Likelihood) are used and tested with the newly generated trust file along with the Pearson Correlation method which is used in the study. Results of all the algorithms are compared in the results section.
•	 All the 5 similarities are used and tested with only the user rating file, without the trust relation file. Results are marked and compared in the results section.
## Defining similarity by Euclidean distance 
Let’s try EuclideanDistanceSimilarity—swap in this implementation by changing the UserSimilarity implementation used in recommender program to new EuclideanDistanceSimilarity(model) instead. This implementation is based on the distance between users. This idea makes sense if you think of users as points in a space of many dimensions (as many dimensions as there are items), whose coordinates are preference values. This similarity metric computes the Euclidean distance d between two such user points.2 this value alone doesn’t constitute a valid similarity metric, because larger values would mean more-distant and therefore less similar, users. The value should be smaller when users are more similar. Therefore, the implementation actually returns 1 / (1+d).. You can verify that when the distance is 0 (users have identical preferences) the result is 1, decreasing to 0 as d increases. This similarity metric never returns a negative value, but larger values still mean more similarity [4].
## Defining similarity by relative rank with the Spearman correlation
The Spearman correlation is an interesting variant on the Pearson correlation, for our purposes. Rather than compute a correlation based on the original preference values, it computes a correlation based on the relative rank of preference values. Imagine that, for each user, their least-preferred item’s preference value is overwritten with a 1. Then the next-least-preferred item’s preference value is changed to 2, and so on. This implementation is far slower because it must do some nontrivial work to compute and store these ranks. The Spearman correlation–based similarity metric is expensive to compute, and is therefore possibly more of academic interest than practical use. For some small data sets, though, it may be desirable..[4]
## Ignoring preference values in similarity with the Tanimoto coefficient
 Interestingly, there are also UserSimilarity implementations that ignore preference values entirely. They don’t care whether a user expresses a high or low preference for an item—only that the user expresses a preference at all. TanimotoCoefficientSimilarity is one such implementation, based on (surprise) the Tanimoto coefficient. This value is also known as the Jaccard coefficient. It’s the number of items that two users express some preference for, divided by the number of items that either user expresses some preference for. In other words, it’s the ratio of the size of the intersection to the size of the union of their preferred items. It has the required properties: when two users’ items completely overlap, the result is 1.0. When they have nothing in common, it’s 0.0. The value is never negative, but that’s OK. It’s possible to expand the results into the range –1 to 1 with some simple math: similarity = 2 • similarity – 1. It won’t matter to the framework. Note that this similarity metric doesn’t depend only on the items that both users have some preference for, but that either user has some preference for. Hence, all seven items appear in the calculation, unlike before. You’re likely to use this similarity metric if, and only if, your underlying data contains only Boolean preferences and you have no preference values to begin with. Ifyou do have preference values, you might use this metric because you believe there’s more signal than noise in the values. You would usually do better with a metric that uses the preference information. [4].
## Computing smarter similarity with a log-likelihood test
Log-likelihood–based similarity is similar to the Tanimoto coefficient–based similarity, though it’s more difficult to understand intuitively. It’s another metric that doesn’t take account of individual preference values. Like the Tanimoto coefficient, it’s based on the number of items in common between two users, but its value is more an expression of how unlikely it is for two users to have so much overlap, given the total number of items out there and the number of items each user has a preference for. The log likelihood is trying to assess how unlikely it is that the overlap between the two users is just due to chance. That is to say, two dissimilar users will no doubt happen to rate a couple movies in common, but two similar users will show an overlap that looks quite unlikely to be mere chance. With some statistical tests, this similarity metric attempts to determine just how strongly unlikely it is that two users have no resemblance in their tastes; the more unlikely, the more similar the two should be. The resulting similarity value may be interpreted as a probability that the overlap isn’t due to chance..[4]
