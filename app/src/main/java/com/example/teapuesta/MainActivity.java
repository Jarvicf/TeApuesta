package com.example.teapuesta;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    private  static  String ApiKeys = "8040dcee0dab70e5dc82007fd8b04266de3138d127f7379e2e7780b2dc0627d9";
    private  static  String JSON_URL = "  http://appestadisticas.iijavaii.com/apuestas/Fixtures.php?APIkey="+ApiKeys;
    String rua="1";

    ProgressDialog progressDialog;

    ArrayList event_key = new ArrayList();
    ArrayList event_date = new ArrayList();
    ArrayList event_time  = new ArrayList();
    ArrayList event_home_team  = new ArrayList();
    ArrayList home_team_key = new ArrayList();
    ArrayList event_away_team = new ArrayList();
    ArrayList away_team_key = new ArrayList();
    ArrayList event_final_result = new ArrayList();
    ArrayList event_status = new ArrayList();
    ArrayList country_name = new ArrayList();
    ArrayList league_name = new ArrayList();
    ArrayList league_key = new ArrayList();
    ArrayList event_stadium = new ArrayList();
    ArrayList home_team_logo = new ArrayList();
    ArrayList away_team_logo = new ArrayList();
    ArrayList league_logo = new ArrayList();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Traer_Juegos();
        ObtenerDatos obtenerDatos = new ObtenerDatos();
        obtenerDatos.execute();

    }

    private void Traer_Juegos() {

        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setMessage("Buscando");
        progressDialog.show();
    }

    //Consultamos los datos en la Api
    public class ObtenerDatos extends AsyncTask<String,String,String> {

        @Override
        protected String doInBackground(String... strings) {

            String current = "";

            try {
                URL url;
                HttpURLConnection urlConnection = null;

                try {
                    url = new URL(JSON_URL);
                    urlConnection = (HttpURLConnection) url.openConnection();
                    InputStream in = urlConnection.getInputStream();
                    InputStreamReader isr = new InputStreamReader(in);

                    int data = isr.read();
                    while (data != -1) {
                        current += (char) data;
                        data = isr.read();
                    }


                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (urlConnection != null) {
                        urlConnection.disconnect();
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return current;
        }

        @Override
        protected void onPostExecute(String s) {



            try {


                    JSONObject jsonObject = new JSONObject(s);
                    JSONArray jsonArray = jsonObject.getJSONArray("result");


                    //Recorro Json y obtengo los datos y los guardo en los ArrayList
                    for (int i = 0; i < jsonArray.length(); i++) {

                        event_key.add(jsonArray.getJSONObject(i).getString("event_key"));
                        event_date.add(jsonArray.getJSONObject(i).getString("event_date"));
                        event_time.add(jsonArray.getJSONObject(i).getString("event_time"));
                        event_home_team.add(jsonArray.getJSONObject(i).getString("event_home_team"));
                        event_away_team.add(jsonArray.getJSONObject(i).getString("event_away_team"));


                        //cada dato que extraemos lo mostramos atravez del listView
                        //Lista_libros.setAdapter(new equipoAdater(getApplicationContext()));

                    /*JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                    String titulo = jsonObject.getString("title");
                    HashMap<String, String> libros = new HashMap<>();
                    libros.put("title",titulo);
                    listaLibros.add(libros);*/

                    }

                if (rua.equals("1")) {
                    JSONObject jsonObjectw = new JSONObject(s);

                }else {

                }




                progressDialog.dismiss();

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }


}