function [mitochondriaScores, centerOfPatches ] = calculateImageLevelScores(roiFile , healthyProbabilty ,tumorProbability)

load(roiFile,'roi');
numOfObjects = size(roi,2);
mitochondriaScores = zeros(numOfObjects,1);

centerOfPatches = zeros(numOfObjects,2);


for i = 1 : numOfObjects
    [score , centroid] = calculateMitochondriaScore(roi(i), healthyProbabilty , tumorProbability);
    mitochondriaScores(i) = score;
    centerOfPatches(i,1) = centroid.x;
    centerOfPatches(i,2) =  centroid.y;
    
end


end

function [ score, centroid ] = calculateMitochondriaScore(roi,healthyProbabilty, tumorProbability )
%CALCULATEMITOCHONDRIASCORE Summary of this function goes here
%   Detailed explanation goes here

totalScore = 0;
for i = 1:size(roi.x , 1)
    healthy = ( healthyProbabilty(roi.y(i), roi.x(i)) - tumorProbability(roi.y(i), roi.x(i)));
    tumor = tumorProbability(roi.y(i), roi.x(i)) - healthyProbabilty(roi.y(i), roi.x(i));
    
    totalScore = totalScore + exp(tumor) / ( exp( tumor) + exp (healthy) );
end

score =  (totalScore / size(roi.x,1));
centroid.x = mean(roi.x);
centroid.y = mean(roi.y);
fprintf('mitochondria x : %f, y : %f\n' ,centroid.x , centroid.y);
fprintf('mitochondria score : %f\n' ,score);



end

