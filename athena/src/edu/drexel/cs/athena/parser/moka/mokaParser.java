package edu.drexel.cs.athena.parser.moka;

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

public class mokaParser extends DefaultHandler implements inputParser
{

    private SAXParser parser;
    private InputStream is;
    private StringBuilder content = new StringBuilder();

    public Set<String> dpTypes = new HashSet<String>();
    public Map<Integer, String> nodes = new TreeMap<Integer, String>();
    public Set<edge> edges = new HashSet<edge>();
    public String valid_prefix = "";

    private String current_node = "";
    private int current_node_id;

    public mokaParser(String prefix) throws ParserConfigurationException, SAXException, IOException
    {

        SAXParserFactory factory = SAXParserFactory.newInstance();
        parser = factory.newSAXParser();
        valid_prefix = prefix;
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

        if (qName.equals("entity")) {

            String name = attributes.getValue("name");
            if (name.startsWith(valid_prefix)) {
                current_node_id = addNode(name);
                current_node = name;

                String parent = attributes.getValue("parent");
                if (parent != null && parent.startsWith(valid_prefix)) {
                    addEdge(current_node_id, addNode(parent), "ih");
                }

                String enclosing = attributes.getValue("enclosing");
                if (enclosing != null && enclosing.startsWith(valid_prefix)) {
                    addEdge(current_node_id, addNode(enclosing), "nt");
                }

            }
            else {
                current_node = null;
            }
        }

        if (qName.equals("field") && current_node != null) {

            String type = attributes.getValue("type");
            if (type.startsWith(valid_prefix)) {
                addEdge(current_node_id, addNode(type), "ag");
            }
        }

        if (current_node != null
                && (qName.equals("param") || qName.equals("invoke") || qName.equals("construct") || qName
                        .equals("access"))) {

            String type = attributes.getValue("type");
            if (type.startsWith(valid_prefix)) {
                addEdge(current_node_id, addNode(type), "dp");
            }

        }

    }

    private int addNode(String name)
    {
        for (int id : nodes.keySet()) {
            if (nodes.get(id).equals(name)) {
                return id;
            }
        }
        int rt = nodes.size() + 1;
        nodes.put(rt, name);
        return rt;
    }

    private void addEdge(int first, int second, String type)
    {
        dpTypes.add(type);
        for (edge ed : edges) {
            if (ed.s == first && ed.t == second) {
                ed.types.add(type);
                return;
            }
        }
        edge ned = new edge();
        ned.t = first;
        ned.s = second;
        ned.types.add(type);
        edges.add(ned);

    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException
    {
        if (current_node != null && qName.equals("interface")) {
            String inter = content.toString();
            if (inter.startsWith(valid_prefix)) {
                addEdge(current_node_id, addNode(inter), "impl");
            }
        }

    }

    // @Override
    public void initParser(String[] args)
    {
        // TODO Auto-generated method stub

    }

    // @Override
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

}
