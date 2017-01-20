function[] = ColorSegmentsOfImage(roiFile, rawImage,healthyProbabilityFile ,tumorProbabilityFile,pathToSave)
%image = imread('/home/manish/git/Documents/fifth_sem/thesis/restult_folder/matlab_all_images/Fao/Fao_826-14_200000_8.png');
%imageGray = image(:,:,1);
%imshow(imageGray)
% I = imread('rice.png');
% I2 = imtophat(I, ones(15, 15));
% bw = im2bw(I2, graythresh(I2));
% bw2 = bwareaopen(bw, 5);
% bw3 = imclearborder(bw2);
% imshow(bw3)
% L = bwlabel(bw3);
% s = regionprops(L, 'Extrema');
%imshow(I)
%roiFile = '/home/manish/git/Documents/fifth_sem/thesis/restult_folder/malis_loss_results/Fao/roiFolder/Fao_826-14_200000_8-roi.mat';
image = imread(rawImage);
healthyProbabilty = imread(healthyProbabilityFile);
tumorProbability = imread(tumorProbabilityFile);
load(roiFile,'roi');
numOfObjects = size(roi,2);
index = 1;
for i = 1 : numOfObjects
    [score, centroid ] = calculateMitochondriaScore(roi(i),healthyProbabilty, tumorProbability);
    if score < 0.5 
        index = 1;
    else
        index = 2;
    end
    
    x = roi(i).x;
    y = roi(i).y;
    for j = 1:size(x , 1)
        image(y(j),x(j),index) = 0;
    end
end
%imshow(image);
%patch(x, y, 'r')

%hold on
% for k = 1:numel(s)
%     x = s(k).Extrema(:,1);
%     y = s(k).Extrema(:,2);
%    patch(x, y, 'g', 'FaceAlpha', 0.1)
% end
%hold off


%figure
%I2 = image(F.cdata);
[pathstr,name,ext] = fileparts(rawImage);
name = strcat(name,'.tif');
finalFileName = fullfile(pathToSave, name);
imwrite( image,finalFileName,'tif');

end




