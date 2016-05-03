package image.preprocessing;

import ij.gui.Roi;
import ij.io.RoiDecoder;
import ij.process.ImageProcessor;

import java.awt.Point;
import java.awt.Rectangle;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Vector;

/***
 * 
 * @author manish This class reads a Roi and stores its pixel indices and pixel
 *         intensity.
 * 
 */
// List Pixels In ROI
//
// Displays the coordinates and values of
// the pixels within a non-rectangular ROI.
//
// var img = IJ.getImage();
// var roi = img.getRoi();
// var mask = roi!=null?roi.getMask():null;
// if (mask==null)
// IJ.error("Non-rectangular ROI required");
// var ip = img.getProcessor();
// var r = roi.getBounds();
// var z = img.getCurrentSlice()-1;
// for (var y=0; y<r.height; y++) {
// for (var x=0; x<r.width; x++) {
// if (mask.getPixel(x,y)!=0)
// IJ.log(x+" \t"+y+" \t"+z+"  \t"+ip.getPixel(r.x+x,r.y+y));
// }
// }

public class RoiPixels
{

    Roi roi;
    Vector<Pixel> pixels;

    RoiPixels(Path roiFilePath, ImageProcessor segmenteImageProcessor)
    {
        roi = null;
        pixels = new Vector<Pixel>();
        initializeRoi(roiFilePath);
        fillPixelIndicesAndIntensity(segmenteImageProcessor);

    }

    public Vector<Pixel> getRoiPixels()
    {
        return pixels;
    }

    public Roi getRoi()
    {
        return roi;
    }

    private void initializeRoi(Path roiFilePath)
    {

        RoiDecoder roiDecoder = new RoiDecoder(roiFilePath.toString());
        try
        {
            roi = roiDecoder.getRoi();

        } catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void fillPixelIndicesAndIntensity(
            ImageProcessor segmenteImageProcessor)
    {

        if (roi != null)
        {
            System.out.println("name :" + roi.getLength());

            System.out.println("name :" + roi.getName());

            ImageProcessor roiMask = roi.getMask();
            Rectangle roiRect = roi.getBounds();
            for (int y = 0; y < roiRect.height; y++)
            {
                for (int x = 0; x < roiRect.width; x++)
                {
                    if (roiMask.getPixel(x, y) != 0)
                    {

                        int[] intensity = new int[4];
                        Point point = new Point(roiRect.x + x, roiRect.y + y);
                        segmenteImageProcessor.getPixel(point.x, point.y,
                                intensity);

                        for (int i = 0; i < intensity.length; i++)
                        {
                            // System.out.print(" " + intensity[i]);
                        }
                        System.out.println();
                        Pixel pixel = new Pixel(point, intensity);
                        pixels.add(pixel);

                    }
                }
            }

        }
    }

}
