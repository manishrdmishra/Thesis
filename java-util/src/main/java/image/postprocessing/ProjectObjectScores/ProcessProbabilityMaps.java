package main.java.image.postprocessing.ProjectObjectScores;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryIteratorException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import ij.ImagePlus;
import ij.gui.Overlay;
import ij.gui.Roi;
import ij.gui.TextRoi;
import ij.io.RoiDecoder;
import ij.process.ImageProcessor;
import image.preprocessing.LabelImage.Label;
import image.preprocessing.LabelImage.RoiPixels;
import main.java.util.FileSystemUtil;

public class ProcessProbabilityMaps
{

    final String SEGMENTEDIMAGEDIR = "segmented_images";
    final String LABELIMAGEDIR = "label_images";
    final String MAXPROBOBALITYDIR = "max_probabilitymap_images";
    final int HEALTHY_CLASS_INDEX = 1;
    final int TUMOUR_CLASS_INDEX = 2;
    /**
     * This saves the path where all the probability maps images corresponding
     * to test images, are stored Here. It is assumed that the probability map
     * folder contains n folder each belonging to one class.
     * probabilityMapsPath<0,1, .. n> and in each class-i directory there will
     * be m probability maps images. m will be same for all class-i directory.
     */
    private String probabilityMapsPath = null;
    /**
     * This saves the path where all the test images are stored
     */
    private String testImagePath = null;

    private String prefixPath = null;

    // private Vector<String> roiPaths = null;
    private String roiPath = null;
    private String healthypath = null;
    private String tumourPath = null;
    /**
     * Data structures to store the post processed outputs.
     */
    Vector<HashMap<String, File>> probabilityMaps;
    Vector<ImageProcessor> maxProbablityMapProcessors;
    // Vector<ImageProcessor> segmentedTestImageProcessors;
    // Vector<ImageProcessor> labelImageProcessors;
    // Vector<File> testImageProcessors;
    Vector<String> imageNames;
    Vector<ImageStats> imageStats;
    Vector<String> roiDirectories;

    /* Width and Height of one probability maps image */
    private int width;
    private int height;

    // private Vector<Double> imageScore;
    // private Vector<Double> heterogeneityIndex;
    // private Vector<Vector<Double>> roiScores;
    private double imageScore;
    private double heterogeneityIndex;
    private Vector<Double> roiScores;
    private Vector<Point> roiCenters;

    HashMap<String, Integer> labels;
    Label label;

    private void createLabels()
    {
        labels = new HashMap<String, Integer>();
        // For visualizing change the label here to 100
        labels.put("Healthy", 1);
        labels.put("Tumor", 2);
        labels.put("Non_Mitochondria", 3);
        label = new Label(labels);
    }

    public void process()
    {
        if (probabilityMapsPath == null && roiPath == null)
        {
            System.out.println("Data structures are not set");
        } else
        {
            createLabels();
            // initializeDataStructures();
            // loadTestImages();
            // loadProbabilityMaps();
            // show images for visualization
            // (new ImagePlus("healty", probabilityMaps.get(1).get(4))).show();
            // (new ImagePlus("tumour", probabilityMaps.get(2).get(4))).show();
            // setWidthAndHeight();
            // printPixels();

            roiScores = calculateRoisScore(Paths.get(roiPath),
                    Paths.get(healthypath), Paths.get(tumourPath));
            roiCenters = getRoiCenters(Paths.get(roiPath));
            imageScore = calculateImageScore(roiScores);
            heterogeneityIndex = calculateHeterogeneityIndex(roiScores,
                    imageScore);
            // calculateAllimagesScore();
            // calculateAllImagesHeterogeneityIndex();
            putTextOnImage();

        }

    }

