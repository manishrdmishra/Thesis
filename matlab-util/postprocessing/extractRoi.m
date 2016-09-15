function[] =  extractRoi(imageFolderPath, roiFolder)

imageNames = dir(imageFolderPath);


for i = 3:size(imageNames , 1)
    imagePath = fullfile(imageFolderPath,imageNames(i).name);
    %imageName = '/home/manish/git/Documents/fifth_sem/thesis/restult_folder/all_predicted_images_processed/tumour_seg_png/results-1/149-12b_20000_1.png';
    I = imread(imagePath);
    processedImage = processImage(I);
    [labelMatrix, numOfObjects] =  labelObjects(processedImage);
    saveRoi(labelMatrix, numOfObjects,roiFolder,imageNames(i).name);
%     displayLabledObjects(processedImage);
    
    % imshow(BW)
end
end

function [processedImage] = processImage(image)

%     level = graythresh(I);
%     BW = im2bw(I,level);
% Binarize the image
BW = imbinarize(image);
% BW = imcomplement(BW);
% BW = imfill(BW,'holes');
% BW = bwmorph(BW,'clean');
% D = bwdist(~BW); % image B (above)
% D = -bwdist(~BW); % image C (above)
% L = watershed(D);
% BW(L == 0) = 0;
% imshow(BW) % Segmented image D (above)

BW = imcomplement(BW);
% figure ;
% imshow(BW);
% Fill the holes to complete the
% mitochondria

BW2 = bwmorph(BW,'fill',5);
% figure;
% imshow(BW2);% Fill the holes to complete the
% mitochondria
BW2 = imfill(BW2,'holes');
BW2 = bwmorph(BW2,'clean',1);
% BW2 = bwmorph(BW2,'shrink',20);
% BW2  = bwmorph(BW2,'thicken',10);
% BW2 = bwareaopen(BW2,1000);
% figure ; imshow(BW2);
% title('cleaned Image')
processedImage = BW2;

end

function[labelMatrix , numOfObjects ] = labelObjects(binaryImage)


% I3 = imhmin(BW,20); %20 is the height threshold for suppressing shallow minima
% L = watershed(I3);



% labels = watershed(BW2);
% imshow(label2rgb(labels,'jet','w'))
% %BW2 = bwmorph(BW2,'shrink',15);
% figure;
% imshow(BW2);
% title('shrinked Image')

% create the objects on the basis
% of given criterion
BW3 = bwpropfilt(binaryImage,'area',[1000 10000000]);
% show the image with extracted objects image
figure ;
imshow(BW3);
title('extracted image');
% label the connected components
[labelMatrix , numOfObjects ] = bwlabel(BW3);

%         figure , imcontour(BW3);
fprintf('num of mitochondria : %d \n',numOfObjects);

end

function[] = saveRoi(labelMatrix, numOfObjects,roiFolder,imageName)
maxRows = 10000;
maxCols = 10000;
% roi.x = zeros(numOfObjects,maxRows,1);
% roi.y = zeros(numOfObjects,maxCols,1);



% roi.x=zeros(maxRows,maxCols);
% roi.y = zeros(maxRows,maxCols);

for i = 1:numOfObjects
    % to find the row and column for 2nd object
    [r,c] = find(labelMatrix == i);
    roi(i).x= r;
    roi(i).y= c;
   
    %     roi(i) = c;
    % BW = roicolor(L,1,1); % it will generate roi corresponding
    %  to object 1
end
[pathstr,name,ext] = fileparts(imageName);
roifileName = strcat(name,'-roi.mat');
roiFilePath = fullfile(roiFolder,roifileName);
fprintf('saving the roi file -  %s \n',roiFilePath);
save(roiFilePath,'roi');
end

function[] = displayLabledObjects(binaryImage)         %
% Overlay Region Boundaries on Image and Annotate with Region Numbers
%O

%BW = imread('blobs.png');
[B,L,N,A] = bwboundaries(binaryImage);
imshow(binaryImage); hold on;
colors=['b' 'g' 'r' 'c' 'm' 'y'];
for k=1:length(B),
    boundary = B{k};
    cidx = mod(k,length(colors))+1;
    plot(boundary(:,2), boundary(:,1),...
        colors(cidx),'LineWidth',2);
    
    %randomize text position for better visibility
    rndRow = ceil(length(boundary)/(mod(rand*k,7)+1));
    col = boundary(rndRow,2); row = boundary(rndRow,1);
    h = text(col+1, row-1, num2str(L(row,col)));
    set(h,'Color',colors(cidx),'FontSize',14,'FontWeight','bold');
end
end