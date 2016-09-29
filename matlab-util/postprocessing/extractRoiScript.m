%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% This script extract the ROIs for all cell lines kept at rootImagePath. 
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%



%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Root path of the directory is where all predicted images are kept for one or
% more cell lines.
% for example if the dir is /rootDir and let us assume the cell line is Fao
% then the directory structure will look as mentioned below.
% /rootDir/Fao/results-1  , /rootDir/Fao/results-2 , /rootDir/results-3 ,
% rootDir/Fao/roiFolder , rootDir/projected_scores
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
rootImagePath = '/home/manish/git/Documents/fifth_sem/thesis/restult_folder/matlab_predicted_images_processed';

backgroundFolderName = 'results-1';
roiFolderName = 'roiFolder';
imageDirectories = dir(rootImagePath);

% iterate over all the cell lines
for i = 3:size(imageDirectories , 1)
    imageFolderPath = fullfile(rootImagePath,imageDirectories(i).name,backgroundFolderName);
    
    % In this directory the ROI will be saved 
    roiFolder = fullfile(rootImagePath,imageDirectories(i).name,roiFolderName);
    
    % extract the ROI kept in imageFolderPath directory 
    extractRoi(imageFolderPath, roiFolder);
end