    public void putTextOnImage()
    {
        ImagePlus test = new ImagePlus();
        // Overlay overlay = new Overlay();
        ImageProcessor im = test.getProcessor();
        BufferedImage image = null;
        try
        {
            File f = Paths.get(testImagePath).toFile();
            // File f = Paths
            // .get("/home/manish/git/Documents/fifth_sem/thesis/randomly_choosen_images/HepG_T/HepG2_230-12_20000_1.tif")
            // .toFile();
            image = ImageIO.read(f);
        } catch (Exception e)
        {
            System.out.println("exception done!!");
        }
        Graphics g = image.getGraphics();
        g.setFont(g.getFont().deriveFont(30f));
        for (int i = 0; i < roiScores.size(); i++)
        {
            System.out.println("score : " + roiCenters.get(i).getX() + " "
                    + roiCenters.get(i).getY() + " " + roiScores.get(i));
            TextRoi tr = new TextRoi(roiCenters.get(i).getX(),
                    roiCenters.get(i).getY(),
                    Double.toString(roiScores.get(i)));
            tr.setStrokeColor(Color.white);
            Overlay overlay = new Overlay();
            overlay.add(tr);
            test.setOverlay(overlay);
            // tr.setAntialiased(true);
            // im.drawString(Double.toString(roiScores.get(i)),
            // (int)roiCenters.get(i).getX(), (int)roiCenters.get(i).getY());
            // test.setOverlay(tr, Color.CYAN, 2, Color.BLACK);
            // im.setLineWidth(1);
            // im.setColor(Color.BLACK);
            // im.setValue(2);
            // im.setRoi(tr);
            //
            // im.fill();
            // im.draw(tr);
            // overlay.add(tr);
            // tr.drawPixels(im);
            // test.setRoi(tr);
            // test.setRoi(new TextRoi(roiCenters.get(i).getX(),
            // roiCenters.get(i).getY(),
            // Double.toString(roiScores.get(i))));

            DecimalFormat myFormatter = new DecimalFormat("0.00");
            String output = myFormatter.format(roiScores.get(i));
            if (roiScores.get(i) <= 0.5)
            {
                g.setColor(Color.GREEN);

            } else
            {
                g.setColor(Color.RED);
            }
            g.drawString(output, (int) roiCenters.get(i).getX(),
                    (int) roiCenters.get(i).getY());

        }
        // ImageProcessor im = test.getProcessor();
        // overlay.drawLabels(true);
        // im.setOverlay(overlay);
        // overlay.drawLabels(true);
        // overlay.drawBackgrounds(true);
        // overlay.setFillColor(Color.BLACK);
        // overlay.setFillColor(Color.CYAN);
        // im.drawOverlay(overlay);
        // /// test.show();
        //
        // ImagePlus imp = (new ImagePlus("test", im);
        // Roi[] ovlArray = test.getOverlay().toArray();
        // for (Roi roi : ovlArray)
        // {
        // test.setRoi(roi);
        // IJ.run(test, "Draw", "slice");
        // }
        // test.show();
        // test.flatten();
        g.dispose();

        // Use a label to display the image
        // JFrame frame = new JFrame();
        // ImageIcon imageIcon = new ImageIcon(image);
        // JLabel jLabel = new JLabel();
        // jLabel.setIcon(imageIcon);
        // JPanel mainPanel = new JPanel(new BorderLayout());
        // mainPanel.add(jLabel);
        // // add more components here
        // frame.add(mainPanel);
        // frame.setVisible(true);
        System.out.println("done!!");
        try
        {
            String name = Paths.get(testImagePath).getFileName().toString();
            ImageIO.write(image, "png", new File(name));
        } catch (IOException e)
        {
            System.out.println("IOException occured" + e.getMessage());
        }

    }

    Vector<Point> getRoiCenters(Path roiFolderPath)
    {
        Vector<Point> centers = new Vector<Point>();
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

                // System.out.println("roi file path : " + file.toString());
                RoiDecoder roiDecoder = new RoiDecoder(file.toString());
                Roi roi = null;
                try
                {
                    roi = roiDecoder.getRoi();

                } catch (IOException e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                // System.out.println("label for the roi : "
                // + label.getLabelForRoi(roi));
                if (label.getLabelForRoi(roi) != 3)
                {
                    centers.add(getCenter(getRoi(file)));
                }

            }
        } else
        {

            centers.add(getCenter(getRoi(roiFolderPath)));
        }

