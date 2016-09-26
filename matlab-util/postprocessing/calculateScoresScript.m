
rootPredictedImagePath = '/home/manish/git/Documents/fifth_sem/thesis/restult_folder/matlab_predicted_images_processed';
rootImagePath = '/home/manish/git/Documents/fifth_sem/thesis/restult_folder/matlab_all_images';

healthyFolderName = 'results-2';
tumorFolderName = 'results-3';
roiFolderName = 'roiFolder';
projectedScoresFolderName = 'projected_scores';

imageDirectories = dir(rootPredictedImagePath);

rawImageDirectories = dir(rootImagePath);

for i = 3:size(imageDirectories , 1)
    
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
    
    
    for j = 3:size(healthyProbabilityFiles,1)
        
        roiFile = fullfile(roiFolder,roiFiles(j).name);
        healthyProbabilityFile = fullfile(healthyFolderPath,healthyProbabilityFiles(i).name);
        tumorProbabilityFile = fullfile(tumorFolderPath,tumorProbabilityFiles(i).name);
        rawFile = fullfile(rawImageFolderPath,rawFiles(j).name);
        
        healthyProbability = imread(healthyProbabilityFile);
        tumorProbability = imread(tumorProbabilityFile);
        
         fprintf('file : %s\n' ,healthyProbabilityFiles(i).name);
        [mitochondriaScores, centerOfPatches ] = calculateImageLevelScores(roiFile , healthyProbability ,tumorProbability);
        
        
        
        imageScore = mean(mitochondriaScores,1);
        heterogeneityScore  = std(mitochondriaScores,1);
        
        fprintf('image score : %f\n' ,imageScore);
        fprintf('heterogeneity score : %f',  heterogeneityScore);
        
        % print scores to file 
        textFileName = 'scores.txt';
        textFilePath = fullfile(rootPredictedImagePath,imageDirectories(i).name,textFileName);
       % printScoresToFile(textFilePath,mitochondriaScores, centerOfPatches );
        projectScore(projecteScoreFolderPath,rawFile, mitochondriaScores, centerOfPatches)
    end
end



