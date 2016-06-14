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

/**
 * 1) fileName
 * This command takes input the name of the file which contains the name
 * of segmented images and their corresponding roi file name. The structure of
 * the text file is given below.
 * 
 * seg_image-1.tif seg_image_1.tif.roi seg_image_2.tif seg_image_2.tif.roi ....
 * 2) path_to_store_labelled_images
 * 3) File which contains class label mapping
 *   ex. 
 *   
 *   healhty 1
 *   tumor   2
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
        lines = readFile();
        Vector<String> tokens = null;
        tokens = splitLines(lines);
        imageRoiPairs = convertTokensInImageRoiPairs(tokens);

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
            //labeledImage.show();
            Path newPath = new File(directoryName + "/"
                    + (Paths.get(pair.getKey())).getFileName()).toPath();
            System.out.println("Saving labeled image at: " + newPath);
            FileSaver fileSaver = new FileSaver(labeledImage);
            fileSaver.saveAsTiff(newPath.toString());
        }

    }

    /**
     * Read the lines of files and stores them in a vector of string.
     * 
     * @return
     */
    private Vector<String> readFile()
    {
        Vector<String> lines = new Vector<String>();
        try
        {

            FileInputStream fstream = new FileInputStream(fileName);
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    fstream));

            String strLine;

            // Read File Line By Line
            while ((strLine = br.readLine()) != null)
            {
                // Print the content on the console
                System.out.println(strLine);
                lines.add(strLine);

            }

            // Close the input stream
            br.close();
        } catch (IOException exception)
        {
            exception.printStackTrace();
        }
        ;
        return lines;
    }

    /**
     * This function splits lines into tokens and stores them into a vector. We
     * assume that the tokens are space separated.
     * 
     * @return
     */
    private Vector<String> splitLines(Vector<String> lines)
    {
        Vector<String> tokens = new Vector<String>();
        for (String line : lines)
        {
            String[] splited = line.split("\\s+");
            for (String string : splited)
            {
                tokens.add(string);
            }
        }
        return tokens;
    }

    private HashMap<String, String> convertTokensInImageRoiPairs(
            Vector<String> tokens)
    {
        HashMap<String, String> imageRoiPairs = new HashMap<String, String>();
        if (tokens.size() % 2 != 0)
        {
            String errorMessage = "The number of tokens should be even";
            System.out.print(errorMessage);

            // throw new Exception(errorMessage);
        }
        for (int i = 0; i < tokens.size(); i = i + 2)
        {
            System.out.println("key: " + tokens.get(i) + "  value: "
                    + tokens.get(i + 1));
            imageRoiPairs.put(tokens.get(i), tokens.get(i + 1));

        }
        return imageRoiPairs;

    }
}
