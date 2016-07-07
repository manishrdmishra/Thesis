%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% This script converts images whose pixel values are between 0 and 1 to a
% gray scale image and save the image in png format.
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

%% path where the predicted map is saved
%predictedImageFolderPath = '/home/manish/git/Documents/fifth_sem/thesis/prediction';

predictedImageFolderPath = '/home/manish/git/Documents/fifth_sem/thesis/post_processed_images/max_probabilitymap_images';
%% path where the predicted map will be saved as png images
resultPath = '/home/manish/git/Documents/fifth_sem/thesis/post_processed_images';

folderNames = dir(predictedImageFolderPath);
counter = 0;

for i = 3:size(folderNames , 1)
    folderName = fullfile(predictedImageFolderPath,folderNames(i).name);
    fileNames = dir (folderName);
    %% create folders to save the resulted images
    resultFolderName = fullfile(resultPath,strcat('results-',num2str(i - 2)));
    mkdir(resultFolderName);
    % iterate over the one label images
    for j = 3:size(fileNames,1)
        file = fullfile(folderName, fileNames(j).name);
        
        %% print the file name in log file
        %fprintf(fileID, '%30s \n', fileNames(j).name);
        disp(fileNames(j).name)
                       
        image = imread(file);
        [pathstr,name,ext] = fileparts(file);
        newFile = fullfile(resultFolderName,[name '.png']);
        
        disp(newFile)
        imwrite(image,newFile);
    end
end

 