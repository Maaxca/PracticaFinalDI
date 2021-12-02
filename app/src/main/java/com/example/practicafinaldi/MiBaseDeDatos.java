package com.example.practicafinaldi;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class MiBaseDeDatos extends AppCompatActivity {

    Button buttonCSV,buttonJSON,buttonXML,buttonModificar,buttonInsertar,buttonBorrar;
    EditText txtNombre,txtAño,txtCadena,txtTemporadas,txtID;
    ListView lista;
    ProgressDialog progressDialog;
    static final String SERVIDOR="http://192.168.3.76";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mi_base_de_datos);
        buttonCSV=findViewById(R.id.buttonCSV);
        buttonJSON=findViewById(R.id.buttonJSON);
        buttonXML=findViewById(R.id.buttonXML);
        buttonModificar=findViewById(R.id.buttonModificar);
        buttonInsertar=findViewById(R.id.buttonInsertar);
        buttonBorrar=findViewById(R.id.buttonBorrar);
        txtNombre=findViewById(R.id.txtNombre);
        txtAño=findViewById(R.id.txtAño);
        txtCadena=findViewById(R.id.txtCadena);
        txtTemporadas=findViewById(R.id.txtTemporadas);
        txtID=findViewById(R.id.txtID);
        lista=findViewById(R.id.listView);

        buttonJSON.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DescargarJSON descargarJSON=new DescargarJSON();
                descargarJSON.execute("/PracticaDI/listadoJSON2.php");
            }
        });
        buttonCSV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DescargarCSV descargarCSV=new DescargarCSV();
                descargarCSV.execute("/PracticaDI/listadoCSV2.php");
            }
        });
        buttonXML.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DescargarXML descargarXML=new DescargarXML();
                descargarXML.execute("/PracticaDI/listadoXML2.php");
            }
        });
    }


    private class DescargarJSON extends AsyncTask<String,Void,Void> {

        String todo="";

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
            progressDialog.setProgress(progressDialog.getProgress()+10);
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog=new ProgressDialog(MiBaseDeDatos.this);
            progressDialog.setTitle("Descargando datos...");
            progressDialog.setIndeterminate(true);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(String... strings) {
            String script=strings[0];
            URL url;
            HttpURLConnection httpURLConnection;

            try {
                url=new URL(SERVIDOR+script);
                httpURLConnection= (HttpURLConnection) url.openConnection();

                if(httpURLConnection.getResponseCode()==HttpURLConnection.HTTP_OK){
                    InputStream inputStream=httpURLConnection.getInputStream();
                    BufferedReader br=new BufferedReader(new InputStreamReader(inputStream));

                    String linea="";
                    while((linea=br.readLine())!=null){
                        todo+=linea+"\n";
                        Thread.sleep(100);
                        publishProgress();

                    }
                    br.close();
                    inputStream.close();

                }
                else{
                    Toast.makeText(MiBaseDeDatos.this, "No me pude conectar a la nube", Toast.LENGTH_SHORT).show();
                }


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            ArrayAdapter<String> adapter;
            List<String> list=new ArrayList<String>();
            JsonParser parser=new JsonParser();
            JsonArray jsonArray=parser.parse(todo).getAsJsonArray();

            for(JsonElement elemento : jsonArray){
                JsonObject objeto = elemento.getAsJsonObject();
                String dato="ID: "+objeto.get("ID").getAsString();
                dato+=" NOMBRE: "+objeto.get("Nombre").getAsString();
                dato+=" AÑO ESTRENO: "+objeto.get("Año de estreno").getAsString();
                dato+=" CADENA: "+objeto.get("Cadena").getAsString();
                dato+=" NUMERO TEMPORADAS: "+objeto.get("Numero Temporadas").getAsString();

                list.add(dato);
            }
            adapter=new ArrayAdapter<String>(getApplicationContext(),R.layout.support_simple_spinner_dropdown_item,list);
            lista.setAdapter(adapter);
            progressDialog.dismiss();
        }
    }
    private class DescargarCSV extends AsyncTask<String,Void,Void>{
        String todo="";
        @Override
        protected Void doInBackground(String... strings) {
            String script=strings[0];
            URL url;
            HttpURLConnection httpURLConnection;

            try {
                url=new URL(SERVIDOR+script);
                httpURLConnection= (HttpURLConnection) url.openConnection();

                if(httpURLConnection.getResponseCode()==HttpURLConnection.HTTP_OK){
                    InputStream inputStream=httpURLConnection.getInputStream();
                    BufferedReader br=new BufferedReader(new InputStreamReader(inputStream));

                    String linea="";
                    while((linea=br.readLine())!=null){
                        todo+=linea+"\n";
                        Thread.sleep(100);
                        publishProgress();

                    }
                    br.close();
                    inputStream.close();

                }
                else{
                    Toast.makeText(MiBaseDeDatos.this, "No me pude conectar a la nube", Toast.LENGTH_SHORT).show();
                }


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
            progressDialog.setProgress(progressDialog.getProgress()+10);
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            ArrayAdapter<String>adapter;
            List<String>list=new ArrayList<String>();
            String[]lineas=todo.split("\n");
            for (String linea:lineas){
                String[]campos=linea.split(";");
                String dato="ID "+campos[0];
                    dato+=" NOMBRE: "+campos[1];
                dato+=" AÑO ESTRENO: "+campos[2];
                dato+=" CADENA: "+campos[3];
                dato+=" NUMERO TEMPORADAS: "+campos[4];
                list.add(dato);
            }
            adapter=new ArrayAdapter<String>(getApplicationContext(),R.layout.support_simple_spinner_dropdown_item,list);
            lista.setAdapter(adapter);
            progressDialog.dismiss();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog=new ProgressDialog(MiBaseDeDatos.this);
            progressDialog.setTitle("Descargando datos...");
            progressDialog.setIndeterminate(true);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.show();
        }
    }



    private class DescargarXML extends AsyncTask<String,Void,Void>{
        ArrayAdapter<String>adapter;
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
                doc = db.parse(new URL(SERVIDOR+strings[0]).openStream());
            } catch (IOException e) {
                e.printStackTrace();
            } catch (SAXException e) {
                e.printStackTrace();
            }
            Element raiz=doc.getDocumentElement();
            NodeList hijos=raiz.getChildNodes();


            List<String>list=new ArrayList<String>();
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
            progressDialog=new ProgressDialog(MiBaseDeDatos.this);
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