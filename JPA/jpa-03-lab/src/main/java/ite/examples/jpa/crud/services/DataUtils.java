package ite.examples.jpa.crud.services;

import java.util.ArrayList;
import java.util.List;

import ite.examples.jpa.crud.entities.Label;
import ite.examples.jpa.crud.entities.Project;
import ite.examples.jpa.crud.entities.UserData;

public final class DataUtils {
	
	public static String userDataToString(UserData ud) {
		StringBuilder sb = new StringBuilder();
		sb.append("USER:");
		sb.append(ud.getId() + ":"); 
		sb.append(ud.getFirstName() + ":");
		sb.append(ud.getSecondName() + ":");
		sb.append("Labels[");
		if (ud.getLabels() != null) {
			for (Label label: ud.getLabels()) {
				sb.append(label.getLabel() + ",");
			}
		} else {
			sb.append("null");
		}
		sb.append("]");
		sb.append("Projects[");
		if (ud.getProjects() != null) {
			for (Project project: ud.getProjects()) {
				sb.append(project.getName() + ",");
			}
		} else {
			sb.append("null");
		}
		sb.append("]");
		return sb.toString();
	}

	public static UserData createUserEntity01() {
		List<Label> labels = new ArrayList<>();
		labels.add(new Label("label1"));
		labels.add(new Label("label2"));
		List<Project> projects = new ArrayList<>();
		projects.add(new Project("project1"));
		projects.add(new Project("project2"));
		UserData user = new UserData("Juraj", "Veverka", "juraj.veverka@inter-net.sk", "0905947391");
		user.setProjects(projects);
		user.setLabels(labels);
		return user;
	}

	public static List<UserData> createEntities01() {
		List<UserData> users = new ArrayList<>();
		List<Label> labels = new ArrayList<>();
		labels.add(new Label("label1"));
		labels.add(new Label("label2"));
		
		Project project1 = new Project("project1");
		Project project2 = new Project("project2");
		Project project3 = new Project("project3");
		Project project4 = new Project("project4");
		
		UserData user1 = new UserData("Juraj", "Veverka", "juraj.veverka@inter-net.sk", "0905947391");
		List<Project> projects1 = new ArrayList<>();
		projects1.add(project1);
		projects1.add(project2);
		user1.setProjects(projects1);
		user1.setLabels(labels);
		users.add(user1);
		
		UserData user2 = new UserData("John", "Doe", "john.doe@email.com", "0905947391");
		List<Project> projects2 = new ArrayList<>();
		projects2.add(project1);
		user2.setProjects(projects2);
		users.add(user2);

		UserData user3 = new UserData("First", "Name", "first.name@email.com", "0905947391");
		List<Project> projects3 = new ArrayList<>();
		projects2.add(project1);
		projects2.add(project2);
		projects2.add(project3);
		projects2.add(project4);
		user3.setProjects(projects3);
		users.add(user3);

		return users;
	}

}
