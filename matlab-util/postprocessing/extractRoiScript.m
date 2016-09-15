

rootImagePath = '/home/manish/git/Documents/fifth_sem/thesis/restult_folder/matlab_predicted_images_processed';

backgroundFolderName = 'results-1';
roiFolderName = 'roiFolder';
imageDirectories = dir(rootImagePath);

for i = 3:size(imageDirectories , 1)
    imageFolderPath = fullfile(rootImagePath,imageDirectories(i).name,backgroundFolderName);
    roiFolder = fullfile(rootImagePath,imageDirectories(i).name,roiFolderName);
    extractRoi(imageFolderPath, roiFolder);
end