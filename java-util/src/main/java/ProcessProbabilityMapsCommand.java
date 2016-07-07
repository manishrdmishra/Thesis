package main.java;

import ij.ImagePlus;
import ij.io.FileSaver;
import ij.process.ByteProcessor;
import ij.process.FloatProcessor;
import ij.process.ImageProcessor;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Vector;

import main.java.exception.NumberOfArgumentsMismatchException;
import main.java.util.FileSystemUtil;

/**
 * This command takes 3 inputs <1 - probabilityMapPath, 2 - testImagePath 3 -
 * prefixPath>
 * 
 * @author manish
 * 
 */
public class ProcessProbabilityMapsCommand implements Command
{

    final String SEGMENTEDIMAGEDIR = "segmented_images";
    final String LABELIMAGEDIR = "label_images";
    final String MAXPROBOBALITYDIR = "max_probabilitymap_images";
    /**
     * This saves the path where all the probability maps are stored Here it is
     * assumed that the probability map folder contains n folder each belonging
     * to one class. probabilityMapPath<class-0, class-1, .. class-n> and in
     * each class-i directory there will be m probability maps images. m will be
     * same for all class-i directory.
     */
    private String probabilityMapPath = null;
    /**
     * This saves the path where all the test images are stored
     */
    private String testImagePath = null;

    private String prefixPath = null;

    Vector<Vector<ImageProcessor>> probabilityMaps = new Vector<Vector<ImageProcessor>>();
    Vector<ImageProcessor> maxProbablityMapProcessors = new Vector<ImageProcessor>();
    Vector<ImageProcessor> segmentedTestImageProcessors = new Vector<ImageProcessor>();
    Vector<ImageProcessor> labelImageProcessors = new Vector<ImageProcessor>();
    Vector<ImageProcessor> testImageProcessors = new Vector<ImageProcessor>();
    Vector<String> imageNames = new Vector<String>();
    int width;
    int height;

    @Override
    public void parseArguments(String[] args)
            throws NumberOfArgumentsMismatchException
    {
        probabilityMapPath = args[0];
        testImagePath = args[1];
        prefixPath = args[2];

    }

    @Override
    public void execute()
    {
        readImageFileNames();
        loadProbabilityMaps();
        loadTestImages();
        setWidthAndHeight();
        processProbabilityMaps();
        writeMaxProbabilityMapsToDisk();
        writeSegmentedImagesToDisk();
        writeLabelImagesToDisk();

    }

    private void readImageFileNames()
    {
        Vector<File> imageFiles;
        imageFiles = FileSystemUtil.getFiles(testImagePath.toString());

        for (File imageFile : imageFiles)
        {
            System.out.println("Test file : " + imageFile.getName());
            imageNames.add(imageFile.getName());

        }
    }

    private Vector<ImageProcessor> loadImagesFromDirectory(File dirName)
    {
        Vector<File> imageFiles;
        imageFiles = FileSystemUtil.getFiles(dirName.toString());
        Vector<ImageProcessor> imageProcessors = new Vector<ImageProcessor>();
        for (File imageFile : imageFiles)
        {

            // open the image file

            ImagePlus rawImage = new ImagePlus(imageFile.toString());

            ImageProcessor imageProcessor = rawImage.getProcessor();
            // set the parameters for creating and padding the patches
            imageProcessors.add(imageProcessor);
        }
        return imageProcessors;
    }

    private void loadTestImages()
    {
        testImageProcessors = loadImagesFromDirectory(Paths.get(testImagePath)
                .toFile());
    }

    private void loadProbabilityMaps()
    {
        Vector<File> directories;
        directories = FileSystemUtil.getDirectories(probabilityMapPath);

        for (File dir : directories)
        {

            System.out.println("Directory name : " + dir);
            Vector<ImageProcessor> oneClassProbabilityMaps = loadImagesFromDirectory(dir);
            probabilityMaps.add(oneClassProbabilityMaps);
        }

    }

