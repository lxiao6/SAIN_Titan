package edu.drexel.cs.athena.parser.cytoscape;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import edu.drexel.cs.athena.data.edge;
import edu.drexel.cs.athena.parser.inputParser;
//import edu.drexel.cs.athena.util.pathUtil;

public class fileDependencyCytoscapeParser extends DefaultHandler implements inputParser
{

    private SAXParser parser;
    private InputStream is;
    private StringBuilder content = new StringBuilder();

    int id = 0;
    int s = 0, t = 0;
    edge e;
    public Set<String> dpTypes = new HashSet<String>();
    public Map<Integer, String> nodes = new TreeMap<Integer, String>();
    public Set<edge> edges = new HashSet<edge>();
    public Set<String> tps = new HashSet<String>();

    private enum element_type
    {
        blank, node, edge;
    }

    private element_type status = element_type.blank;

    public fileDependencyCytoscapeParser(InputStream is) throws ParserConfigurationException,
            SAXException, IOException
    {

        this.is = is;
        SAXParserFactory factory = SAXParserFactory.newInstance();
        parser = factory.newSAXParser();

    }

    public fileDependencyCytoscapeParser() throws ParserConfigurationException, SAXException,
            IOException
    {

        SAXParserFactory factory = SAXParserFactory.newInstance();
        parser = factory.newSAXParser();
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException
    {
        if (content == null)
            throw new IllegalStateException(new NullPointerException());
        content.delete(0, content.length());
        content.append(ch, start, length);
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes)
            throws SAXException
    {

        if (qName.equals("node")) {
            status = element_type.node;
            id = Integer.parseInt(attributes.getValue("id"));
        }
        if (qName.equals("edge")) {
            e = new edge();
            status = element_type.edge;
            s = Integer.parseInt(attributes.getValue("source"));
            t = Integer.parseInt(attributes.getValue("target"));
            e.s = t;
            e.t = s;
        }
        if (qName.equals("att")) {
            if (status == element_type.node) {
                String name = attributes.getValue("name");
                if (name.equalsIgnoreCase("longName")) {

                    String value = attributes.getValue("value");
                    nodes.put(id, value);

                }
            }
            if (status == element_type.edge) {
                String name = attributes.getValue("name");
                if (name.equalsIgnoreCase("dependency kind")) {
                    String value = attributes.getValue("value");
                    String types[] = value.split(",");
                    for (String tp : types) {
                        e.types.add(tp.trim());
                        tps.add(tp.trim());
                        dpTypes.add(tp.trim());
                    }

                    if (nodes.containsKey(e.s) && nodes.containsKey(e.t))
                        edges.add(e);
                }
            }
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException
    {

    }

    public void initParser(String[] args)
    {
        // TODO Auto-generated method stub

    }

    public void process(String args) throws FileNotFoundException
    {

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

}
