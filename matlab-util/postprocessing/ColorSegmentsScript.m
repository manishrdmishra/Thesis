%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Root path of the directory is where all predicted images are kept for one or
% more cell lines.
% for example if the dir is /rootDir and let us assume the cell line is Fao
% then the directory structure will look as mentioned below.
% /rootDir/Fao/results-1  , /rootDir/Fao/results-2 , /rootDir/results-3 ,
% rootDir/Fao/roiFolder , rootDir/projected_scores
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%rootPredictedImagePath = '/home/manish/git/Documents/fifth_sem/thesis/restult_folder/matlab_util_postprocessed';
rootPredictedImagePath = '/home/manish/git/Documents/fifth_sem/thesis/restult_folder/malis_loss_results';
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
segmentedFolderName = 'colored_segmented_images';



imageDirectories = dir(rootPredictedImagePath);

rawImageDirectories = dir(rootImagePath);

% iterate over all the cell lines
for i = 3:size(imageDirectories , 1)
    
   
    % create a file named "scores.txt"
   
    
    roiFolder = fullfile(rootPredictedImagePath,imageDirectories(i).name,roiFolderName);
    healthyFolderPath = fullfile(rootPredictedImagePath,imageDirectories(i).name,healthyFolderName);
    tumorFolderPath = fullfile(rootPredictedImagePath,imageDirectories(i).name,tumorFolderName);
    segmentedFolderPath = fullfile(rootPredictedImagePath,imageDirectories(i).name,segmentedFolderName);
    
    % raw image folder
    rawImageFolderPath = fullfile(rootImagePath,rawImageDirectories(i).name);
    
    healthyProbabilityFiles = dir(healthyFolderPath);
    tumorProbabilityFiles = dir(tumorFolderPath);
    roiFiles = dir(roiFolder);
    rawFiles = dir(rawImageFolderPath);
    
    % iterate over all the images in a cell line
    for j = 3:size(healthyProbabilityFiles,1)
    % fprintf('file : %s\n' ,healthyProbabilityFiles(j).name);
       
        roiFile = fullfile(roiFolder,roiFiles(j).name);
        healthyProbabilityFile = fullfile(healthyFolderPath,healthyProbabilityFiles(j).name);
        tumorProbabilityFile = fullfile(tumorFolderPath,tumorProbabilityFiles(j).name);
        rawFile = fullfile(rawImageFolderPath,rawFiles(j).name);
        
       % healthyProbability = imread(healthyProbabilityFile);
        %tumorProbability = imread(tumorProbabilityFile);
        
        % calculate the score for each mitochondria
      ColorSegmentsOfImage(roiFile ,rawFile, healthyProbabilityFile ,tumorProbabilityFile,segmentedFolderPath);
        
        
        
    end
end



