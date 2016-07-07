%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%% This script converts the probability map into a segmented map 
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

%% path where the predicted map will be saved as png images
resultPath = '/home/manish/git/Documents/fifth_sem/thesis/result';

folderNames = dir(resultPath);
counter = 0;


for i = 3:size(folderNames , 1)
    folderName = fullfile(resultPath,folderNames(i).name);
    fileNames = dir (folderName);
       % iterate over the one label images
    for j = 3:size(fileNames,1)
        file = fullfile(folderName, fileNames(j).name);
        
        %% print the file name in log file
        %fprintf(fileID, '%30s \n', fileNames(j).name);
        disp(fileNames(j).name)
                       
        image(i,j) = imread(file);
        
    end
end