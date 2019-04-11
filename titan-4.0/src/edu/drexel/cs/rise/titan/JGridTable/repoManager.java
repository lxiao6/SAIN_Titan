package edu.drexel.cs.rise.titan.JGridTable;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import edu.drexel.cs.rise.DesignSpace.data.refProject;

public class repoManager {
	
	private String path = "Index.xml";
	private Vector<refProject> data;
	private static repoManager singleton = new repoManager();
	
	private repoManager(){
		
	}
	
	public static repoManager getInstance(){
		return singleton;
	}
	
	public  void loadIndexFile(){
		
		FileInputStream fs;
		try {
			fs = new FileInputStream(new File(path));
			IndexParser myParser = new IndexParser(fs);
			myParser.parse();
			data = new Vector<refProject>();
			data.addAll(myParser.projs);
			System.out.println("Load Index size "+data.size());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	public void saveIndexFile(){
		/*<name>project1</name>
		<size>100</size>
		<pc>0.5</pc>
		<archLevel>10</archLevel>
		<archDepth>20</archDepth>
		<StrictIndLevel>0.4</StrictIndLevel>
		<GeneralIndLevel>0.5</GeneralIndLevel>
		<archDir>C:\Users\lx52\workspace\data\avro</archDir>*/
		try {
			FileWriter fw = new FileWriter(new File(path));
			fw.write("<?xml version=\"1.0\"?>\n");
			fw.write("<repository>\n");
			for(refProject proj:data){
				fw.write("	<project>\n");
				fw.write("		<name>"+proj.getName()+"</name>\n");
				fw.write("		<size>"+proj.getSize()+"</size>\n");
				fw.write("		<pc>"+proj.getPc()+"</pc>\n");
				fw.write("		<archLevel>"+proj.getArchLevel()+"</archLevel>\n");
				fw.write("		<archDepth>"+proj.getArchDepth()+"</archDepth>\n");
				fw.write("		<StrictIndLevel>"+proj.getsMil()+"</StrictIndLevel>\n");
				fw.write("		<GeneralIndLevel>"+proj.getgMil()+"</GeneralIndLevel>\n");
				fw.write("		<archDir>"+proj.getArchIssueDir().getAbsolutePath()+"</archDir>\n");
				fw.write("	</project>\n");
			}
			fw.write("</repository>\n");
			fw.flush();
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	public Vector<refProject> data() {
		return data;
	}
	
	public void addRefProj(refProject proj){
		data.add(proj);
	}
	
	public Vector<refProject> search(String name, int size){
		Vector<refProject> rt = new Vector<refProject>();
		rt.addAll(data);
		if( name != null){
			Set<refProject> toRemove = new HashSet<refProject>();
			for(refProject proj:rt){
				if(!proj.getName().contains(name)){
					toRemove.add(proj);
				}
			}
			for(refProject proj:toRemove){
				rt.remove(proj);
			}
			
		}
		if(size > 0){
			Set<refProject> toRemove = new HashSet<refProject>();
			for(refProject proj:rt){
				if(proj.getSize() != size){
					toRemove.add(proj);
				}
			}
			for(refProject proj:toRemove){
				rt.remove(proj);
			}
			
		}
		return rt;
	}

	public void saveChange(String orig_name, refProject projEdit) {
		System.out.println("Save change "+orig_name);
		for(refProject proj:data){
			if(proj.getName().equals(orig_name)){
				
				System.out.println("Save change "+orig_name);
				proj.setName(projEdit.getName());
				System.out.println("Save change "+proj.getName());
				proj.setArchDepth(projEdit.getArchDepth());
				proj.setArchIssueDir(projEdit.getArchIssueDir());
				proj.setArchLevel(projEdit.getArchLevel());
				proj.setgMil(projEdit.getgMil());
				proj.setsMil(projEdit.getsMil());
				proj.setPc(projEdit.getPc());
				proj.setSize(projEdit.getSize());
			}
		}
		saveIndexFile();
		
	}
	
	class IndexParser extends DefaultHandler{
		
		private int i = 0;
		private SAXParser parser;
		private InputStream is;
		private StringBuilder content = new StringBuilder();
		
		private refProject proj;
		
		public Set<refProject> projs = new HashSet<refProject>();
		
		
		
		public IndexParser(InputStream is) throws ParserConfigurationException,
		SAXException, IOException {
			
			this.is = is;
			SAXParserFactory factory = SAXParserFactory.newInstance();
			parser = factory.newSAXParser();

		}
		
		public IndexParser() throws ParserConfigurationException,
		SAXException, IOException {
			
			SAXParserFactory factory = SAXParserFactory.newInstance();
			parser = factory.newSAXParser();

		}
		
		
		@Override
		public void characters(char[] ch, int start, int length)
				throws SAXException
		{
			if (content == null)
				throw new IllegalStateException(new NullPointerException());
			//content.delete(0, content.length());
			content.append(ch, start, length);
		}
		
		@Override
		public void startElement(String uri, String localName, String qName,
				Attributes attributes) throws SAXException {
			content = new StringBuilder();
			if(qName.equals("project")){
				
				proj = new refProject();
				
			}
			
			
		}
		
		@Override
		public void endElement(String uri, String localName, String qName) throws SAXException {
			
			/*<name>project1</name>
		<size>100</size>
		<pc>0.5</pc>
		<archLevel>10</archLevel>
		<archDepth>20</archDepth>
		<StrictIndLevel>0.4</StrictIndLevel>
		<GeneralIndLevel>0.5</GeneralIndLevel>
		<archDir>C:\Users\lx52\workspace\data\avro<archDir>*/
			if(qName.equals("project")){
				
					projs.add(proj);
				
			}
			if(qName.equals("name")){
				proj.setName(content.toString());
			}
			if(qName.equals("size")){
				proj.setSize(Integer.parseInt(content.toString()));
			}
			if(qName.equals("pc")){
				proj.setPc(Double.parseDouble(content.toString()));
			}
			if(qName.equals("archLevel"))
			{
				proj.setArchLevel(Integer.parseInt(content.toString()));
			}
			if(qName.equals("archDepth")){
				proj.setArchDepth(Integer.parseInt(content.toString()));
			}
			if(qName.equals("StrictIndLevel")){
				proj.setsMil(Double.parseDouble(content.toString()));
				
			}
			if(qName.equals("GeneralIndLevel")){
				proj.setgMil(Double.parseDouble(content.toString()));
			}
			if(qName.equals("archDir")){
				proj.setArchIssueDir(new File(content.toString()));
			}
		}
		
		public void parse(){
			try {
				parser.parse(this.is, this);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void saveDelete(String selectedRow) {
		
		Set<refProject> toRemove = new HashSet<refProject>();
		for(refProject proj:data){
			if(proj.getName().equals(selectedRow)){
				toRemove.add(proj);
			}
		}
		for(refProject proj:toRemove){
			data.remove(proj);
		}
		saveIndexFile();
		
	}
	

}
