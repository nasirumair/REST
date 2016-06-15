package com.REST;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "textData")
public class Text implements Serializable {
	
	DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
	DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
	private static final long serialVersionUID = 1L;
	private String userName;
	private String text;
	private String time;
	private String date;
	private String reply;
	private String lat;
	private String lng;
	private String temp;

	public String getReply() {
		return reply;
	}

	public String getLat() {
		return lat;
	}

	@XmlElement
	public void setLat(String lat) {
		this.lat = lat;
	}

	public String getLng() {
		return lng;
	}
	
	@XmlElement
	public void setLng(String lng) {
		this.lng = lng;
	}

	public String getTemp() {
		return temp;
	}

	@XmlElement
	public void setTemp(String temp) {
		this.temp = temp;
	}

	@XmlElement
	public void setReply(String reply) {
		this.reply = reply;
	}

	public String getTime() {
		return time;
	}

	@XmlElement
	public void setTime(String time) {
		this.time = time;
	}

	public String getDate() {
		return date;
	}
	
	@XmlElement
	public void setDate(String date) {
		this.date = date;
	}

	public Text(){}

	public Text(String userName, String text, String lat, String lng, String temp){
		this.userName = userName;
		this.text = text;
		this.time = timeFormat.format(new Date());
		this.date = dateFormat.format(new Date());
		this.reply = "";
		this.lat = lat;
		this.lng = lng;
		this.temp = temp;
	}
	
	public Text(String userName, String text, String time, String date, String lat, String lng, String temp, String reply){
		this.userName = userName;
		this.text = text;
		this.time = time;
		this.date = date;
		this.reply = reply;
		this.lat = lat;
		this.lng = lng;
		this.temp = temp;
	}

	public String getUserName() {
		return userName;
	}
	
	@XmlElement
	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getText() {
		return text;
	}
	
	@XmlElement
	public void setText(String text) {
		this.text = text;
	}

}