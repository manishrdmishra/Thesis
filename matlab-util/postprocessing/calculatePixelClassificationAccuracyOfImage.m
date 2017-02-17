function [confusion_matrix] = calculatePixelClassificationAccuracyOfImage(testFile , backgroundProbabilityFile, healthyProbabilityFile ,tumorProbabilityFile, fileID)
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% This function calcualte the pixel accuracy of given image.
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

testFileMap = imread(testFile);
backgroundProbability = imread(backgroundProbabilityFile);
healthyProbability = imread(healthyProbabilityFile);
tumorProbability = imread(tumorProbabilityFile);

total_count = zeros(1,3);
class_accuracy = zeros(1,3);

confusion_matrix = zeros(3,3);


for i = 1:size(testFileMap,1)
    for j = 1:size(testFileMap,2)
        probabilities = [backgroundProbability(i,j) healthyProbability(i,j) tumorProbability(i,j) ];
        predicted_class = find(probabilities == max(probabilities(:)));
        groundtruth_class = testFileMap(i,j);
        
        % label for impurity is 3 and we want to treat them 
        % as background. 
        if (groundtruth_class == 3)
            groundtruth_class = 0;
        end
        
        % adding one to make it 1 index 
        groundtruth_class = groundtruth_class + 1;
        
        total_count(groundtruth_class) = total_count(groundtruth_class) + 1;
%         if( predicted_class == groundtruth_class)
%             class_accuracy(predicted_class) =   class_accuracy(predicted_class) + 1;
%         end
confusion_matrix(groundtruth_class,predicted_class) = confusion_matrix(groundtruth_class,predicted_class) + 1;
        

    end
end
confusion_matrix

end