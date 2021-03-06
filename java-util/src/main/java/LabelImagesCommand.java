package main.java;

import ij.ImagePlus;
import ij.io.FileSaver;
import image.preprocessing.LabelImage.Label;
import image.preprocessing.LabelImage.LabeledImageBuilder;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Vector;

import main.java.util.FileSystemUtil;
import main.java.util.StringUtil;

/**
 * 1) fileName This command takes input the name of the file which contains the
 * name of segmented images and their corresponding roi file name. The structure
 * of the text file is given below.
 * 
 * seg_image-1.tif seg_image_1.tif.roi seg_image_2.tif seg_image_2.tif.roi ....
 * 2) path_to_store_labelled_images 3) File which contains class label mapping
 * ex.
 * 
 * healhty 1 tumor 2
 */

public class LabelImagesCommand implements Command
{
    private String fileName = null;
    // directory where labelled images will be saved
    // the path of the directory should be absolute.
    private String directoryName = null;

    private HashMap<String, String> imageRoiPairs = null;

    public LabelImagesCommand()
    {

    }

    @Override
    public void parseArguments(String[] args)
    {

        fileName = args[0];
        directoryName = args[1];
        Vector<String> lines = null;
        lines = FileSystemUtil.readFile(fileName);
        Vector<String> tokens = null;
        tokens = StringUtil.splitLines(lines);
        imageRoiPairs = StringUtil.convertTokensInPairs(tokens);

    }

    @Override
    public void execute()
    {
        // TODO Auto-generated method stub
        // String segmentedImageName =
        // "/home/manish/Desktop/Tumor_segmented/149-12b 20000 7.tif-seg.tif";
        // Path roiFolderPath = FileSystems
        // .getDefault()
        // .getPath(
        // "/home/manish/Desktop/Tumor_segmented/149-12b 20000 7.tif-seg.tif-ROIset");
        HashMap<String, Integer> labels = new HashMap<String, Integer>();
        // For visualizing change the label here to 100
        labels.put("Healthy", 1);
        labels.put("Tumor", 2);
        labels.put("Non_Mitochondria", 3);
        Label label = new Label(labels);

        // create instance of label image builder
        LabeledImageBuilder labelImageBuilder = new LabeledImageBuilder(label);

        Iterator<Entry<String, String>> it = imageRoiPairs.entrySet()
                .iterator();
        while (it.hasNext())
        {
            Map.Entry<String, String> pair = (Entry<String, String>) it.next();
            String imageFileName = pair.getKey();
            Path roiPathName = Paths.get(pair.getValue());
            ImagePlus labeledImage = labelImageBuilder.build(imageFileName,
                    roiPathName);

            // Uncomment it to show the labelled image
            // labeledImage.show();
            Path newPath = new File(directoryName + "/"
                    + (Paths.get(pair.getKey())).getFileName()).toPath();
            System.out.println("Saving labeled image at: " + newPath);
            FileSaver fileSaver = new FileSaver(labeledImage);
            fileSaver.saveAsTiff(newPath.toString());
        }

    }

}