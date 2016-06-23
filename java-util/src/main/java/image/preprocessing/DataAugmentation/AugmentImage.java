package main.java.image.preprocessing.DataAugmentation;

import ij.process.ImageProcessor;

import java.util.Vector;

public class AugmentImage
{
    private ImageProcessor image;
    private Vector<ImageProcessor> augmented_images;
    private final int MAX_ANGLE = 360;
    private final int DEGREE_OF_ROTATION = 90;

    public void augment()
    {

        for (int angle = 0; angle < MAX_ANGLE; angle = angle
                + DEGREE_OF_ROTATION)
        {
            ImageProcessor rotetedImage = rotate(angle);
            ImageProcessor flipedVertical = flipVertical(rotetedImage);
            ImageProcessor flipedHorizontal = flipHorizontal(rotetedImage);
            augmented_images.add(rotetedImage);
            augmented_images.add(flipedVertical);
            augmented_images.add(flipedHorizontal);

        }

    }

    public Vector<ImageProcessor> getAugmentedImages()
    {
        return augmented_images;
    }

    public ImageProcessor getImage()
    {
        return image;
    }

    public ImageProcessor rotate(int angle)
    {
        /*
         * a new duplicate image is created because the operation on original
         * image will change the original image
         */
        ImageProcessor duplicate = image.duplicate();
        duplicate.rotate(angle);
        return duplicate;

    }

    public ImageProcessor flipVertical(ImageProcessor rotetedImage)
    {
        ImageProcessor duplicate = rotetedImage.duplicate();
        duplicate.flipVertical();
        return duplicate;
    }

    public ImageProcessor flipHorizontal(ImageProcessor rotetedImage)
    {
        ImageProcessor duplicate = rotetedImage.duplicate();
        duplicate.flipHorizontal();
        return duplicate;
    }

}
