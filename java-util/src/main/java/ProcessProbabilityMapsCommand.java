package main.java;

import ij.ImagePlus;
import ij.io.FileSaver;
import ij.process.ByteProcessor;
import ij.process.FloatProcessor;
import ij.process.ImageProcessor;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;
import java.util.Map.Entry;

import main.java.exception.NumberOfArgumentsMismatchException;
import main.java.image.postprocessing.GenerateProbabilityMap.ProcessProbabilityMaps;
import main.java.util.FileSystemUtil;
import main.java.util.StringUtil;

/**
 * This command takes 3 inputs <1 - probabilityMapPath, 2 - testImagePath 3 -
 * prefixPath>
 * 
 * @author manish
 * 
 */
public class ProcessProbabilityMapsCommand implements Command
{
    ProcessProbabilityMaps processMaps = new ProcessProbabilityMaps();
    // private String fileName = null;
    private HashMap<String, String> imageRoiPairs = null;

    private String roiFileName;
    private String healthyFileName;
    private String tumourFileName;
    private String testFileName;

    @Override
    public void parseArguments(String[] args)
            throws NumberOfArgumentsMismatchException
    {

        // probabilityMapPath = args[0];
        // testImagePath = args[1];
        // prefixPath = args[2];
        // fileName = new String();
        // imageRoiPairs = new HashMap<String, String>();

        roiFileName = args[0];
        healthyFileName = args[1];
        tumourFileName = args[2];
        testFileName = args[3];

    }

    @Override
    public void execute()
    {
        // readImageFileNames();
        // loadProbabilityMaps();
        // loadTestImages();
        // setWidthAndHeight();
        // processProbabilityMaps();
        // writeMaxProbabilityMapsToDisk();
        // writeSegmentedImagesToDisk();
        // writeLabelImagesToDisk();

        Vector<String> roiFileLines = FileSystemUtil.readFile(roiFileName);
        Vector<String> healthyFileLines = FileSystemUtil
                .readFile(healthyFileName);
        Vector<String> tumourFileLines = FileSystemUtil
                .readFile(tumourFileName);
        Vector<String> testFileLines = FileSystemUtil.readFile(testFileName);
        for (int i = 0; i < roiFileLines.size(); i++)
        {
            // String[] filenames = lines.get(i).split("\\s+");

            // processMaps.setProbabilityMapsPath(imageFileName);
            processMaps.setRoiPath(roiFileLines.get(i));
            processMaps.setHealthyPath(healthyFileLines.get(i));
            processMaps.setTumourPath(tumourFileLines.get(i));
            processMaps.settestImagePath(testFileLines.get(i));
            processMaps.process();
            System.out.println("File : "
                    + Paths.get(healthyFileLines.get(i)).getFileName());
            System.out.println("image score : " + processMaps.getImageScore());
            System.out.println("heterogeneity score : "
                    + processMaps.getHeterogeneityScore());
            // Uncomment it to show the labelled image
            // labeledImage.show();
            // Path newPath = new File(directoryName + "/"
            // + (Paths.get(pair.getKey())).getFileName()).toPath();
            // System.out.println("Saving labeled image at: " + newPath);
            // FileSaver fileSaver = new FileSaver(labeledImage);
            // fileSaver.saveAsTiff(newPath.toString());
        }

    }
}
