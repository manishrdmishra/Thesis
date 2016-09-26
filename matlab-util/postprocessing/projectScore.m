function[]= projectScore(pathToSave, imageName, mitochondriaScores, centerOfPatches)

image = imread(imageName);

numOfOjbjects = size(mitochondriaScores,1);
textColor = cell(1,numOfOjbjects);
imageGray = rgb2gray(image);
for i = 1:size(mitochondriaScores,1)
    if(mitochondriaScores(i) > 0.5)
        textColor(i) =  cellstr('red');
        
    else
        textColor(i) = cellstr('green');
    end
end
imageWithProbability = insertText(imageGray,centerOfPatches(:,:), mitochondriaScores(:,1),'TextColor', textColor,'BoxColor','white','AnchorPoint','center');
[pathstr,name,ext] = fileparts(imageName);
name = strcat(name,'.png');
finalFileName = fullfile(pathToSave, name);
imwrite(imageWithProbability,finalFileName,'png');
end
