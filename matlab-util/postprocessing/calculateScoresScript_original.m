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
%rootRoiPath = '/home/manish/git/Documents/fifth_sem/thesis/restult_folder/malis_loss_results';
%rootPredictedImagePath = '/home/manish/git/Documents/fifth_sem/thesis/restult_folder/matlab_util_postprocessed';
rootPredictedImagePath = '/home/manish/git/Documents/fifth_sem/thesis/restult_folder/malis_loss_results';
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% When we do projection of mitochondria score on images then we need the
% the path of raw images.
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
rootImagePath = '/home/manish/git/Documents/fifth_sem/thesis/restult_folder/matlab_all_images';



% Name of the directory where background probability predicted images are
% kept.

backgroundFolderName = 'results-1';

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

% Name of the directory where all the score files are saved
scoreFolderName = 'scores';

%rootRoiDirectories = dir(rootPredictedImagePath);
predImageDirectories = dir(rootPredictedImagePath);
predImageDirectories = predImageDirectories(arrayfun(@(x)x.name(1),predImageDirectories)~='.');
rawImageDirectories = dir(rootImagePath);
rawImageDirectories = rawImageDirectories(arrayfun(@(x)x.name(1),rawImageDirectories)~='.');

% iterate over all the cell lines
for i = 1:size(rawImageDirectories , 1)
    
   
    % create a file named "scores.txt"
    textFileName = 'scores.txt';
    textFilePath = fullfile(rootPredictedImagePath,predImageDirectories(i).name,scoreFolderName,textFileName);
    fileID = fopen(textFilePath,'w');
    
    roiFolder = fullfile(rootPredictedImagePath,predImageDirectories(i).name,roiFolderName);
   
    backgroundFolderPath = fullfile(rootPredictedImagePath,predImageDirectories(i).name,backgroundFolderName);
    healthyFolderPath = fullfile(rootPredictedImagePath,predImageDirectories(i).name,healthyFolderName);
    tumorFolderPath = fullfile(rootPredictedImagePath,predImageDirectories(i).name,tumorFolderName);
    
    projecteScoreFolderPath = fullfile(rootPredictedImagePath,predImageDirectories(i).name,projectedScoresFolderName);
    
    % raw image folder
    rawImageFolderPath = fullfile(rootImagePath,rawImageDirectories(i).name);
    
%     backgroundProbabilityFiles = dir(backgroundFolderPath);
%     healthyProbabilityFiles = dir(healthyFolderPath);
%     tumorProbabilityFiles = dir(tumorFolderPath);
%     roiFiles = dir(roiFolder);
    rawFiles = dir(rawImageFolderPath);
    rawFiles = rawFiles(arrayfun(@(x)x.name(1),rawFiles)~='.');
    
    % iterate over all the images in a cell line
    for j = 1:size(rawFiles,1)
    % fprintf('file : %s\n' ,healthyProbabilityFiles(j).name);
        fprintf(fileID,'file : %s\n' ,rawFiles(j).name);
        roiFile_name = strrep(rawFiles(j).name,'.png','-roi.mat');
        roiFile = fullfile(roiFolder,roiFile_name);
        probabilityFile_name = strrep(rawFiles(j).name,'.png','.tif');
        backgroundProbabilityFile = fullfile(backgroundFolderPath,probabilityFile_name);
        healthyProbabilityFile = fullfile(healthyFolderPath,probabilityFile_name);
        tumorProbabilityFile = fullfile(tumorFolderPath,probabilityFile_name);
        rawFile = fullfile(rawImageFolderPath,rawFiles(j).name);
        if ~((exist(rawFile,'file')==2)&&(exist(roiFile,'file')==2)&&(exist(healthyProbabilityFile,'file')==2)&&(exist(tumorProbabilityFile,'file')==2)&&(exist(backgroundProbabilityFile,'file')==2))
            disp(sprintf('%s%s%s','one of results of',rawFiles(j).name,'is not present'));
        else

        
       % healthyProbability = imread(healthyProbabilityFile);
        %tumorProbability = imread(tumorProbabilityFile);
        
        % calculate the score for each mitochondria
        [mitochondriaScores, centerOfPatches ] = calculateImageLevelScores(roiFile ,  backgroundProbabilityFile, healthyProbabilityFile ,tumorProbabilityFile,fileID);
        
        
        % calulate the tumor score for an image
        imageScore = mean(mitochondriaScores,2);
        
        % calculate the heterogeneity score for an image
        heterogeneityScore  = std(mitochondriaScores,1);
        
        % print the relevant information to file
        fprintf(fileID,'done!!\n');
       % fprintf(fileID,'file : %s\n' ,healthyProbabilityFiles(i).name);
        fprintf(fileID, '%s image score : %f\n' , rawFiles(i).name, imageScore);
        fprintf(fileID, '%s heterogeneity score : %f\n', rawFiles(i).name, heterogeneityScore);
        
        
        % project the score of each mitochondria on the raw image
        projectScore(projecteScoreFolderPath,roiFile,rawFile, mitochondriaScores, centerOfPatches)
        end
    end
end



