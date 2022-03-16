package com.example.teapuesta;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.signature.StringSignature;

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

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity  {


    private  static  String ApiKeys = "8040dcee0dab70e5dc82007fd8b04266de3138d127f7379e2e7780b2dc0627d9";
    private  static  String JSON_URL = "https://apiv3.apifootball.com/?action=get_countries&APIkey="+ApiKeys;
    private  static  String JSON_URL2 = "https://apiv3.apifootball.com/?action=get_events&APIkey=8040dcee0dab70e5dc82007fd8b04266de3138d127f7379e2e7780b2dc0627d9&from=2022-03-16&to=2022-03-18";
    String rua="1";

    ProgressDialog progressDialog;

    ArrayList country_id = new ArrayList();
    ArrayList country_name = new ArrayList();
    ArrayList country_logo  = new ArrayList();

    ArrayList match_id  = new ArrayList();
    //ArrayList country_name = new ArrayList();
    ArrayList league_id = new ArrayList();
    ArrayList league_name = new ArrayList();
    ArrayList match_date = new ArrayList();
    ArrayList match_time = new ArrayList();
    ArrayList match_hometeam_name = new ArrayList();
    ArrayList match_awayteam_name = new ArrayList();
    ArrayList team_away_badge = new ArrayList();
    ArrayList team_home_badge = new ArrayList();

    ArrayList away_team_logo = new ArrayList();
    ArrayList league_logo = new ArrayList();

    Button ir;
    ListView paises;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ir= (Button) findViewById(R.id.btn_ir);
        paises=(ListView)findViewById(R.id.lst_Ligas);


        ir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Traer_Juegos();
                ObtenerDatos obtenerDatos = new ObtenerDatos();
                obtenerDatos.execute();
            }
        });


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
                    url = new URL(JSON_URL2);
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
        protected void onPostExecute(String result) {

            JSONArray ja = null;

            try {
                ja = new JSONArray(result);

                for (int i=0;i<ja.length();i++){

                    league_id.add(ja.getJSONObject(i).getString("league_id"));
                    league_name.add(ja.getJSONObject(i).getString("league_name"));
                    match_date.add(ja.getJSONObject(i).getString("match_date"));
                    match_time.add(ja.getJSONObject(i).getString("match_time"));
                    match_hometeam_name.add(ja.getJSONObject(i).getString("match_hometeam_name"));
                    match_awayteam_name.add(ja.getJSONObject(i).getString("match_awayteam_name"));

                    team_away_badge.add(ja.getJSONObject(i).getString("team_away_badge"));
                    team_home_badge.add(ja.getJSONObject(i).getString("team_home_badge"));


                    paises.setAdapter(new ImageAdater(getApplicationContext()));


                }

                progressDialog.dismiss();

            } catch (JSONException e) {
                Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }

    }

    private class ImageAdater extends BaseAdapter {



        Context ctx;
        LayoutInflater layoutInflater;
        CircleImageView img_local,img_visitante;
        TextView liga,local,visitante,hora;



        public ImageAdater(Context applicationContext){

            this.ctx=applicationContext;
            layoutInflater=(LayoutInflater)ctx.getSystemService(LAYOUT_INFLATER_SERVICE);

        }

        @Override
        public int getCount() {

            return country_id.size();
        }

        @Override
        public Object getItem(int position) {
            //Toast.makeText(Fichajes.this, "Usuario "+position, Toast.LENGTH_SHORT).show();
            return position;
        }

        @Override
        public long getItemId(int position) {
            //Toast.makeText(Fichajes.this, "Usuario selecionado"+position, Toast.LENGTH_SHORT).show();
            return position;

        }

        @Override
        public View getView(int pos, View convertView, ViewGroup parent) {

            ViewGroup viewGroup = (ViewGroup)layoutInflater.inflate(R.layout.partidos,null);


            img_local =(CircleImageView)viewGroup.findViewById(R.id.img_local);
            img_visitante =(CircleImageView)viewGroup.findViewById(R.id.img_visitante);

            liga=(TextView) viewGroup.findViewById(R.id.lbl_liga);
            local=(TextView) viewGroup.findViewById(R.id.lbl_local);
            visitante=(TextView) viewGroup.findViewById(R.id.lbl_visitante);

            liga.setText(league_name.get(pos).toString());
            local.setText(match_hometeam_name.get(pos).toString());
            visitante.setText(match_awayteam_name.get(pos).toString());


                String posi = (team_away_badge.get(pos).toString());
                String urlfinal=country_logo.get(pos).toString();
                Glide.with(img_local.getContext())
                        .load(urlfinal)
                        .centerCrop()
                        .dontAnimate()
                        .signature(new StringSignature(urlfinal))
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .into(img_local);

            String posi2 = (team_home_badge.get(pos).toString());
            String urlfinal2=country_logo.get(pos).toString();
            Glide.with(img_visitante.getContext())
                    .load(urlfinal)
                    .centerCrop()
                    .dontAnimate()
                    .signature(new StringSignature(urlfinal))
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(img_visitante);



            return viewGroup;
        }

    }


}