        return centers;

    }
    // private void printPixels()
    // {
    // System.out.println("pixel values : ");
    // for (int i = 0; i < 100; i++)
    // {
    // for (int j = 0; j < 100; j++)
    // {
    // System.out.print(" "
    // + +probabilityMaps.get(3).get(0).getf(i, j));
    // }
    // }
    // }

    // void initializeDataStructures()
    // {
    //
    // probabilityMaps = new Vector<HashMap<String, File>>();
    // // maxProbablityMapProcessors = new Vector<ImageProcessor>();
    // // segmentedTestImageProcessors = new Vector<ImageProcessor>();
    // // labelImageProcessors = new Vector<ImageProcessor>();
    // // testImageProcessors = new Vector<ImageProcessor>();
    // imageNames = new Vector<String>();
    // imageStats = new Vector<ImageStats>();
    // // imageScore = new Vector<Double>();
    // // heterogeneityIndex = new Vector<Double>();
    // roiScores = new Vector<Double>();
    // roiDirectories = new Vector<String>();
    //
    // }

    public void setProbabilityMapsPath(String path)
    {
        probabilityMapsPath = path;
    }

    public void settestImagePath(String path)
    {
        testImagePath = path;
    }

    public void setPrefixPath(String path)
    {
        prefixPath = path;
    }

    public void setRoiPath(String path)
    {
        roiPath = path;
    }

    public void setHealthyPath(String path)
    {
        healthypath = path;
    }

    public void setTumourPath(String path)
    {
        tumourPath = path;
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

    private Vector<File> readFilesPathFromDirectory(File dirName)
    {
        Vector<File> imageFiles;
        imageFiles = FileSystemUtil.getFiles(dirName.toString());
        Vector<File> fileNames = new Vector<File>();
        for (File imageFile : imageFiles)
        {
            System.out.println("image file name : " + imageFile);

            // open the image file

            // ImagePlus rawImage = new ImagePlus(imageFile.toString());

            // ImageProcessor imageProcessor = rawImage.getProcessor();
            // set the parameters for creating and padding the patches
            fileNames.add(imageFile);
        }
        return fileNames;
    }

    private void loadTestImageNames()
    {
        Vector<File> fileNames = readFilesPathFromDirectory(
                Paths.get(testImagePath).toFile());
        for (File file : fileNames)
        {
            imageNames.add(file.getName());

        }

    }

    // private void loadRoiDirectories()
    // {
    // roiDirectories =
    // readFilesPathFromDirectory(Paths.get(roiPaths).toFile());
    //
    // }

    // private void loadProbabilityMaps()
    // {
    // Vector<File> directories;
    // directories = FileSystemUtil.getDirectories(probabilityMapsPath);
    //
    // for (File dir : directories)
    // {
    // /*
    // * here it is assumed that the name of the directories are digits
    // * like 0 , 1, 2 etc, and they represent classes
    // */
    // int classIndex = Integer.parseInt(dir.getName());
    // System.out.println("Directory name : " + classIndex);
    // Vector<File> oneClassProbabilityMaps = readFilesPathFromDirectory(
    // dir);
    // HashMap<String, File> imageNamesAndPathMaps = new HashMap<String,
    // File>();
    // for (File file : oneClassProbabilityMaps)
    // {
    // imageNamesAndPathMaps.put(file.getName(), file);
    // }
    // probabilityMaps.add(classIndex, imageNamesAndPathMaps);
    // }
    //
    // }

    // private void setWidthAndHeight()
    // {
    // /* set the width and height value by the first image stored */
    // width = 0;
    // height = 0;
    // if (probabilityMaps.size() >= 0 && probabilityMaps.get(0).size() >= 0)
    // {
    // width = (probabilityMaps.get(0).get(0)).getWidth();
    // height = (probabilityMaps.get(0).get(0)).getHeight();
    //
    // } else
    // {
    // System.out
    // .println("There are no probability maps present in the given directory");
    // }
    // System.out.println("The size of probability map - width : " + width
    // + " height : " + height);
    // }

    // private void processProbabilityMaps()
    // {
    //
    // /* find the number of classes and images present in each class */
    // int numOfClasses = 0;
    // int numOfImagesPerClass = 0;
    // if (probabilityMaps.size() >= 0 && probabilityMaps.get(0).size() >= 0)
    // {
    // numOfClasses = probabilityMaps.size();
    // numOfImagesPerClass = probabilityMaps.get(0).size();
    // } else
    // {
    // System.out
    // .println("There are no probability maps present in the given directory");
    //
    // }
    // for (int i = 0; i < imageNames.size(); i++)
    // {
    // ImageProcessor maxProbabilityImageProcessor = new FloatProcessor(
    // width, height);
    // ImageProcessor labelImageProcessor = new ByteProcessor(width,
    // height);
    // // ImageProcessor segmentedImageProcessor = new ByteProcessor(
    // // testImageProcessors.get(i), false);
    // // loop over the pixel values for all the images
    // Vector<FloatProcessor> probabilities = new Vector<FloatProcessor>();
    // for (int j = 0 ; j< probabilityMaps.size(); j++)
    // {
    // File file = probabilityMaps.get(j).get(imageNames.get(i));
    // probabilities.add(file);
    // }
    // for (int y = 0; y < height; y++)
    // {
    // for (int x = 0; x < width; x++)
    // {
    // double maxIntensity = 0;
    // int index = 0;
    //
    // /* loop to find the max probability */
    // for (int j = 0; j < numOfClasses; j++)
    // {
    //
    // double pixelIntensity = probabilityMaps.get(j)
    //
    //
    // if (maxIntensity < pixelIntensity)
    // {
    // maxIntensity = pixelIntensity;
    // /*
    // * save the index of class which have max
    // * probability
    // */
    // index = j;
    // }
    // }
    // /* put the max probability */
    // maxProbabilityImageProcessor.putPixelValue(x, y,
    // maxIntensity);
    //
    // /* put the label value */
    // int intensity = (index * 100) % 255; /*
    // * to help in
    // * visualization
    // */
    // labelImageProcessor.putPixel(x, y, intensity);
    //
    // /* set the background in segmented image processor */
    // // if (index == 0 || index == 3)
    // // {
    // // segmentedImageProcessor.putPixel(x, y, 0);
    // // }
    //
    // }
    // }
    // // segmentedTestImageProcessors.add(segmentedImageProcessor);
    // maxProbablityMapProcessors.add(maxProbabilityImageProcessor);
    // // labelImageProcessors.add(labelImageProcessor);
    //
    // }
    //
    // }

    // private void calculateAllImagesRoiScores()
    // {
    // for (int i = 0; i < imageNames.size(); i++)
    // {
    // File heathlyClassFile = probabilityMaps.get(HEALTHY_CLASS_INDEX)
    // .get(imageNames.get(i));
    // File tumourClassFile = probabilityMaps.get(TUMOUR_CLASS_INDEX).get(
    // imageNames.get(i));
    // calculateRoisScore(Paths.get(roiPaths.get(i)), heathlyClassFile,
    // tumourClassFile);
    // }
    // }

    /**
     * Calculates the scores of all Rois in an image.
     * 
     * @param roiFolderPath
     * @return
     */
    private Vector<Double> calculateRoisScore(Path roiFolderPath,
            Path healthyClass, Path tumourClass)
    {

        Vector<Double> roiScore = new Vector<Double>();
        File roiFileName = new File(roiFolderPath.toString());
        ImageProcessor healthyImageProcessor = new ImagePlus(
                healthyClass.toString()).getChannelProcessor();
        ImageProcessor cancerImageProcessor = new ImagePlus(
                tumourClass.toString()).getChannelProcessor();
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

                // System.out.println("roi file path : " + file.toString());
                RoiDecoder roiDecoder = new RoiDecoder(file.toString());
                Roi roi = null;
                try
                {
                    roi = roiDecoder.getRoi();

                } catch (IOException e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                // System.out.println("label for the roi : "
                // + label.getLabelForRoi(roi));
                if (label.getLabelForRoi(roi) != 3)
                {
                    roiScore.add(calculateScoreOfRoi(file,
                            healthyImageProcessor, cancerImageProcessor));
                }

            }
        } else
        {
            // System.out.println("roi file path : " +
            // roiFolderPath.toString());
            roiScore.add(calculateScoreOfRoi(roiFolderPath,
                    healthyImageProcessor, cancerImageProcessor));
        }
        // roiScores.add(roiScore);
        return roiScore;
    }

    private Point getCenter(Roi roi)
    {

        Rectangle rect = roi.getBounds();
        System.out.println(
                "x : " + rect.getCenterX() + " y: " + rect.getCenterY());
        Point point = new Point();
        point.setLocation(rect.getCenterX(), rect.getCenterY());
        return point;

    }

    private Roi getRoi(Path roiFilePath)
    {
        RoiDecoder roiDecoder = new RoiDecoder(roiFilePath.toString());
        Roi roi = null;
        try
        {
            roi = roiDecoder.getRoi();

        } catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return roi;
    }

    // private void calculateAllimagesScore()
    // {
    // for (int i = 0; i < roiScores.size(); i++)
    // {
    // Vector<Double> roiScore = roiScores.get(i);
    // double score = calculateImageScore(roiScore);
    // imageScore.add(score);
    // }
    // }

    /* before calling this call calculateAllimagesScore */
    // private void calculateAllImagesHeterogeneityIndex()
    // {
    // for (int i = 0; i < roiScores.size(); i++)
    // {
    // Vector<Double> roiScore = roiScores.get(i);
    // double score = imageScore.get(i);
    // double hi = calculateHeterogeneityIndex(roiScore, score);
    // heterogeneityIndex.add(hi);
    // }
    // }

    private double calculateImageScore(Vector<Double> roiScore)
    {
        double sum = 0;
        for (Double score : roiScore)
        {
            sum = sum + score;

        }

        return sum / roiScore.size();

    }

    private double calculateHeterogeneityIndex(Vector<Double> roiScore,
            double imageScore)
    {
        double sum = 0;
        for (Double score : roiScore)
        {
            sum = sum + (imageScore - score) * (imageScore - score);

        }
        return Math.sqrt(sum / roiScore.size());
    }

    public double getImageScore()
    {
        return imageScore;
    }

    public double getHeterogeneityScore()
    {
        return heterogeneityIndex;
    }

    public double calculateScoreOfRoi(Path roiFilePath,
            ImageProcessor healthyImageProcessor,
            ImageProcessor cancerImageProcessor)
    {

        RoiPixels roiPixelsHealthy = new RoiPixels(roiFilePath,

                healthyImageProcessor, 0);
        RoiPixels roiPixelsTumour = new RoiPixels(roiFilePath,

                cancerImageProcessor, 0);
        Vector<Float> pixelsHealthy = roiPixelsHealthy.getFloatIntensities();
        Vector<Float> pixelsTumour = roiPixelsTumour.getFloatIntensities();
        double cumulativeScoreOfTumour = 0;
        for (int i = 0; i < pixelsHealthy.size(); i++)
        {
            double probabilityOfTumour = pixelsTumour.get(i);

            double probabilityOfHealthy = pixelsHealthy.get(i);
            // if (i % 100 == 0)
            // {
            // System.out.println("t score : " + probabilityOfTumour
            // + " h score : " + probabilityOfHealthy);
            // }
            double scoreDiff = probabilityOfTumour - probabilityOfHealthy;
            cumulativeScoreOfTumour = cumulativeScoreOfTumour
                    + (Math.exp(scoreDiff)
                            / (Math.exp(scoreDiff) + Math.exp(-scoreDiff)));

        }
        double avgScoreOfTumour = cumulativeScoreOfTumour
                / pixelsHealthy.size();
        // System.out.println("Tumour score of roi : " + avgScoreOfTumour);

        // TextRoi textRoi = new TextRoi(centroid[0], centroid[1],
        // Double.toString(avgScoreOfTumour));
        // cancerImageProcessor.drawString( Double.toString(avgScoreOfTumour),
        // (int)centroid[0] , (int)centroid[1]);
        return avgScoreOfTumour;
    }

    // public void writeMaxProbabilityMapsToDisk()
    // {
    // String pathString = prefixPath + "/" + MAXPROBOBALITYDIR;
    // System.out.println("creating dir.. : " + pathString);
    // Path maxprobabilityMapsPath = Paths.get(pathString);
    // FileSystemUtil.createDirectory(maxprobabilityMapsPath);
    // FileSystemUtil.saveImages(maxprobabilityMapsPath, imageNames,
    // maxProbablityMapProcessors);
    //
    // }

    // public void writeSegmentedImagesToDisk()
    // {
    // String pathString = prefixPath + "/" + SEGMENTEDIMAGEDIR;
    // System.out.println("creating dir.. : " + pathString);
    // Path maxprobabilityMapsPath = Paths.get(pathString);
    // FileSystemUtil.createDirectory(maxprobabilityMapsPath);
    // FileSystemUtil.saveImages(maxprobabilityMapsPath, imageNames,
    // segmentedTestImageProcessors);
    //
    // }

    // public void writeLabelImagesToDisk()
    // {
    // String pathString = prefixPath + "/" + LABELIMAGEDIR;
    // System.out.println("creating dir.. : " + pathString);
    // Path maxprobabilityMapsPath = Paths.get(pathString);
    // FileSystemUtil.createDirectory(maxprobabilityMapsPath);
    // FileSystemUtil.saveImages(maxprobabilityMapsPath, imageNames,
    // labelImageProcessors);
    // }

}
