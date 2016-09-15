function [imageScore, heterogeneityScore ] = calculateImageLevelScores(roiFile , healthyProbabilty ,tumorProbability)

load(roiFile,'roi');
numOfObjects = size(roi,2);
mitochondriaScores = zeros(numOfObjects,1);

for i = 1 : numOfObjects
    mitochondriaScores(i) = calculateMitochondriaScore(roi(i), healthyProbabilty , tumorProbability);
end
imageScore = mean(mitochondriaScores,1);
heterogeneityScore  = std(mitochondriaScores,1);

fprintf('image score : %lf\n' ,imageScore);
fprintf('heterogeneity score : %lf',  heterogeneityScore);


end

function [ score, centroid ] = calculateMitochondriaScore(roi,healthyProbabilty, tumorProbability )
%CALCULATEMITOCHONDRIASCORE Summary of this function goes here
%   Detailed explanation goes here

totalScore = 0;
for i = 1:size(roi.x , 1)
    healthy = int( healthyProbabilty(roi.x(i), roi.y(i)) - tumorProbability(roi.x(i), roi.y(i)));
    tumor = tumorProbability(roi.x(i), roi.y(i)) - healthyProbabilty(roi.x(i), roi.y(i));
    
    totalScore = totalScore + exp(tumor) / ( exp( tumor) + exp (healthy) );
end

score =  (totalScore / size(roi.x,1));

fprintf('mitochondria score : %lf\n' ,score);
centroid = mean(roi);

end

