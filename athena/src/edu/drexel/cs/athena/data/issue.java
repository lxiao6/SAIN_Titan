package edu.drexel.cs.athena.data;



public class issue {
	private String key;
	private String description;
	private String summary;
	private String type;
	private String priority;
	private String status;
	private String resolution;
	private String assignee;
	private String reporter;
	private String assignee_username;
	private String reporter_username;
	private String created;
	private String updated;
	private String resolved;
	
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getSummary() {
		return summary;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getPriority() {
		return priority;
	}
	public void setPriority(String priority) {
		this.priority = priority;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getResolution() {
		return resolution;
	}
	public void setResolution(String resolution) {
		this.resolution = resolution;
	}
	public String getAssignee() {
		return assignee;
	}
	public void setAssignee(String assignee) {
		this.assignee = assignee;
	}
	public String getReporter() {
		return reporter;
	}
	public void setReporter(String reporter) {
		this.reporter = reporter;
	}
	public String getCreated() {
		return created;
	}
	
	public String getUpdated() {
		return updated;
	}
	
	public String getResolved() {
		return resolved;
	}
	
	public String getAssignee_username() {
		return assignee_username;
	}
	public void setAssignee_username(String assignee_username) {
		this.assignee_username = assignee_username;
	}
	public String getReporter_username() {
		return reporter_username;
	}
	public void setReporter_username(String reporter_username) {
		this.reporter_username = reporter_username;
	}
	public void setCreated(String string) {
		
		this.created = string;
		
	}
	public void setUpdated(String string) {
		
		this.updated = string;
		
	}
	public void setResolved(String string) {
		
		this.resolved = string;
		
	}
	
	
	
}
