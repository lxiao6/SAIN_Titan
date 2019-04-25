package edu.drexel.cs.athena.parser.revisionHistory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import edu.drexel.cs.athena.data.changed_file;
import edu.drexel.cs.athena.data.commit;
import edu.drexel.cs.athena.parser.inputParser;

public class svnXmlParser extends DefaultHandler implements inputParser
{

    private SAXParser parser;
    private InputStream is;
    private StringBuilder content = new StringBuilder();

    private changed_file path;
    private commit one_commit;

    public Set<commit> history;

    public svnXmlParser(InputStream is) throws ParserConfigurationException, SAXException,
            IOException
    {

        this.is = is;
        SAXParserFactory factory = SAXParserFactory.newInstance();
        parser = factory.newSAXParser();

    }

    public svnXmlParser() throws ParserConfigurationException, SAXException, IOException
    {

        SAXParserFactory factory = SAXParserFactory.newInstance();
        parser = factory.newSAXParser();

    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException
    {
        if (content == null)
            throw new IllegalStateException(new NullPointerException());
        // content.delete(0, content.length());
        content.append(ch, start, length);
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes)
            throws SAXException
    {
        content = new StringBuilder();
        if (qName.equals("logentry")) {

            one_commit = new commit(attributes.getValue("revision"));

        }
        if (qName.equals("path")) {
            path = new changed_file();

        }

    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException
    {

        if (qName.equals("author")) {
            one_commit.author = content.toString();
        }
        if (qName.equals("date")) {
            one_commit.date = content.toString();
        }
        if (qName.equals("path")) {
            path.setPath(content.toString());
            one_commit.changed_files.add(path);
        }
        if (qName.equals("msg")) {
            one_commit.msg = content.toString();
            history.add(one_commit);
            // System.out.println(one_commit.revision);
        }
    }

    // @Override
    public void parse()
    {
        try {
            parser.parse(this.is, this);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    // @Override
    public void initParser(String[] args)
    {
        history = new HashSet<commit>();
    }

    // @Override
    public void process(String args) throws FileNotFoundException
    {
        history = new HashSet<commit>();
        File input = new File(args);
        if (input.isDirectory()) {
            for (File f : input.listFiles()) {
                this.is = new FileInputStream(f);
                parse();
            }
        }
        else {
            this.is = new FileInputStream(input);
            parse();
        }
    }

}
