function [mitochondriaScores, centerOfPatches ] = calculateImageLevelScores(roiFile , healthyProbabilty ,tumorProbability, fileID)
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% This function calcualte the score of single mitochondria and its 
% centroid. 
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%


load(roiFile,'roi');
numOfObjects = size(roi,2);
mitochondriaScores = zeros(numOfObjects,1);

centerOfPatches = zeros(numOfObjects,2);

% iterate over all the ROIs corresponding to an image
for i = 1 : numOfObjects
    [score , centroid] = calculateMitochondriaScore(roi(i), healthyProbabilty , tumorProbability, fileID);
    mitochondriaScores(i) = score;
    centerOfPatches(i,1) = centroid.x;
    centerOfPatches(i,2) =  centroid.y;
    
end


end

function [ score, centroid ] = calculateMitochondriaScore(roi,healthyProbabilty, tumorProbability, fileID )
%CALCULATE MITOCHONDRIA SCORE Summary of this function goes here
%   Detailed explanation goes here

totalScore = 0;

% iterate over all the pixels in a ROI 
for i = 1:size(roi.x , 1)
    healthy = ( healthyProbabilty(roi.y(i), roi.x(i)) - tumorProbability(roi.y(i), roi.x(i)));
    tumor = tumorProbability(roi.y(i), roi.x(i)) - healthyProbabilty(roi.y(i), roi.x(i));
    
    totalScore = totalScore + exp(tumor) / ( exp( tumor) + exp (healthy) );
end

% store score of single mitochondria 
score =  (totalScore / size(roi.x,1));

% store centroid of single mitochondria 
centroid.x = mean(roi.x);
centroid.y = mean(roi.y);


fprintf(fileID, 'mitochondria center x : %f, y : %f\n' ,centroid.x , centroid.y);
fprintf(fileID, 'mitochondria score : %f\n' ,score);



end

