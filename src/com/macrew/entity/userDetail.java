package com.macrew.entity;

import java.util.ArrayList;
import java.util.HashMap;

public class userDetail {
	
	public static String USERNAME = new String();
	public static String PASSWORD = new String();
	public static String EMAIL = new String();
	public static String USER_ID = new String();
	public static int randQuesNo = 0;
	
	public static int h;
	
	public static ArrayList<HashMap<String, String>> QuestionsAnswerList = new ArrayList<HashMap<String, String>>();

	/**
	 * hash map key
	 */
	
	public static String QUESTIONS = "questions";
	public static String ANSWERS1 = "answer1";
	public static String ANSWERS2 = "answer2";
	public static String ANSWERS3 = "answer3";
	

}
