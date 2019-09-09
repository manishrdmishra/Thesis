# Master thesis 
In this study, we propose a deep-learning based model to quantitatively assess the status
of each intact mitochondrion with a continuous score, which measures
its closeness to the healthy/tumor classes based on its morphology. 

This allows us to describe the structural transition from healthy to cancer-
ous mitochondria. Methodologically, we train two USK networks, one to segment 
individual mitochondria from an electron micrograph, and the other to softly 
classify each image pixel as belonging to (i) healthy mito-chondrial, (ii) cancerous mitochondrial and (iii) non-mitochondrial (image background & impurities) tissue. 
Our combined model outperforms each network alone in both pixel classification and object segmentation.
