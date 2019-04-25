package edu.drexel.cs.athena.data;

import java.util.HashSet;
import java.util.Set;

public class commit
{

    public String revision;
    public String author;
    public String date;
    public Set<changed_file> changed_files;
    public String msg;
    public String tag;

    public commit()
    {
        changed_files = new HashSet<changed_file>();
    }

    public commit(String id)
    {
        revision = id;
        changed_files = new HashSet<changed_file>();
    }

}
