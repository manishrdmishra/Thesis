package main.java.image.postprocessing.GenerateProbabilityMap;

import java.util.Vector;

public class ImageStats
{
    private String fileName;
    private double imageScore;
    private double heterogeneityIndex;
    /*
     * This vector contains the scores of the individual objects in the image.
     */
    private Vector<Double> objectScores;

    public String getFileName()
    {
        return fileName;
    }

    public void setFileName(String name)
    {
        fileName = name;
    }

    public double getImageScore()
    {
        return imageScore;
    }

    public void setImageScore(double score)
    {
        imageScore = score;
    }

    public void setHeterogeneityIndex(double heterogeneity)
    {
        heterogeneityIndex = heterogeneity;
    }

    public double getHeterogeneityIndex()
    {
        return heterogeneityIndex;
    }

    public Vector<Double> getAllObjectsScores()
    {
        return objectScores;
    }

    public void setAllObjectsScores(Vector<Double> allObjectScores)
    {
        objectScores = allObjectScores;
    }

}
