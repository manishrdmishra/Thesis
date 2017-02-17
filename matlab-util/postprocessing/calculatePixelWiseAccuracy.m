rootTestImagePath = '/home/manish/git/Documents/fifth_sem/thesis/restult_folder/all_test_images';

rootPredictedImagePath = '/home/manish/git/Documents/fifth_sem/thesis/restult_folder/all_test_images_predicted';

% Name of the directory where background probability predicted images are
% kept.

backgroundFolderName = 'results-1';

% Name of the directory where healthy probability predicted images are
% kept.
healthyFolderName = 'results-2';

% Name of the directory where tumor probability predicted images are
% kept.
tumorFolderName = 'results-3';

% Name of the directory where all the score files are saved
scoreFolderName = 'scores';



testImageDirectories = dir(rootTestImagePath);
testImageDirectories = testImageDirectories(arrayfun(@(x)x.name(1),testImageDirectories)~='.');



predImageDirectories = dir(rootPredictedImagePath);
predImageDirectories = predImageDirectories(arrayfun(@(x)x.name(1),predImageDirectories)~='.');

% iterate over all the cell lines
for i = 1:size(testImageDirectories , 1)
    
    % create a file named "scores.txt"
    textFileName = 'pixel_scores.txt';
    textFilePath = fullfile(rootPredictedImagePath,predImageDirectories(i).name,scoreFolderName,textFileName);
    fileID = fopen(textFilePath,'w');
    
    
    % test image folder
    testImageFolderPath = fullfile(rootTestImagePath,testImageDirectories(i).name);
    
    backgroundFolderPath = fullfile(rootPredictedImagePath,predImageDirectories(i).name,backgroundFolderName);
    healthyFolderPath = fullfile(rootPredictedImagePath,predImageDirectories(i).name,healthyFolderName);
    tumorFolderPath = fullfile(rootPredictedImagePath,predImageDirectories(i).name,tumorFolderName);
    
    
    
    
    testFiles = dir(testImageFolderPath);
    testFiles = testFiles(arrayfun(@(x)x.name(1),testFiles)~='.');
    
    % iterate over all the test images in a cell line
    for j = 1:size(testFiles,1)
        % fprintf('file : %s\n' ,healthyProbabilityFiles(j).name);
        fprintf(fileID,'file : %s\n' ,testFiles(j).name);
        
        
        
        probabilityFile_name = strrep(testFiles(j).name,'.tif-seg.tif','.tif');
        
        testFile = fullfile(testImageFolderPath,testFiles(j).name);
        
        backgroundProbabilityFile = fullfile(backgroundFolderPath,probabilityFile_name);
        healthyProbabilityFile = fullfile(healthyFolderPath,probabilityFile_name);
        tumorProbabilityFile = fullfile(tumorFolderPath,probabilityFile_name);
        
        [confusion_matrix] =  calculatePixelClassificationAccuracyOfImage(testFile , backgroundProbabilityFile, healthyProbabilityFile ,tumorProbabilityFile, fileID);
        
        
        % count total number of pixels for each class
        background_pixel_count = sum(confusion_matrix(1,:));
        healthy_pixel_count = sum(confusion_matrix(2,:));
        tumor_pixel_count = sum(confusion_matrix(3,:));
        
        background_accuracy = 0;
        healthy_accuracy = 0;
        tumor_accuracy = 0;
        if( background_pixel_count > 0 )
            background_accuracy = confusion_matrix(1,1)/  background_pixel_count;
        end
        if (  healthy_pixel_count > 0 )
            healthy_accuracy = confusion_matrix(2,2)/ healthy_pixel_count;
        end
        if ( tumor_pixel_count > 0 )
            tumor_accuracy = confusion_matrix(3,3) / tumor_pixel_count;
        end
        
        
        fprintf(fileID,'file : background accuracy  : %f\n' ,background_accuracy);
        fprintf(fileID,'file : healthy accuracy : %f\n' ,healthy_accuracy);
        fprintf(fileID,'file : tumor accuracy : %f\n' ,tumor_accuracy);
    end
   
    
end