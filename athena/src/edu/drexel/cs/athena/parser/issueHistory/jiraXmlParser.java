package edu.drexel.cs.athena.parser.issueHistory;

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

import edu.drexel.cs.athena.data.issue;
import edu.drexel.cs.athena.parser.inputParser;

public class jiraXmlParser extends DefaultHandler implements inputParser
{

    private SAXParser parser;
    private InputStream is;
    private StringBuilder content = new StringBuilder();
    private issue one_issue;
    public Set<issue> all_issues = new HashSet<issue>();
    int itemTag = 0;

    public jiraXmlParser(InputStream is) throws ParserConfigurationException, SAXException,
            IOException
    {

        this.is = is;
        SAXParserFactory factory = SAXParserFactory.newInstance();
        parser = factory.newSAXParser();

    }

    public jiraXmlParser() throws ParserConfigurationException, SAXException, IOException
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

        content.delete(0, content.length());
        if (qName.equals("item")) {
            one_issue = new issue();
            itemTag = 1;
        }
        /*
         * if(qName.equals("description")){
         * 
         * }
         * if(qName.equals("key")){
         * 
         * }
         * if(qName.equals("summary")){
         * 
         * }
         * if(qName.equals("type")){
         * 
         * }
         * if(qName.equals("priority")){
         * 
         * }
         * if(qName.equals("status")){
         * 
         * }
         * if(qName.equals("resolution")){
         * 
         * }
         */
        if (qName.equals("assignee")) {
            String username = attributes.getValue("username");
            one_issue.setAssignee_username(username);

        }
        if (qName.equals("reporter")) {
            String username = attributes.getValue("username");
            one_issue.setReporter_username(username);
        }/*
          * if(qName.equals("created")){
          * 
          * }
          * if(qName.equals("updated")){
          * 
          * }
          * if(qName.equals("resolved")){
          * 
          * }
          */

    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException
    {

        if (itemTag == 1) {
            if (qName.equals("item")) {
                all_issues.add(one_issue);
                itemTag = 0;
            }
            if (qName.equals("description")) {
                one_issue.setDescription(content.toString());
            }
            if (qName.equals("key")) {
                one_issue.setKey(content.toString());
            }
            if (qName.equals("summary")) {
                one_issue.setSummary(content.toString());
            }
            if (qName.equals("type")) {
                one_issue.setType(content.toString());
            }
            if (qName.equals("priority")) {
                one_issue.setPriority(content.toString());
            }
            if (qName.equals("status")) {
                one_issue.setStatus(content.toString());
            }
            if (qName.equals("resolution")) {
                one_issue.setResolution(content.toString());
            }
            if (qName.equals("assignee")) {
                one_issue.setAssignee(content.toString());
            }
            if (qName.equals("reporter")) {
                one_issue.setReporter(content.toString());
            }
            if (qName.equals("created")) {
                one_issue.setCreated(content.toString());
            }
            if (qName.equals("updated")) {
                one_issue.setUpdated(content.toString());
            }
            if (qName.equals("resolved")) {
                one_issue.setResolved(content.toString());
            }
        }

    }

    // @Override
    public void initParser(String[] args)
    {

    }

    // @Override
    public void parse()
    {
        try {
            parser.parse(this.is, this);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        catch (SAXException e) {
            e.printStackTrace();
        }
    }

    // @Override
    public void process(String args) throws FileNotFoundException
    {
        all_issues = new HashSet<issue>();
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
