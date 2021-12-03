package com.example.practicafinaldi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
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
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class MiBaseDeDatos extends AppCompatActivity {

    Button buttonCSV,buttonJSON,buttonXML,buttonModificar,buttonInsertar,buttonBorrar;
    EditText txtNombre,txtAño,txtCadena,txtTemporadas,txtID;
    ListView lista;
    ProgressDialog progressDialog;
    ArrayList<String>Nombre=new ArrayList<>();
    ArrayList<String>Año=new ArrayList<>();
    ArrayList<String>Cadenas=new ArrayList<>();
    ArrayList<String>Temporadas=new ArrayList<>();
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
        buttonInsertar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Insertar insertar=new Insertar();
                insertar.execute();
            }
        });
        buttonModificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Modificar modificar=new Modificar();
                modificar.execute();
            }
        });
        buttonBorrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Borrar borrar=new Borrar();
                borrar.execute();
            }
        });
    }


    private class Insertar extends AsyncTask<String,Void,Void>{
        @Override
        protected Void doInBackground(String... strings) {
            try {
            String script=SERVIDOR+"/PracticaDI/insertar.php";
            URLConnection conexion=new URL(script).openConnection();

            conexion.setDoOutput(true);

            PrintStream ps=new PrintStream(conexion.getOutputStream());

            ps.print("nombre="+txtNombre.getText().toString());
            ps.print("&ano="+txtAño.getText().toString());
            ps.print("&cadena="+txtCadena.getText().toString());
            ps.print("&temporadas="+txtTemporadas.getText().toString());

                InputStream inputStream=conexion.getInputStream();
                BufferedReader br=new BufferedReader(new InputStreamReader(inputStream,"UTF-8"));
                String salida="";
                String linea="";
                while((linea=br.readLine())!=null){
                    salida+=linea+"\n";
                }
                br.close();
                ps.close();
                System.out.println(salida);

        }catch (MalformedURLException ex)
        {
          Logger.getLogger(MiBaseDeDatos.class.getName()).log(Level.SEVERE, null, ex);
        }catch (IOException e) {
                Logger.getLogger(MiBaseDeDatos.class.getName()).log(Level.SEVERE, null, e);
        }
            return null;
        }

    }
    private class Borrar extends AsyncTask<String,Void,Void>{

        @Override
        protected Void doInBackground(String... strings) {
            try {
            String id= null;

                id = URLEncoder.encode(txtID.getText().toString(), "UTF-8");
            String script=SERVIDOR+"/PracticaDI/borrar.php?id="+id;
            URLConnection conexion= null;
                conexion = new URL(script).openConnection();
                conexion.connect();

            InputStream inputStream=conexion.getInputStream();
            BufferedReader br=new BufferedReader(new InputStreamReader(inputStream,"UTF-8"));
            String salida="";
            String linea="";
            while((linea=br.readLine())!=null){
                salida+=linea+"\n";
            }
            br.close();
            System.out.println(salida);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
    private class Modificar extends AsyncTask<String,Void,Void>{

        @Override
        protected Void doInBackground(String... strings) {
            try {
                String id=URLEncoder.encode(txtID.getText().toString(), "UTF-8");
                String nombre=URLEncoder.encode(txtNombre.getText().toString(), "UTF-8");
                String ano=URLEncoder.encode(txtAño.getText().toString(), "UTF-8");
                String cadena=URLEncoder.encode(txtCadena.getText().toString(), "UTF-8");
                String temporadas=URLEncoder.encode(txtTemporadas.getText().toString(), "UTF-8");
                String script=SERVIDOR+"/PracticaDI/modificar.php?id="+id+"&nombre="+nombre+"&ano="+ano+"&cadena="+cadena+"&temporadas="+temporadas;
                URLConnection conexion=new URL(script).openConnection();
                conexion.connect();

                InputStream inputStream=conexion.getInputStream();
                BufferedReader br=new BufferedReader(new InputStreamReader(inputStream,"UTF-8"));
                String salida="";
                String linea="";
                while((linea=br.readLine())!=null){
                    salida+=linea+"\n";
                }
                br.close();
                System.out.println(salida);
            } catch (MalformedURLException ex) {
            } catch (IOException ex) {
            }
            return null;
        }
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
                Nombre.removeAll(Nombre);
                Nombre.add(campos[1]);
                Año.add(campos[2]);
                Año.removeAll(Año);
                Cadenas.add(campos[3]);
                Cadenas.removeAll(Cadenas);
                Temporadas.add(campos[4]);
                Temporadas.removeAll(Temporadas);
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
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

        menu.add(0, v.getId(),0,"Mostrar");
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int Posi = info.position;

        if (item.getTitle()=="Mostrar"){
                    AlertDialog.Builder builder=new AlertDialog.Builder(this);
                    builder.setTitle("Datos de la Serie");
                    builder.setMessage("ID: "+(Posi+1)+
                            "\n Nombre: "+Nombre.get(Posi)+
                            "\n Año de Estreno: "+Año.get(Posi)+
                            "\n Cadena: "+Cadenas.get(Posi)+
                            "\n Numero Temporadas: "+Temporadas.get(Posi));
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    builder.show();
        }
        return true;
    }
}