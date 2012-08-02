/*
 * Copyright 2010: K.-M. Hansche
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 3, which
 * you can find at http://www.opensource.org/licenses/gpl-3.0.html
 * 
 */

package com.passwordmaker;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.os.Handler;
import android.view.ViewGroup.LayoutParams;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebSettings.TextSize;
import android.widget.LinearLayout;
import android.widget.Toast;

public class PasswordCalculator implements OnDismissListener {
	private static final int _TIMEOUT_SECONDS = 5;	//Standard-timeout for tryAquires. 
	protected WebView webview;
	private Dialog dialog;
	private Context ctx;
	protected Handler mHandler=new Handler();
	private String password;
	private Semaphore pwSem=new Semaphore(1,true);	//used to simulate a critical section to work with webkit

	public PasswordCalculator(Context aContext) {
		this.ctx=aContext;

		webview=new WebView(this.ctx);
		WebSettings ws=webview.getSettings();
		ws.setJavaScriptEnabled(true);
		ws.setLayoutAlgorithm(LayoutAlgorithm.NARROW_COLUMNS);
		ws.setLightTouchEnabled(true);
		ws.setTextSize(TextSize.SMALLER);
		CookieManager.setAcceptFileSchemeCookies(true);

		webview.addJavascriptInterface(new wvc(), "wvc");
		webview.loadUrl("file:///android_asset/pwm.html");

		dialog = new Dialog(this.ctx);
		dialog.setTitle(R.string.modProfile);
		dialog.setContentView(webview,new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		dialog.setOnDismissListener(this);
	}

	class wvc{
		public void updatePW(final String aPW){
			setPassword(aPW);
		}
	}

	/**
	 * Show the original profile-configuration-dialog
	 */
	public void showConfDialog(){
		dialog.show();
	}

	@Override
	public void onDismiss(DialogInterface arg0) {
		CookieSyncManager.getInstance().sync();
	}

	private void setPassword(String password) {
		if(pwSem.availablePermits()<1){
			this.password = password;
			pwSem.release();
		}
	}

	/**
	 * Return the last calculated Password
	 * 
	 * @return The Password or an empty string
	 */
	public String getPassword() {
		String tempString="";
		
		try {
			if (pwSem.tryAcquire(_TIMEOUT_SECONDS, TimeUnit.SECONDS)){
				tempString=password;
				pwSem.release();
			} else {
				Toast.makeText(ctx, R.string.could_not_get_password, Toast.LENGTH_SHORT).show();
			}
		} catch (InterruptedException e) {
			Toast.makeText(ctx, R.string.could_not_get_password, Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		}
		
		return tempString;
	}
	
	/**
	 * Sets the master-password
	 * 
	 * @param masterpw The master-password
	 * @return Success
	 */
	public boolean setMasterPassword(String masterpw){
		String url=
			"javascript:document.getElementById(\"passwdMaster\").value="+
			"\""+masterpw.replaceAll("\"",	"\\\\\"")+"\";";

		return setCredentials(url);
	}

	/**
	 * Sets the URL to generate a password for
	 * 
	 * @param url The URL to generate a Password for
	 * @return Success
	 */
	public boolean setURL(String url){
		url=
			"javascript:document.getElementById(\"preURL\").value="+
			"\""+url+"\";";
		
		return setCredentials(url);
	}

	private boolean setCredentials(String url){
		boolean res=false;
	
		try {
			if (pwSem.tryAcquire(_TIMEOUT_SECONDS, TimeUnit.SECONDS)){
				password="";
				url=url+"populateURL();"; //Calls wvc.updatePW from another thread.
				webview.loadUrl(url);
				res=true;
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		return res;
	}
}
