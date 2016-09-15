%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% This script converts images whose pixel values are between 0 and 1 to a
% gray scale image and save the image in png format.
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

%% path where the predicted map is saved
%predictedImageFolderPath = '/home/manish/git/Documents/fifth_sem/thesis/prediction';

predictedImageFolderPath = '/home/manish/git/Documents/fifth_sem/thesis/restult_folder/all_images/human';
%% path where the predicted map will be saved as png images
resultPath = '/home/manish/git/Documents/fifth_sem/thesis/restult_folder/all_images/human_png';

folderNames = dir(predictedImageFolderPath);
counter = 0;

for i = 3:size(folderNames , 1)
    
    fileName =    fullfile(predictedImageFolderPath, folderNames(i).name);
    
    %% print the file name in log file
    %fprintf(fileID, '%30s \n', fileNames(j).name);
    disp(folderNames(i).name)
    
    image = imread(fileName);
    if(size(image,3) == 4)
        image = image(:,:,1:3);
        
    elseif (size(image,3) == 1)
        image = cat(3, image, image, image);
    end
    
    [pathstr,name,ext] = fileparts(fileName);
    newFile = fullfile(resultPath,[name '.png']);
    
    disp(newFile)
    imwrite(image,newFile);
end
