package com.zos.activiti.candidate;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthService implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8937381178077325653L;

	private String lastUser = "joker";
	
	private String assigneeUser = "joker";
	
	private List<String> candidateUsers = new ArrayList<String>();
	
	private List<String> candidateGroups = new ArrayList<String>();
	
	public List<String> getCandidateUsers() {
		this.candidateUsers.add("joker");
		return this.candidateUsers;
	}
	
	public List<String> getCandidateGroups() {
		this.candidateGroups.add("management");
		this.candidateGroups.add("boss");
		return this.candidateGroups;
	}
}
