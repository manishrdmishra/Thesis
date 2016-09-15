imag_path = '/home/manish/Desktop/hepg_t/';
prob_path = '/home/manish/git/Documents/fifth_sem/thesis/other_tumor_result/';
imag_name = 'HepG2_230-12_20000_1.tif-seg.tif';
imag = imread([imag_path imag_name]);
prob_back = imread([prob_path '0/' imag_name]);
prob_healthy = imread([prob_path '1/' imag_name]);
prob_tumour = imread([prob_path '2/' imag_name]);

bw_back = im2bw(1-prob_back, 0.5);
L = bwlabel(bw_back);
figure; imshow(bw_back);colorbar;


figure; subplot(221); imshow(imag);colorbar;title('original image');
subplot(222); imagesc(prob_back);colorbar; title('background prob');colormap('gray');
subplot(223); imagesc(prob_healthy);colorbar; title('healthy prob');
subplot(224); imagesc(prob_tumour);colorbar; title('tumour prob');
