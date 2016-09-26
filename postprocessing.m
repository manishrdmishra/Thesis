function[] =  extractRoi(imageFolderPath, roiFolder)

imageNames = dir(imageFolderPath);


for i = 3:size(imageNames , 1)
    imagePath = fullfile(imageFolderPath,imageNames(i).name);
    %imageName = '/home/manish/git/Documents/fifth_sem/thesis/restult_folder/all_predicted_images_processed/tumour_seg_png/results-1/149-12b_20000_1.png';
    I = imread(imagePath);
    processedImage = processImage(I);
    labelObjects(processedImage);
    displayLabledObjects(processedImage);
    
    % imshow(BW)
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
        figure ;
        imshow(BW);
        % Fill the holes to complete the
        % mitochondria
        BW2 = imfill(BW,'holes');
        for i = 1:1
            BW2 = bwmorph(BW2,'clean');
            %BW2 = bwmorph(BW2,'clean');
        end
        
        BW2 = bwmorph(BW2,'fill',5);
        figure;
        imshow(BW2);% Fill the holes to complete the
        % mitochondria
        BW2 = imfill(BW,'holes');
        
        BW2 = bwmorph(BW2,'clean',1);
        %BW2 = bwmorph(BW2,'clean');
        
        
        title('cleaned Image')
        processedImage = BW2;
        
    end

    function[] = labelObjects(binaryImage)
        
        
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
        [L , num ] = bwlabel(BW3);
        % to find the row and column for 2nd object
        % [r,c] = find(L == 2)
        % BW = roicolor(L,1,1); % it will generate roi corresponding
        %  to object 1
        figure , imcontour(BW3);
        disp num;
        
    end

    function[] = displayLabledObjects(binaryImage)         %
        % Overlay Region Boundaries on Image and Annotate with Region Numbers
        %O
      
        %BW = imread('blobs.png');
        [B,L,N,A] = bwboundaries(binaryImage);
        imshow(BW); hold on;
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