package com.REST;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class UserDao {
	List<User> userList = null;
	public List<User> getAllUsers(){
		getUsers();
		return userList;
	}

	private void getUsers() {
		// TODO Auto-generated method stub
		JDBC db = new JDBC();
		HashMap<Integer, User> hm = db.getData("jdbc:sqlserver://localhost;Database=testDB", "dpmdev", "dpmdev");
		userList = new ArrayList<User>();
		Iterator it = hm.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pair = (Map.Entry)it.next();
			userList.add((User) pair.getValue());
			it.remove(); // avoids a ConcurrentModificationException
		}
	}

	public User getUser(int id){
		List<User> users = getAllUsers();

		for(User user: users){
			if(user.getId() == id){
				return user;
			}
		}
		return null;
	}

	public int addUser(User pUser){
		List<User> userList = getAllUsers();
		boolean userExists = false;
		for(User user: userList){
			if(user.getId() == pUser.getId()){
				userExists = true;
				break;
			}
		}		
		if(!userExists){
			userList.add(pUser);
			saveUserList(userList);
			return 1;
		}
		return 0;
	}

	public int updateUser(User pUser){
		List<User> userList = getAllUsers();
		for(User user: userList){
			if(user.getId() == pUser.getId()){
				int index = userList.indexOf(user);			
				userList.set(index, pUser);
				saveUserList(userList);
				return 1;
			}
		}		
		return 0;
	}

	public int deleteUser(int id){
		List<User> userList = getAllUsers();

		for(User user: userList){
			if(user.getId() == id){
				int index = userList.indexOf(user);			
				userList.remove(index);
				saveUserList(userList);
				return 1;   
			}
		}		
		return 0;
	}

	private void saveUserList(List<User> userList){
		JDBC db = new JDBC();
		db.postData("jdbc:sqlserver://localhost;Database=testDB", "dpmdev", "dpmdev", userList);
	}
	
	public void savetext(List<Text> textList){
		JDBC db = new JDBC();
		db.postText("jdbc:sqlserver://localhost;Database=testDB", "dpmdev", "dpmdev", textList);
	}
	
	public List<Text> getAllText(String user){
		JDBC db = new JDBC();
		List<Text> textList = db.getTextData("jdbc:sqlserver://localhost;Database=testDB", "dpmdev", "dpmdev", user);
		return textList;
	}

	public void setReply(List<Text> textList, String user, String text, String reply) {
		// TODO Auto-generated method stub
		for(Text t: textList){
			if (t.getUserName().equals(user) && t.getText().equals(text)){
				t.setReply(reply);
				break;
			}
		}
		savetext(textList);
	}
}