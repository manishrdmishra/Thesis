
rootImagePath = '/home/manish/git/Documents/fifth_sem/thesis/restult_folder/matlab_predicted_images_processed';


healthyFolderName = 'results-2';
tumorFolderName = 'results-3';
roiFolderName = 'roiFolder';
imageDirectories = dir(rootImagePath);

for i = 3:size(imageDirectories , 1)
    roiFolder = fullfile(rootImagePath,imageDirectories(i).name,roiFolderName);
    healthyFolderPath = fullfile(rootImagePath,imageDirectories(i).name,healthyFolderName);
    tumorFolderPath = fullfile(rootImagePath,imageDirectories(i).name,tumorFolderName);
    
    healthyProbabilityFiles = dir(healthyFolderPath);
    tumorProbabilityFiles = dir(tumorFolderPath);
    roiFiles = dir(roiFolder);
    
    for j = 3:size(healthyProbabilityFiles,1)
        
        roiFile = fullfile(roiFolder,roiFiles(j).name);
        healthyProbabilityFile = fullfile(healthyFolderPath,healthyProbabilityFiles(i).name);
        tumorProbabilityFile = fullfile(tumorFolderPath,tumorProbabilityFiles(i).name);
        healthyProbability = imread(healthyProbabilityFile);
        tumorProbability = imread(tumorProbabilityFile);
        calculateImageLevelScores(roiFile , healthyProbability ,tumorProbability);
    end
 end