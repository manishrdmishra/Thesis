function [ score, centroid ] = calculateMitochondriaScore(roi, backgroundProbability, healthyProbability, tumorProbability)
%CALCULATE MITOCHONDRIA SCORE Summary of this function goes here
%   Detailed explanation goes here

totalScore = 0;
backgroundScore = 0;
healthyScore = 0;
tumorScore = 0;
% pixelScore = exp(tumorProbability-healthyProbability)/( exp(tumorProbability-healthyProbability)+ exp(healthyProbability-tumorProbability));
% backgroundPixelProbability = 0;
% healthyPixelProbability = 0;
% tumorPixelProbability = 0;

% iterate over all the pixels in a ROI 
for i = 1:size(roi.x , 1)
    backgroundScore =   backgroundScore + backgroundProbability(roi.y(i), roi.x(i));
     healthyScore =  healthyScore + healthyProbability(roi.y(i), roi.x(i));
     tumorScore =  tumorScore + tumorProbability(roi.y(i), roi.x(i));
   
     healthy = ( healthyProbability(roi.y(i), roi.x(i)) - tumorProbability(roi.y(i), roi.x(i)));
     tumor = tumorProbability(roi.y(i), roi.x(i)) - healthyProbability(roi.y(i), roi.x(i));
    
     totalScore = totalScore + exp(tumor) / ( exp( tumor) + exp (healthy) );
%      totalScore = totalScore + pixelScore(roi.y(i), roi.x(i));
end
% 
% mitochondriascore = mean(mean(pixelscore(roi.x,roi.y)));
% mitochondriabackground= mean(mean(backgroundProbability(roi.x,roi.y)));


% store score of single mitochondria 
if( backgroundScore<max(healthyScore,tumorScore))
    %score = backgroundScore/size(roi.x,1);
    score =  totalScore/size(roi.x,1);
else
   score = 0;
end
% store centroid of single mitochondria 
centroid.x = mean(roi.x);
centroid.y = mean(roi.y);


%fprintf(fileID, 'mitochondria center x : %f, y : %f\n' ,centroid.x , centroid.y);
%fprintf(fileID, 'mitochondria score : %f\n' ,score);





end

