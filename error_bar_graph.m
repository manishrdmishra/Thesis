%%%%%%%%%%%%%%%%%%%%%%%%%
%% This script plots the error bar graph
%%%%%%%%%%%%%%%%%%%%%%%%%

path = '/home/manish/git/Documents/fifth_sem/thesis/restult_folder/all_predicted_images_processed';
folders = dir(path);
m = size(folders , 1);

y = zeros(m-2,2);

formatSpec = '%f';
for j = 3:m
  %  file_name = fullfile(path,folders(j).name,'/projected_scores/image_scores.txt');
     file_name = fullfile(path,folders(j).name,'/projected_scores/hi_scores.txt');
    %     fprintf(fileID, ' \n %20s \n\n',dname );
    disp(file_name);
    
    file_id = fopen(file_name,'r');
    cancer_prob = fscanf(file_id,formatSpec);
    
    
    %% uncomment this to plot bar graph of image level mean and its std
    y(j- 2 , 1) = mean(cancer_prob);
    y(j-2 , 2) = std(cancer_prob);
    
    
    
    
end
bar(1:m-2 , y(1:m-2,1) , 'b');
hold on; errorbar ( 1:m-2 , y(1:m-2,1) , y(1:m-2,2),'b*');
Labels = {'Fao','H4IIE', 'HepG2', 'HepT1','Healthy','Mhic1','McA7777'};
set(gca, 'XTick', 1:7, 'XTickLabel', Labels);