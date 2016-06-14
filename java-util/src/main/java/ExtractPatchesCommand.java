package main.java;

import ij.ImagePlus;
import ij.io.FileSaver;
import ij.process.ImageProcessor;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Vector;

import main.java.exception.NumberOfArgumentsMismatchException;
import main.java.image.preprocessing.ExtractPatch.ExtractPatches;
import main.java.util.FileSystemUtil;

public class ExtractPatchesCommand implements Command
{

    /*
     * fileName stores the name of all the names of images from which we want to
     * extract patches
     */
    private String fileName = null;
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

    private String directoryName = null;

    public ExtractPatchesCommand()
    {
        // TODO Auto-generated constructor stub
    }

    @Override
    public void parseArguments(String[] args)
            throws NumberOfArgumentsMismatchException
    {
        fileName = args[0];
        patchWidth = Integer.parseInt(args[1]);
        patchHeight = Integer.parseInt(args[2]);
        padding = Integer.parseInt(args[3]);
        directoryName = args[4];

    }

    @Override
    public void execute()
    {
        Vector<String> rawFileNames = null;
        rawFileNames = FileSystemUtil.readFile(fileName);
        ExtractPatches extractPatches = new ExtractPatches();
        for (String rawFile : rawFileNames)
        {
            // open the file

            ImagePlus rawImage = new ImagePlus(rawFile);

            ImageProcessor rawImageProcessor = rawImage.getProcessor();
            // set the parameters for creating and padding the patches
            extractPatches.setPatchWidth(patchWidth);
            extractPatches.setPatchHeight(patchHeight);
            extractPatches.setPadding(padding);
            extractPatches.createPatches(rawImageProcessor);
            Vector<ImageProcessor> patches = extractPatches.getPaddedPatches();
            // save the patches as file on disk
            savePatchs(patches, rawFile);
            // clear the vector of padded patches
            extractPatches.clear();
        }

    }

    private void savePatchs(Vector<ImageProcessor> patches,
            String patchPrefixName)
    {

        int count = 0;
        for (ImageProcessor patch : patches)
        {
            String name = (Paths.get(patchPrefixName)).getFileName() + "-"
                    + count;
            ImagePlus patchImage = new ImagePlus(name, patch);
            Path newPath = new File(directoryName + "/" + name).toPath();
            System.out.println("Saving patch at: " + newPath);
            FileSaver fileSaver = new FileSaver(patchImage);
            fileSaver.saveAsTiff(newPath.toString());
            count = count + 1;
        }
    }
}
