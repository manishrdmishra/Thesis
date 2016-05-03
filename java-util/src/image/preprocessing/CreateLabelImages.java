package image.preprocessing;

import ij.ImagePlus;

import java.nio.file.FileSystems;
import java.nio.file.Path;

public class CreateLabelImages
{

    public static void main(String[] args)
    {
        String segmentedImageName = "/home/manish/Desktop/Tumor_segmented/149-12b 20000 7.tif-seg.tif";
        Path roiFolderPath = FileSystems
                .getDefault()
                .getPath(
                        "/home/manish/Desktop/Tumor_segmented/149-12b 20000 7.tif-seg.tif-ROIset");
        String[] labels = { "Healthy", "Tumor", "Non_Mitochondria" };
        Label label = new Label(labels);
        LabeledImageBuilder labelImageBuilder = new LabeledImageBuilder();
        ImagePlus labeledImage = labelImageBuilder.build(segmentedImageName,
                roiFolderPath, label);
        labeledImage.show();
    }
}
