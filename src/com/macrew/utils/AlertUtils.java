package com.macrew.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class AlertUtils {
	Context context;
	AlertDialog.Builder alert;

	public static final String ENTER_EMPTY_USERNAME_WARNING = "Please enter username";
	public static final String ENTER_EMPTY_PASSWORD_WARNING = "Please enter password";
	public static final String ENTER_EMPTY_EMAIL_WARNING = "Please enter email";
	public static final String ENTER_EMPTY_FIELDS_WARNING = "Please fill all the fields.";
	public static final String ENTER_MISSING_FIELDS_WARNING = "Some field is missing, Please check again.";
	public static final String NO_INTERNET_CONNECTION="No Internet Connection.";
	public static final String EMAIL_SYNTAX_ERROR_WARNING="Please enter valid email.";
	public static final String USERNAME_STARTS_WITH_SPACE_WARNING="Username cannot starts with space.";
	public static final String NO_SPACE_ALLOWED="Username cannot contain spaces.";
	public static final String INVALID_EMAIL_PASSWORD_WARNING="Either email or password is incorrect.";
	public static final String ERROR_ENCOUNTERED="Error occur while processing the signup request.";
	public static final String ERROR_OCCURED_WHILE_SIGNIN="Error occured while processing the signin request.";
	public static final String PASSWORD_OR_USERNAME_INCORRECT="Either password or username is incorrect.";
	public static final String ERROR_OCCURED="Error occured while processing the request.";
	public static final String USER_ALREADY_EXIST="User Already exist.";
	public static final String PLEASE_TRY_AGAIN="Error occured while processing the request. Please try again.";
	public static final String CHECK_YOUR_INBOX="Please check your inbox.";
	public static final String YOU_HAVE_PLAYED_ALL_THE_QUESTIONS="You have played all the questions.";
	public static final String YOUR_ANSWER_IS_INCORRECT="Incorrect.";
	public static final String TIME_WARNING="Your time to answer this question has been finished.";
	public static final String NO_MORE_LIVES="Your lives for this question has been finished. Please take hint to proceed further.";
	public static final String NO_MORE_HINTS="No more hints available.Press solve to check result";
	public static final String ANSWER_IS_CORRECT="CORRECT.";
	public AlertUtils(Context cxt, String message) {
		this.context = cxt;

		alert = new AlertDialog.Builder(context);
		alert.setMessage(message)
				.setNeutralButton("OK", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {

					}
				}).show();

	}

}
