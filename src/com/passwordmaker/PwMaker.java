/*
 * Copyright 2010: K.-M. Hansche
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 3, which
 * you can find at http://www.opensource.org/licenses/gpl-3.0.html
 * 
 */

package com.passwordmaker;

import android.app.Activity;
import android.app.Dialog;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.CookieSyncManager;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;
import android.content.ClipboardManager;
import android.view.View;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import org.daveware.passwordmaker.PasswordMaker;
//import com.passwordmaker.PasswordCalculator;

public class PwMaker extends Activity implements View.OnClickListener{
    protected EditText edDomain;
    protected EditText edPW;
    protected Button btnGo;
    protected Button btnUpdate;
    protected Button btnCopy;
    protected CheckBox cbEmptyFields;
    protected TextView tvPW;
    protected Dialog dialog;
    private PasswordCalculator pwc; 

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        String text = intent.getStringExtra(Intent.EXTRA_TEXT);

        setContentView(R.layout.main);

        tvPW=(TextView) findViewById(R.id.textPW);

        pwc=new PasswordCalculator(this);
        CookieSyncManager.createInstance(this);

        edDomain=(EditText) findViewById(R.id.editDomain);
        edPW=(EditText) findViewById(R.id.editPW);
        btnGo=(Button) findViewById(R.id.btnGo);
        btnGo.setOnClickListener(this);
        btnUpdate=(Button) findViewById(R.id.btnUpdate);
        btnUpdate.setOnClickListener(this);
        btnCopy=(Button) findViewById(R.id.btnCopy);
        btnCopy.setOnClickListener(this);
        cbEmptyFields=(CheckBox) findViewById(R.id.emptyFieldsCB);
        findViewById(R.id.btnProfile).setOnClickListener(this);

        if(text!=null) {
            edDomain.setText(intent.getStringExtra(Intent.EXTRA_TEXT));
            edPW.requestFocus();
        }

        SharedPreferences settings=getPreferences(MODE_PRIVATE);
        cbEmptyFields.setChecked(settings.getBoolean("emptyFields", true));
    }

    public void onClick(View v) {
        String pw;

        switch (v.getId()) {
            case R.id.btnProfile:
                pwc.showConfDialog();
                break;
            case R.id.btnUpdate:
                if(setCredentials())
                    if((pw=pwc.getPassword())!= "")
                        tvPW.setText(pw);
                break;
            case R.id.btnCopy:
                if(setCredentials()){
                    if((pw=pwc.getPassword())!= ""){
                        tvPW.setText(pw);
                        copyPwToClipboard(pw);
                    }
                }
                break;
            case R.id.btnGo:
                if(setCredentials()){
                    if((pw=pwc.getPassword())!= ""){
                        tvPW.setText(pw);
                        copyPwToClipboard(pw);
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        String link = edDomain.getText().toString();
                        Uri u = Uri.parse(link);
                        i.setData(u);
                        try {
                            startActivity(i);
                        } 
                        catch (ActivityNotFoundException e) {
                            // http://?
                            u = Uri.parse("http://" + link);
                            i.setData(u);
                            try {
                                startActivity(i);
                            } 
                            catch (ActivityNotFoundException e2) {
                                Toast.makeText(this, R.string.urlnotfound, Toast.LENGTH_SHORT).show();
                            }
                        }
                    }			
                }

                break;
        }
    }

    private boolean setCredentials(){
        boolean res=false;

        if (edDomain.getText().length()>0 && edPW.getText().length()>0)
            if(
                    pwc.setURL(edDomain.getText().toString())
                    &&	pwc.setMasterPassword(edPW.getText().toString())
              )
                res=true;

        return res;
    }

    private void copyPwToClipboard(String pw) {
        ClipboardManager cb = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        cb.setText(pw);
        Toast.makeText(this, R.string.copiedtoclip, Toast.LENGTH_SHORT).show();
    }


    @Override
    protected void onResume() {
        CookieSyncManager.getInstance().startSync();
        super.onResume();
    }

    @Override
    protected void onPause() {
        CookieSyncManager.getInstance().startSync();
        if(cbEmptyFields.isChecked()){
            pwc.setMasterPassword("");
            pwc.setURL("");
            edPW.setText("");
            edDomain.setText("");
            tvPW.setText("");
        }

        super.onPause();
    }

    @Override
    protected void onStop() {
        SharedPreferences settings=getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean("emptyFields", cbEmptyFields.isChecked());
        editor.commit();

        super.onStop();
    }
}
