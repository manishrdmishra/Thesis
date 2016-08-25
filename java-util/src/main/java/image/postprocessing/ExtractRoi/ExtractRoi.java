package main.java.image.postprocessing.ExtractRoi;

import java.nio.file.Paths;

import ij.ImagePlus;
import ij.measure.ResultsTable;
import ij.plugin.filter.BackgroundSubtracter;
import ij.plugin.filter.ParticleAnalyzer;
import ij.plugin.frame.RoiManager;
import ij.process.FloodFiller;
import ij.process.ImageProcessor;

public class ExtractRoi
{
    private String imageName;
    private String pathToSaveRoi;
    private ImageProcessor imageProcessor;
    int roiCount = 0;
    private final int NUMBER_OF_EROSION = 20;
    private final int NUMBER_OF_DILATION = 10;
    private final int BACKGROUND = 255;
    private final int FOREGROUND = 0;

    public ExtractRoi(String imageName, String pathToSaveRoi)
    {
        this.imageName = imageName;
        this.pathToSaveRoi = pathToSaveRoi;
    }

    public ExtractRoi()
    {
        this.imageName = null;
        this.pathToSaveRoi = null;
    }

    public ExtractRoi(String pathToSaveRoi)
    {
        this.pathToSaveRoi = pathToSaveRoi;
    }

    public void getRoi()
    {
        initilizeImageProcessor();

        // create the binary image by thresholding with the
        // default threshold value
        thresholdImage();

        // fill the holes to complete the mitochondria
        fill(imageProcessor, FOREGROUND, BACKGROUND);

        // do the erosion to split the attached mitochondria
        for (int i = 0; i < NUMBER_OF_EROSION; i++)
        {
            imageProcessor.erode();
        }
        // do dilation to gain the lost pixels during erosion
        for (int i = 0; i < NUMBER_OF_DILATION; i++)
        {
            imageProcessor.dilate();
        }
        ResultsTable table = new ResultsTable();

        RoiManager manager = new RoiManager(true);

        ParticleAnalyzer.setRoiManager(manager);
        ParticleAnalyzer analyzer = new ParticleAnalyzer(
                ij.plugin.filter.ParticleAnalyzer.ADD_TO_MANAGER,
                ij.measure.Measurements.AREA, table, 5000, Double.MAX_VALUE, 0,
                1);
        ImagePlus imagePlus = new ImagePlus("test", imageProcessor);

        analyzer.analyze(imagePlus, imageProcessor);
        String path = pathToSaveRoi + "/" + Paths.get(imageName).getFileName()
                + "-roi.zip";
        System.out.println("Saving roi at path : " + path);
        manager.runCommand("Save", path);
        roiCount = manager.getCount();
        System.out.println("Number of roi extracted : " + roiCount);
        // analyzer.run(imageProcessor);
        (new ImagePlus("test", imageProcessor)).show();

    }

    public void setImageName(String imageName)
    {
        this.imageName = imageName;
    }

    public void setRoiPathToSave(String pathToSaveRoi)
    {
        this.pathToSaveRoi = pathToSaveRoi;

    }

    public void initilizeImageProcessor()
    {
        imageProcessor = (new ImagePlus(imageName)).getChannelProcessor();
    }

    public void thresholdImage()
    {

        int threshold = imageProcessor.getAutoThreshold();
        System.out.println("The automatic threshold value : " + threshold);
        imageProcessor.threshold(threshold);

    }

    // TODO: cite this paper Binary fill by Gabriel Landini, G.Landini at
    // bham.ac.uk
    // 21/May/2008
    void fill(ImageProcessor ip, int foreground, int background)
    {
        int width = ip.getWidth();
        int height = ip.getHeight();
        FloodFiller ff = new FloodFiller(ip);
        ip.setColor(127);
        for (int y = 0; y < height; y++)
        {
            if (ip.getPixel(0, y) == background)
                ff.fill(0, y);
            if (ip.getPixel(width - 1, y) == background)
                ff.fill(width - 1, y);
        }
        for (int x = 0; x < width; x++)
        {
            if (ip.getPixel(x, 0) == background)
                ff.fill(x, 0);
            if (ip.getPixel(x, height - 1) == background)
                ff.fill(x, height - 1);
        }
        byte[] pixels = (byte[]) ip.getPixels();
        int n = width * height;
        for (int i = 0; i < n; i++)
        {
            if (pixels[i] == 127)
                pixels[i] = (byte) background;
            else
                pixels[i] = (byte) foreground;
        }
    }

}
