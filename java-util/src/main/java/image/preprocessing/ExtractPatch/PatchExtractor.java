package main.java.image.preprocessing.ExtractPatch;

import ij.ImagePlus;
import ij.gui.PolygonRoi;
import ij.gui.Roi;
import ij.process.ByteProcessor;
import ij.process.ImageProcessor;

import java.util.Vector;

public class PatchExtractor
{
    Vector<ImageProcessor> unPaddedPatches;
    Vector<ImageProcessor> paddedPatches;
    int patchWidth;
    int patchHeight;
    int patchPadding;

    public PatchExtractor()
    {
        unPaddedPatches = new Vector<ImageProcessor>();
        paddedPatches = new Vector<ImageProcessor>();
    }

    public void setPadding(int padding)
    {
        patchPadding = padding;
    }

    public void setPatchWidth(int width)

    {
        patchWidth = width;
    }

    public int getPatchHeight()
    {
        return patchHeight;
    }

    public int getPatchWidth()

    {
        return patchWidth;
    }

    public void setPatchHeight(int height)
    {
        patchHeight = height;
    }

    public void createPatches(ImageProcessor imageProcessor)
    {
        extractPatchesFromImage(imageProcessor);
        createPaddedPatches();

    }

    public Vector<ImageProcessor> getPaddedPatches()
    {
        return paddedPatches;
    }

    public Vector<ImageProcessor> getUnPaddedPatches()
    {
        return unPaddedPatches;
    }

    public void clear()
    {
        unPaddedPatches.clear();
        paddedPatches.clear();
    }

    public int[] getNumberOfPatches(int imageWidth, int imageHeight)
    {
        int[] numberOfPatches = { 0, 0 };
        // check if the patch size is smaller than image size
        if (!((patchHeight > imageHeight) || (patchWidth > imageWidth)))
        {
            int numberOfHorizontalPatches = imageWidth / patchWidth;
            int numberOfVerticalPatches = imageHeight / patchHeight;
            numberOfPatches[0] = numberOfHorizontalPatches;
            numberOfPatches[1] = numberOfVerticalPatches;
        }
        System.out.println("number of patches" + numberOfPatches[0] + " "
                + numberOfPatches[1]);
        return numberOfPatches;

    }

    private ImageProcessor createBlankPaddedPatch()
    {
        ImageProcessor blankPaddedPatchProcessor = new ByteProcessor(patchWidth
                + patchPadding, patchHeight + patchPadding);

        return blankPaddedPatchProcessor;
    }

    private void padPatch(ImageProcessor patch, ImageProcessor blankPaddedPatch)
    {

        int start = patchPadding / 2;
        for (int i = 0; i < patch.getWidth(); i++)
        {
            for (int j = 0; j < patch.getHeight(); j++)
            {
                int[] pixelValues = new int[4];
                patch.getPixel(i, j, pixelValues);
                blankPaddedPatch.putPixel(i + start, j + start, pixelValues);

            }
        }
        // to visualize the padded patch

        // (new ImagePlus("paddedImage", blankPaddedPatch)).show();
    }

    private Roi creatRectangularRoi(int x[], int y[], int type)
    {

        PolygonRoi roi = new PolygonRoi(x, y, x.length, type);
        return roi;
    }

    private void extractPatchesFromImage(ImageProcessor imageProcessor)
    {

        // 0 index - width
        // 1 index - height
        int[] numberOfPatches = getNumberOfPatches(imageProcessor.getWidth(),
                imageProcessor.getHeight());

        // Vector<ImageProcessor> patches = new Vector<ImageProcessor>();

        int startX = 0;
        int startY = 0;

        for (int i = 0; i < numberOfPatches[1]; i++)
        {
            for (int j = 0; j < numberOfPatches[0]; j++)
            {
                int[] x = { startX, startX + patchWidth, startX,
                        startX + patchWidth };
                int[] y = { startY, startY, startY + patchHeight,
                        startY + patchHeight };
                PolygonRoi roi = (PolygonRoi) creatRectangularRoi(x, y,
                        Roi.POLYGON);

                imageProcessor.setRoi(roi);
                ImageProcessor patch = imageProcessor.crop();
                // To verify that cropped image is correct
                // (new ImagePlus("unPadded Patch", patch)).show();
                unPaddedPatches.add(patch);
                startX = startX + patchWidth;

            }
            // set startX to zeor
            startX = 0;
            // increase startY by the patch width
            startY = startY + patchHeight;

        }

    }

    private void createPaddedPatches()
    {

        for (ImageProcessor unPaddedPatch : unPaddedPatches)
        {
            ImageProcessor blankPaddedPatch = createBlankPaddedPatch();
            padPatch(unPaddedPatch, blankPaddedPatch);
            // after calling padPatch blankPaddedPatch is padded
            paddedPatches.add(blankPaddedPatch);

        }

    }

}
