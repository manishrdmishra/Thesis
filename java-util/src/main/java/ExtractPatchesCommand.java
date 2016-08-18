package main.java;

import ij.ImagePlus;
import ij.io.FileSaver;
import ij.process.ImageProcessor;

import java.io.File;


import java.io.IOException;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Vector;

import main.java.exception.NumberOfArgumentsMismatchException;
import main.java.image.preprocessing.ExtractPatch.PatchExtractor;
import main.java.image.preprocessing.ExtractPatch.ReadPatchExtractionProperties;
import main.java.util.FileSystemUtil;
import main.java.util.ReadProperties;
//import main.proto.ToolProtos.ToolParam;
//import main.proto.ToolProtos.ExtractPatchesParam;

public class ExtractPatchesCommand implements Command
{

    /*
     * fileName stores the name of all the names of data images from which we
     * want to extract patches
     */
    private String dataFileName = null;
    private String propertyFileName = null;
    /*
     * fileName stores the name of all the names of label images from which we
     * want to extract patches
     */
    private String labelFileName = null;
    /*
     * The width and height of the patches which will be extracted from the
     * images
     */
    private int patchWidth;
    private int patchHeight;

    private int padding;
    /*
     * directory where patches will be saved the path of the directory should be
     * absolute.
     */

    private String dataPatchPath;
    private String labelPatchPath;

    // ToolParam toolParam;

    public ExtractPatchesCommand()
    {
        // TODO Auto-generated constructor stub
    }

    @Override
    public void parseArguments(String[] args)
            throws NumberOfArgumentsMismatchException
    {
        propertyFileName = args[0];
        ReadProperties readProperties = new ReadPatchExtractionProperties();
        Vector<String> properties = null;
        try
        {
            properties = readProperties.readPropertyFile(propertyFileName);
        } catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        setProperties(properties);
        // System.out.println("property : " + properties.get(0));
        // labelFileName = args[1];

        // Read the tool prototext file.
        // try
        // {
        // toolParam = ToolParam.parseFrom(new FileInputStream(args[0]));
        // } catch (IOException exception)
        // {
        // System.out.println("Exception : " + exception.toString());
        // }
        // if(toolParam != null)
        // {
        // ExtractPatchesParam extractPatchesParam =
        // toolParam.getExtractPatchesParam();
        // }
        // else
        // {
        // System.out.println("tool param is null");
        // }
        // System.out.println("patch widht : "
        // + extractPatchesParam.getPatchWidth());
        // patchWidth = Integer.parseInt(args[1]);
        // patchHeight = Integer.parseInt(args[2]);
        // padding = Integer.parseInt(args[3]);
        // directoryName = args[4];

    }

    private void setProperties(Vector<String> properties)
    {
        patchWidth = Integer.parseInt(properties.get(0));
        patchHeight = Integer.parseInt(properties.get(1));
        padding = Integer.parseInt(properties.get(2));
        dataFileName = properties.get(3);
        labelFileName = properties.get(4);
        dataPatchPath = properties.get(5);
        labelPatchPath = properties.get(6);
    }

    @Override
    public void execute()
    {

        // create instance of ExtractPatch class
        PatchExtractor extractPatches = new PatchExtractor();
        // configure the extractpatch
        extractPatches.setPatchWidth(patchWidth);
        extractPatches.setPatchHeight(patchHeight);
        extractPatches.setPadding(padding);
        // Extract patches form data images
        readImagesAndExtractPatches(dataFileName, extractPatches, dataPatchPath);

        // Extract patches from label images
        // no padding is needed for labels
        extractPatches.setPadding(0);
        readImagesAndExtractPatches(labelFileName, extractPatches,
                labelPatchPath);
    }

    private void readImagesAndExtractPatches(String fileName,
            PatchExtractor extractPatches, String pathToSavePatches)
    {
        System.out.println("executing readImagesAndExtractPatches...");
        Vector<String> fileNames = null;
        fileNames = FileSystemUtil.readFile(fileName);

        for (String file : fileNames)
        {
            // open the image file

            ImagePlus rawImage = new ImagePlus(file);

            ImageProcessor rawImageProcessor = rawImage.getProcessor();
            // set the parameters for creating and padding the patches

            extractPatches.createPatches(rawImageProcessor);
            Vector<ImageProcessor> patches = extractPatches.getPaddedPatches();

            // save the patches as file on disk
            savePatches(patches, file, pathToSavePatches);
            // clear the vector of padded patches for the next set of images
            extractPatches.clear();

        }

    }

    private void savePatches(Vector<ImageProcessor> patches,
            String patchPrefixName, String pathToSavePatches)
    {

        int count = 0;
        for (ImageProcessor patch : patches)
        {
            String name = (Paths.get(patchPrefixName)).getFileName() + "-"
                    + count;
            ImagePlus patchImage = new ImagePlus(name, patch);
            Path newPath = new File(pathToSavePatches + "/" + name).toPath();
            System.out.println("Saving patch at: " + newPath);
            FileSaver fileSaver = new FileSaver(patchImage);
            fileSaver.saveAsTiff(newPath.toString());
            count = count + 1;
        }
    }
}
