%%%%%%%%%%%%%%%%%%%%%%%%%
%% This script plots the error bar graph for
%% Biological replicates mitochondria level.
%%%%%%%%%%%%%%%%%%%%%%%%%

rootPath = '/home/manish/git/Documents/fifth_sem/thesis/restult_folder/malis_loss_results';
folders = dir(rootPath);
num_dir = size(folders , 1);

count = 1;

for i = 3 : num_dir
    
%path = '/home/manish/git/Documents/fifth_sem/thesis/restult_folder/malis_loss_results/Leber_HR/replicates/replicate_scores';
path = fullfile(rootPath,folders(i).name,'replicates/replicate_scores');
files  = dir(path);
m = size(files , 1);

%y = zeros(m-2,2);

% create labels array

formatSpec = '%f';
for j = 3:m
%     file_name = fullfile(path,folders(j).name,'/scores/image_scores.txt');
    %file_name = fullfile(path,folders(j).name,'/projected_scores/hi_scores.txt');
   % file_name = fullfile(path,folders(j).name,'/scores/mitochondria_scores.txt');
    file_name = fullfile(path,files(j).name);
    %     fprintf(fileID, ' \n %20s \n\n',dname );
    disp(file_name);
    
    
    strcat(cellstr(folders(i).name),num2str(j-2));
    Labels(count) = strcat(cellstr(folders(i).name),num2str(j-2));;
    file_id = fopen(file_name,'r');
    cancer_prob = fscanf(file_id,formatSpec);
    
    
    y(count, 1) = mean(cancer_prob);
    if ( y(count,1) < 0.5 )
        color(count) = 'g';
    else
        color(count) = 'r';
    end
    y(count , 2) = std(cancer_prob);
    
    count = count + 1;
    
    
end
end
bar(1:count-1 , y(1:count-1,1) ,'b');
hold on; errorbar ( 1:count-1 , y(1:count-1,1) , y(1:count-1,2),'b*');
set(gca, 'XTick', 1:count-1, 'XTickLabel', Labels);
%xlabel('Biological replicates') % x-axis label
%ylabel('Mean HI score') % y-axis label
%ylabel('Mean image score') 
legend('mean','std')
%figure_name = fullfile(path,'hi_score');
%figure_name = fullfile(path,'cancer_score');
%print(figure_name,'-dpng')
%saveas(figureName,'.png')