package edu.drexel.cs.athena.parser;

import java.io.FileNotFoundException;

public interface inputParser
{

    public void initParser(String[] args);

    public void process(String args) throws FileNotFoundException;

    public void parse();
}
