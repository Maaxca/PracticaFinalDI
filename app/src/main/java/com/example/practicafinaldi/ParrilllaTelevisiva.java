package com.example.practicafinaldi;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class ParrilllaTelevisiva extends AppCompatActivity {
    ProgressDialog progressDialog;
    static final String SERVIDOR="https://raw.githubusercontent.com/dracohe/CARLOS/master/guide_IPTV.xml";
    ListView lista;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parrillla_televisiva);
        lista=findViewById(R.id.listView);
        ParrilllaTelevisiva.DescargarXML descargarXML=new ParrilllaTelevisiva.DescargarXML();
        descargarXML.execute();
    }
    private class DescargarXML extends AsyncTask<String,Void,Void> {
        ArrayAdapter<String> adapter;
        @Override
        protected Void doInBackground(String... strings) {
            DocumentBuilderFactory dbf=DocumentBuilderFactory.newInstance();
            DocumentBuilder db= null;
            try {
                db = dbf.newDocumentBuilder();
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            }
            Document doc= null;
            try {
                doc = db.parse(new URL(SERVIDOR).openStream());
            } catch (IOException e) {
                e.printStackTrace();
            } catch (SAXException e) {
                e.printStackTrace();
            }
            Element raiz=doc.getDocumentElement();
            NodeList hijos=raiz.getChildNodes();


            List<String> list=new ArrayList<String>();
            for(int i=0;i<hijos.getLength();i++){
                Node nodo=hijos.item(i);

                if(nodo instanceof Element){
                    NodeList nietos=nodo.getChildNodes();

                    String dato="";
                    for(int j=0;j<nietos.getLength();j++){
                        dato+=" "+nietos.item(j).getTextContent();
                    }
                    list.add(dato);
                }
            }
            adapter=new ArrayAdapter<String>(getApplicationContext(),R.layout.support_simple_spinner_dropdown_item,list);
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPreExecute();
            lista.setAdapter(adapter);
            progressDialog.dismiss();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog=new ProgressDialog(ParrilllaTelevisiva.this);
            progressDialog.setTitle("Descargando datos...");
            progressDialog.setIndeterminate(true);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.show();
        }
        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
            progressDialog.setProgress(progressDialog.getProgress()+10);
        }
    }
}