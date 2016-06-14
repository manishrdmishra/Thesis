package image.preprocessing.LabelImage;

import ij.ImagePlus;
import ij.process.ByteProcessor;
import ij.process.ImageProcessor;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryIteratorException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Vector;

/**
 * 
 * @author manish This class convert a RGB/Gray segmented image to labeled image
 *         by assigning the label to each pixel according to segmented image.
 * 
 */
public class LabeledImageBuilder
{

    private ImageProcessor labeledImageProcessor;
    private ImageProcessor segmentedImageProcessor;
    private ImagePlus segmentedLabeledImage;
    private Label labels = null;

    public LabeledImageBuilder(Label labels)
    {
        this.labels = labels;

    }

    private void initializeAttributes()
    {
        labeledImageProcessor = null;
        segmentedImageProcessor = null;
        segmentedLabeledImage = null;
    }

    public ImagePlus build(String segmentedImageName, Path roiFolderPath)
    {
        System.out.println("LabelImageBuilder: " + segmentedImageName + " "
                + roiFolderPath);
        initializeAttributes();
        ImagePlus segmentedImage = new ImagePlus(segmentedImageName);
        segmentedImageProcessor = segmentedImage.getChannelProcessor();
        initializeImage(segmentedImage);
        assigneLabelToRois(roiFolderPath);
        String title = segmentedImageName + "-label";
        createLabeledImage(title);
        return segmentedLabeledImage;
    }

    public ImagePlus getLabeledImage()
    {
        return segmentedLabeledImage;
    }

    private void createLabeledImage(String title)
    {
        segmentedLabeledImage = new ImagePlus(title, labeledImageProcessor);
    }

    private void assigneLabelToRois(Path roiFolderPath)
    {

        File roiFileName = new File(roiFolderPath.toString());
        // check if the roipath is directory
        if (roiFileName.isDirectory())
        {

            DirectoryStream<Path> files = null;
            try

            {
                files = Files.newDirectoryStream(roiFolderPath);

            } catch (IOException | DirectoryIteratorException x)
            {
                // IOException can never be thrown by the iteration.
                // In this snippet, it can only be thrown by newDirectoryStream.
                System.err.println(x);
            }
            for (Path file : files)
            {

                System.out.println("roi file path : " + file.toString());
                assignLabelToRoi(file);

            }
        } 
        else
        {
            System.out.println("roi file path : " + roiFolderPath.toString());
            assignLabelToRoi(roiFolderPath);
        }

    }

    private void assignLabelToRoi(Path roiFilePath)
    {
        RoiPixels roiPixels = new RoiPixels(roiFilePath,

        segmentedImageProcessor);
        Vector<Pixel> pixels = roiPixels.getRoiPixels();

        int label = labels.getLabelForRoi(roiPixels.getRoi());
        for (Pixel pixel : pixels)
        {

            labeledImageProcessor.putPixel(pixel.getCoordinate().x,
                    pixel.getCoordinate().y, label);
        }
    }

    private void initializeImage(ImagePlus segmentedImage)

    {
        int width = segmentedImage.getWidth();
        int height = segmentedImage.getHeight();
        labeledImageProcessor = new ByteProcessor(width, height);
//        (new ImagePlus(segmentedImage.getTitle(), labeledImageProcessor))
//                .show();
        

    }

}
