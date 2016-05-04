package image.preprocessing.LabelImage;

import ij.ImagePlus;

import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.HashMap;

public class CreateLabelImages
{

    public static void main(String[] args)
    {
        String segmentedImageName = "/home/manish/Desktop/Tumor_segmented/149-12b 20000 7.tif-seg.tif";
        Path roiFolderPath = FileSystems
                .getDefault()
                .getPath(
                        "/home/manish/Desktop/Tumor_segmented/149-12b 20000 7.tif-seg.tif-ROIset");
        HashMap<String, Integer> labels = new HashMap<String, Integer>();
        labels.put("Healthy", 50);
        labels.put("Tumor", 100);
        labels.put("Non_Mitochondria", 200);
        Label label = new Label(labels);
        LabeledImageBuilder labelImageBuilder = new LabeledImageBuilder();
        ImagePlus labeledImage = labelImageBuilder.build(segmentedImageName,
                roiFolderPath, label);
        labeledImage.show();
    }
}
