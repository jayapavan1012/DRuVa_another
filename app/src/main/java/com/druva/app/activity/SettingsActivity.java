package com.druva.app.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.nextinnovation.pt.barcodescanner.R;


import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.view.View;
import android.content.Context;
import android.widget.Toast;
import android.content.DialogInterface.*;
import android.content.DialogInterface;
import java.net.InetAddress;
import android.text.Editable;
import java.net.*;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import android.os.Handler;
import android.os.Message;

public class SettingsActivity extends AppCompatActivity{

    databasehelper db;
    EditText t;
    Button cancel;
    Button ok;
    Button ret;
    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        t=(EditText)findViewById(R.id.pjText);
        ok=(Button)findViewById(R.id.ok);
//        ret=(Button)findViewById(R.id.get);
//        tv=(TextView)findViewById(R.id.show);
        Button button;
        db=new databasehelper(this);
        // Intent intent=new Intent(this,side.class);
        // startActivity(intent);
        //ping_ip();
        String s;
        if(db.getProduct()=="false"){
            t.setText("");
        }
        else {
            t.setText(db.getProduct());
        }
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  t.setText("Its working.")
                ping_ip(v);
            }
        });
 /*       ret.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                tv.setText(db.getProduct());
            }
        });*/
    }

    public void addProduct(){
        String result=db.getProduct();
        //db.onUpgrade(db,null,null);
        if(result=="false") {
            Product product = new Product();
            product.setIp_address(t.getText().toString());
            db.addProduct(product);
            t.setText(db.getProduct());
        }
        else{
            String s=t.getText().toString();
            db.updateProduct(s);
            t.setText(db.getProduct());
        }
        Context context = getApplicationContext();
        CharSequence text = "adding ip address";
        int duration = Toast.LENGTH_LONG;
        Toast toast=Toast.makeText(context,text,duration);
        toast.show();
    }

    //put this whole thing in seperate thread.
    public void ping_ip(View v) {
        final String ip=t.getText().toString();

        Runnable r=new Runnable() {
            @Override
            public void run() {
                Boolean lflag;
                synchronized (this) {
                    //write code to ping external ip address
                    //String ip = t.getText().toString();
                    InetAddress in;
                    in = null;
                    try {
                        in = InetAddress.getByName(ip);
                    } catch (Exception e) {
                        lflag = false;
                    }
                    try {
                        if (in.isReachable(1000)) {
                            lflag = true;
                        } else {
                            lflag = false;
                        }
                    } catch (Exception e) {
                        lflag = false;
                    }
                }
                final Boolean finalLflag = lflag;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Boolean temp;
                        if(finalLflag && (temp=!ip.equals(""))) {
                            addProduct();
                        }
                        else{
                            //create an alert window.
                            opendialog();
                            if(db.getProduct()=="false"){
                                t.setText("");
                            }
                            else {
                                t.setText(db.getProduct());
                            }
                            //tv.setText("no active host.");
                        }
                    }
                });
                // handler.sendEmptyMessage(0);
            }
        };
        Thread pjthread=new Thread(r);
        pjthread.start();
    }

    public void opendialog(){
        dialog dg=new dialog();
        dg.show(getSupportFragmentManager(),"error dialog");
    }

}
