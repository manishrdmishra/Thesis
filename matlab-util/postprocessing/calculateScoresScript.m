%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% This script calculate score for each mitochondria and
% write this score and the center of this mitchondria to
% a text file. After calculating all the score for extracted
% mitochondria the scores can be projected to image also.
%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%



%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Root path of the directory is where all predicted images are kept for one or
% more cell lines.
% for example if the dir is /rootDir and let us assume the cell line is Fao
% then the directory structure will look as mentioned below.
% /rootDir/Fao/results-1  , /rootDir/Fao/results-2 , /rootDir/results-3 ,
% rootDir/Fao/roiFolder , rootDir/projected_scores
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
rootPredictedImagePath = '/home/manish/git/Documents/fifth_sem/thesis/restult_folder/matlab_predicted_images_processed';

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% When we do projection of mitochondria score on images then we need the
% the path of raw images.
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
rootImagePath = '/home/manish/git/Documents/fifth_sem/thesis/restult_folder/matlab_all_images';

% Name of the directory where healthy probability predicted images are
% kept.
healthyFolderName = 'results-2';

% Name of the directory where tumor probability predicted images are
% kept.
tumorFolderName = 'results-3';

% Name of the directory where extracted roi files are
% kept.

roiFolderName = 'roiFolder';

% Name of the directory where images will be saved after projecting scores
projectedScoresFolderName = 'projected_scores';

imageDirectories = dir(rootPredictedImagePath);

rawImageDirectories = dir(rootImagePath);

% iterate over all the cell lines
for i = 3:size(imageDirectories , 1)
    
    % create a file named "scores.txt"
    textFileName = 'scores.txt';
    textFilePath = fullfile(rootPredictedImagePath,imageDirectories(i).name,textFileName);
    fileID = fopen(textFilePath,'w');
    
    roiFolder = fullfile(rootPredictedImagePath,imageDirectories(i).name,roiFolderName);
    healthyFolderPath = fullfile(rootPredictedImagePath,imageDirectories(i).name,healthyFolderName);
    tumorFolderPath = fullfile(rootPredictedImagePath,imageDirectories(i).name,tumorFolderName);
    projecteScoreFolderPath = fullfile(rootPredictedImagePath,imageDirectories(i).name,projectedScoresFolderName);
    
    % raw image folder
    rawImageFolderPath = fullfile(rootImagePath,rawImageDirectories(i).name);
    
    healthyProbabilityFiles = dir(healthyFolderPath);
    tumorProbabilityFiles = dir(tumorFolderPath);
    roiFiles = dir(roiFolder);
    rawFiles = dir(rawImageFolderPath);
    
    % iterate over all the images in a cell line
    for j = 3:size(healthyProbabilityFiles,1)
        
        roiFile = fullfile(roiFolder,roiFiles(j).name);
        healthyProbabilityFile = fullfile(healthyFolderPath,healthyProbabilityFiles(i).name);
        tumorProbabilityFile = fullfile(tumorFolderPath,tumorProbabilityFiles(i).name);
        rawFile = fullfile(rawImageFolderPath,rawFiles(j).name);
        
        healthyProbability = imread(healthyProbabilityFile);
        tumorProbability = imread(tumorProbabilityFile);
        
        % calculate the score for each mitochondria
        [mitochondriaScores, centerOfPatches ] = calculateImageLevelScores(roiFile , healthyProbability ,tumorProbability,fileID);
        
        
        % calulate the tumor score for an image
        imageScore = mean(mitochondriaScores,1);
        
        % calculate the heterogeneity score for an image
        heterogeneityScore  = std(mitochondriaScores,1);
        
        % print the relevant information to file
        fprintf(fileID,'done!!\n');
        fprintf(fileID,'file : %s\n' ,healthyProbabilityFiles(i).name);
        fprintf(fileID, 'image score : %f\n' ,imageScore);
        fprintf(fileID, 'heterogeneity score : %f\n',  heterogeneityScore);
        
        
        % project the score of each mitochondria on the raw image
        projectScore(projecteScoreFolderPath,rawFile, mitochondriaScores, centerOfPatches)
    end
end



