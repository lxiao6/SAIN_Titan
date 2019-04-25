package edu.drexel.cs.athena.data;

public class changed_file
{
    private String path;
    private String churn;

    public changed_file()
    {
    }

    public changed_file(String path)
    {
        this.setPath(path);
    }

    public changed_file(String path, String churn)
    {
        this.setPath(path);
        this.setChurn(churn);
    }

    public String getPath()
    {
        return path;
    }

    public void setPath(String path)
    {
        this.path = path;
    }

    public String getChurn()
    {
        return churn;
    }

    public void setChurn(String churn)
    {
        this.churn = churn;
    }

}
