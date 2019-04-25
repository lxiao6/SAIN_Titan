package edu.drexel.cs.athena.data;

import java.util.HashSet;
import java.util.Set;

public class edge{
	public int s;
	public int t;
	public Set<String> types = new HashSet<String>();
	
	public int s(){
		return s;
	}
	public int t(){
		return t;
	}
	public Set<String> dps(){
		return types;
	}
	
	
}
