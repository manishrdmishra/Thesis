package image.preprocessing.LabelImage;

import ij.ImagePlus;
import ij.gui.Roi;
import ij.process.ByteProcessor;
import ij.process.ImageProcessor;

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

    public LabeledImageBuilder()
    {

    }

    private void initializeAttributes()
    {
        labeledImageProcessor = null;
        segmentedImageProcessor = null;
        segmentedLabeledImage = null;
    }

    public ImagePlus build(String segmentedImageName, Path roiFolderPath,
            Label labels)
    {
        initializeAttributes();
        ImagePlus segmentedImage = new ImagePlus(segmentedImageName);
        segmentedImageProcessor = segmentedImage.getChannelProcessor();
        initializeImage(segmentedImage);
        assigneLabelToPixels(roiFolderPath, labels);
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

    private void assigneLabelToPixels(Path roiFoldePath, Label labels)
    {

        DirectoryStream<Path> files = null;
        try

        {
            files = Files.newDirectoryStream(roiFoldePath);

        } catch (IOException | DirectoryIteratorException x)
        {
            // IOException can never be thrown by the iteration.
            // In this snippet, it can only be thrown by newDirectoryStream.
            System.err.println(x);
        }
        for (Path file : files)
        {

            System.out.println("roi file path : " + file.toString());

            RoiPixels roiPixels = new RoiPixels(file,

            segmentedImageProcessor);
            Vector<Pixel> pixels = roiPixels.getRoiPixels();

            int label = labels.getLabelForRoi(roiPixels.getRoi());
            for (Pixel pixel : pixels)
            {
                // For visualizing change the label here to 100
                labeledImageProcessor.putPixel(pixel.getCoordinate().x,
                        pixel.getCoordinate().y, label);
            }
        }

    }

    private void initializeImage(ImagePlus segmentedImage)

    {
        int width = segmentedImage.getWidth();
        int height = segmentedImage.getHeight();
        labeledImageProcessor = new ByteProcessor(width, height);
        (new ImagePlus(segmentedImage.getTitle(), labeledImageProcessor))
                .show();

    }

}
