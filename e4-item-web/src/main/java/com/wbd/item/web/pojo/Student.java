package com.wbd.item.web.pojo;

import java.util.Date;

public class Student {
	
	private Integer id;
	
	private String userName;
	
	private Integer age;
	
	private Date birthday;

	
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public Student() {
		super();
	}

	public Student(Integer id, String userName, Integer age, Date birthday) {
		super();
		this.id = id;
		this.userName = userName;
		this.age = age;
		this.birthday = birthday;
	}
	
	

}
