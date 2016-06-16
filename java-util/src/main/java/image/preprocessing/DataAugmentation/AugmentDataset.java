package image.preprocessing.DataAugmentation;

import ij.ImagePlus;
import ij.process.ImageProcessor;


public class AugmentDataset
{


    public ImageProcessor rotate(ImagePlus image, int angle)
    {

        ImageProcessor imageProcessor = image.getChannelProcessor();
        imageProcessor.rotate(angle);
        return imageProcessor;

    }

    public ImageProcessor flip(ImageProcessor image, int flip)
    {

        return null;
    }

}
