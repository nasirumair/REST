package com.REST;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

@Path("/UserService")
public class UserService {

	UserDao userDao = new UserDao();
	private static final String SUCCESS_RESULT="<result>success</result>";
	private static final String FAILURE_RESULT="<result>failure</result>";

//	@GET
//	@Path("/{user}/{text}/{lat}/{lng}/{temp}")
//	@Produces(MediaType.TEXT_PLAIN)
//	public String getText(@PathParam("text") String text, @PathParam("user") String user){
//		List<Text> textList = userDao.getAllText(user);
//		Text t = new Text(user, text);
//		textList.add(t);
//		userDao.savetext(textList);
//		return "User: "+user+" says: "+t.getText() + " at: "+t.getTime()+ " on: "+t.getDate();
//	}
	
	@PUT
	@Path("/users/setText")
	@Produces(MediaType.TEXT_PLAIN)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public String getText(@FormParam("text") String text,@FormParam("user") String user,
			@FormParam("lat") String lat, @FormParam("lng") String lng,
			@FormParam("temp") String temp,	@Context HttpServletResponse servletResponse) throws IOException{
		System.out.println(text+user+lat+lng+temp);
		List<Text> textList = userDao.getAllText(user);
		Text t = new Text(user, text, lat, lng, temp);
		textList.add(t);
		userDao.savetext(textList, user);
		return text+","+lat+","+lng+","+temp;
	}
	
	@PUT
	@Path("/users/delText")
	@Produces(MediaType.APPLICATION_XML)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public List<Text> deleteText(@FormParam("text") String text,@FormParam("user") String user,
			@FormParam("time") String time, @FormParam("date") String date,
			@Context HttpServletResponse servletResponse) throws IOException{
		System.out.println(text+user+time+date);
		List<Text> newTextList = new ArrayList<Text>();
		List<Text> textList = userDao.getAllText(user);
		for(Text t: textList){
			System.out.println(","+t.getText().equals(text)+",");
			if(t.getUserName().equals(user) && t.getText().equals(text) && t.getTime().equals(time) && t.getDate().equals(date)){
				continue;
			}else{
				if(t != null){
					newTextList.add(t);
				}
				System.out.println("element removed!");
			}
		}
		if(newTextList.size() == 0){
			userDao.savetext(null, user);
			return null;
		}
		userDao.savetext(newTextList, user);

		return userDao.getAllText(user);
	}
	
	@GET
	@Path("/users/{user}/{text}/{reply}")
	@Produces(MediaType.TEXT_PLAIN)
	public String getReplyText(@PathParam("text") String text, @PathParam("user") String user, @PathParam("reply") String reply){
		List<Text> textList = userDao.getAllText(user);
		userDao.setReply(textList, user, text, reply);
		System.out.println(reply);
		return reply;
	}
	
	@POST
	@Path("/users/setReplyText")
	@Produces(MediaType.TEXT_PLAIN)
	public String getReplyText(@FormParam("text") String text,@FormParam("user") String user,
			@FormParam("reply") String reply,	@Context HttpServletResponse servletResponse) throws IOException{
		List<Text> textList = userDao.getAllText(user);
		userDao.setReply(textList, user, text, reply);
		System.out.println(reply);
		return reply;
	}

	@GET
	@Path("/users/getAll/{user}")
	@Produces(MediaType.APPLICATION_XML)
	public List<Text> getAllText(@PathParam("user") String user){
		return   userDao.getAllText(user); 

	}

	@GET
	@Path("/users")
	@Produces(MediaType.APPLICATION_XML)
	public List<User> getUsers(){
		return userDao.getAllUsers();
	}

	@GET
	@Path("/users/{userid}")
	@Produces(MediaType.APPLICATION_XML)
	public User getUser(@PathParam("userid") int userid){
		return userDao.getUser(userid);
	}

	@PUT
	@Path("/users")
	@Produces(MediaType.APPLICATION_XML)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public String createUser(@FormParam("id") int id,
			@FormParam("name") String name,
			@FormParam("profession") String profession,
			@Context HttpServletResponse servletResponse) throws IOException{
		User user = new User(id, name, profession);
		int result = userDao.addUser(user);
		if(result == 1){
			return SUCCESS_RESULT;
		}
		return FAILURE_RESULT;
	}

	@POST
	@Path("/users")
	@Produces(MediaType.APPLICATION_XML)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public String updateUser(@FormParam("id") int id,
			@FormParam("name") String name,
			@FormParam("profession") String profession,
			@Context HttpServletResponse servletResponse) throws IOException{
		User user = new User(id, name, profession);
		int result = userDao.updateUser(user);
		if(result == 1){
			return SUCCESS_RESULT;
		}
		return FAILURE_RESULT;
	}

	@DELETE
	@Path("/users/{userid}")
	@Produces(MediaType.APPLICATION_XML)
	public String deleteUser(@PathParam("userid") int userid){
		int result = userDao.deleteUser(userid);
		if(result == 1){
			return SUCCESS_RESULT;
		}
		return FAILURE_RESULT;
	}

	@OPTIONS
	@Path("/users")
	@Produces(MediaType.APPLICATION_XML)
	public String getSupportedOperations(){
		return "<operations>GET, PUT, POST</operations>";
	}


}