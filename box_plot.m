
root_folder = '/home/manish/git/Documents/fifth_sem/thesis/restult_folder/all_predicted_images';

formatSpec = '%f';

fao_file = fullfile(root_folder,'/Fao_seg_png/projected_scores','fao_score.txt');
fao_file_id = fopen(fao_file,'r');
fao = fscanf(fao_file_id,formatSpec);

H4IIE_file = fullfile(root_folder,'/H4IIE_seg_png/projected_scores','H4IIE_score.txt');
H4IIE_file_id = fopen(H4IIE_file,'r');
H4IIE = fscanf(H4IIE_file_id,formatSpec);


HepG2_file = fullfile(root_folder,'/HepG2_seg_png/projected_scores','HepG2_score.txt');
HepG2_file_id = fopen(HepG2_file,'r');
HepG2 = fscanf(HepG2_file_id,formatSpec);

HepT1_file = fullfile(root_folder,'/HepT1_seg_png/projected_scores','HepT1_score.txt');
HepT1_file_id = fopen(HepT1_file,'r');
HepT1 = fscanf(HepT1_file_id,formatSpec);

mhic1_file = fullfile(root_folder,'/mhic1_seg_png/projected_scores','mhic1_score.txt');
mhic1_file_id = fopen(mhic1_file,'r');
mhic1 = fscanf(mhic1_file_id,formatSpec);

tumour_file = fullfile(root_folder,'/tumour_seg_png/projected_scores','tumour_score.txt');
tumour_file_id = fopen(tumour_file,'r');
tumour = fscanf(tumour_file_id,formatSpec);

leber_file = fullfile(root_folder,'/leber_seg_png/projected_scores','leber_score.txt');
leber_file_id = fopen(leber_file,'r');
leber = fscanf(leber_file_id,formatSpec);

fao = fao';
H4IIE = H4IIE';
HepG2 = HepG2';
HepT1 = HepT1';
mhic1 = mhic1';
tumour = tumour';
leber = leber';

scores = [fao H4IIE HepG2 HepT1 mhic1 tumour leber];

grp = [zeros(1,size(fao,2)), ones(1,size(H4IIE,2)), 2 * ones( 1, size(HepG2,2)), 3 * ones(1, size(HepT1,2)),4 * ones(1,size(mhic1,2)), 5 * ones(1,size(tumour,2)), 6 * ones(1,size(leber,2))];
boxplot(scores,grp,'Notch','on','Labels',{'fao','H4IIE', 'HepG2', 'HepT1', 'mhic1','tumour', 'leber'})