    private void setWidthAndHeight()
    {
        /* set the width and height value by the first image stored */
        width = 0;
        height = 0;
        if (probabilityMaps.size() >= 0 && probabilityMaps.get(0).size() >= 0)
        {
            width = (probabilityMaps.get(0).get(0)).getWidth();
            height = (probabilityMaps.get(0).get(0)).getHeight();

        } else
        {
            System.out
                    .println("There are no probability maps present in the given directory");
        }
        System.out.println("The size of probability map - width : " + width
                + " height : " + height);
    }

    private void processProbabilityMaps()
    {

        /* find the number of classes and images present in each class */
        int numOfClasses = 0;
        int numOfImagesPerClass = 0;
        if (probabilityMaps.size() >= 0 && probabilityMaps.get(0).size() >= 0)
        {
            numOfClasses = probabilityMaps.size();
            numOfImagesPerClass = probabilityMaps.get(0).size();
        } else
        {
            System.out
                    .println("There are no probability maps present in the given directory");

        }
        for (int i = 0; i < numOfImagesPerClass; i++)
        {
            ImageProcessor maxProbabilityImageProcessor = new FloatProcessor(
                    width, height);
            ImageProcessor labelImageProcessor = new ByteProcessor(width,
                    height);
            ImageProcessor segmentedImageProcessor = new ByteProcessor(
                    testImageProcessors.get(i), false);
            // loop over the pixel values for all the images
            for (int y = 0; y < height; y++)
            {
                for (int x = 0; x < width; x++)
                {
                    double maxIntensity = 0;
                    int index = 0;

                    /* loop to find the max probability */
                    for (int j = 0; j < numOfClasses; j++)
                    {

                        double pixelIntensity = probabilityMaps.get(j).get(i)
                                .getPixel(x, y);

                        if (maxIntensity < pixelIntensity)
                        {
                            maxIntensity = pixelIntensity;
                            /*
                             * save the index of class which have max
                             * probability
                             */
                            index = j;
                        }
                    }
                    /* put the max probability */
                    maxProbabilityImageProcessor.putPixelValue(x, y,
                            maxIntensity);

                    /* put the label value */
                    int intensity = (index * 100) % 255; /*
                                                          * to help in
                                                          * visualization
                                                          */
                    labelImageProcessor.putPixel(x, y, intensity);

                    /* set the background in segmented image processor */
                    if (index == 0 || index == 3)
                    {
                        segmentedImageProcessor.putPixel(x, y, 0);
                    }

                }
            }
            segmentedTestImageProcessors.add(segmentedImageProcessor);
            maxProbablityMapProcessors.add(maxProbabilityImageProcessor);
            labelImageProcessors.add(labelImageProcessor);

        }

    }

    public void writeMaxProbabilityMapsToDisk()
    {
        String pathString = prefixPath + "/" + MAXPROBOBALITYDIR;
        System.out.println("creating dir.. : " + pathString);
        Path maxProbabilityMapPath = Paths.get(pathString);
        FileSystemUtil.createDirectory(maxProbabilityMapPath);
        FileSystemUtil.saveImages(maxProbabilityMapPath, imageNames,
                maxProbablityMapProcessors);

    }

    public void writeSegmentedImagesToDisk()
    {
        String pathString = prefixPath + "/" + SEGMENTEDIMAGEDIR;
        System.out.println("creating dir.. : " + pathString);
        Path maxProbabilityMapPath = Paths.get(pathString);
        FileSystemUtil.createDirectory(maxProbabilityMapPath);
        FileSystemUtil.saveImages(maxProbabilityMapPath, imageNames,
                segmentedTestImageProcessors);

    }

    public void writeLabelImagesToDisk()
    {
        String pathString = prefixPath + "/" + LABELIMAGEDIR;
        System.out.println("creating dir.. : " + pathString);
        Path maxProbabilityMapPath = Paths.get(pathString);
        FileSystemUtil.createDirectory(maxProbabilityMapPath);
        FileSystemUtil.saveImages(maxProbabilityMapPath, imageNames,
                labelImageProcessors);
    }

}
