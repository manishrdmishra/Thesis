package image.preprocessing.LabelImage;

import java.awt.Point;

/**
 * 
 * @author manish
 * 
 */
public class Pixel
{
    private Point coordinate;
    private int[] intensity;

    public Pixel(Point location, int[] intensity)
    {
        this.coordinate = location;
        this.intensity = intensity;
    }

    public Point getCoordinate()
    {
        return coordinate;
    }

    public int[] getIntensity()
    {
        return intensity;
    }

    public void setCoordinate(Point location)
    {
        this.coordinate = location;
    }

    public void setIntensity(int[] intensity)
    {
        this.intensity = intensity;
    }

};
