package com.example.faunora;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.SharedPreferencesCompat;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.faunora.datos.CustomListView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class detalle extends AppCompatActivity {
    //prueba

    String urladdress;
    String[] name;
    String[] email;

    ListView listView;
    BufferedInputStream is;
    String line=null;
    String result=null;

    TextView editText;
    String id,ID2;
    RequestQueue requestQueue;
    ImageView image;
    String nombre_recuperado;
    String fauna;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle);
        image=(ImageView)findViewById(R.id.iv_avatar);

        Bundle datos = this.getIntent().getExtras();
         nombre_recuperado = datos.getString("nombre");
         fauna=datos.getString("fauna");

        editText=(TextView) findViewById(R.id.et_name);
        editText.setText(nombre_recuperado);



        //para listview
        listView=(ListView)findViewById(R.id.lview);
        if(fauna.equals("1")) {

        }else if(fauna.equals("2")){
            consultadeidfauna("https://lamenting-twin.000webhostapp.com/faunora/saber_idfauna.php?nombre=" + nombre_recuperado + "");
        }

        StrictMode.setThreadPolicy((new StrictMode.ThreadPolicy.Builder().permitNetwork().build()));
        collectData();
        CustomListView customListView=new CustomListView(this,name,email);
        listView.setAdapter(customListView);


    }



    public void consultadeidfauna(String URL){
        JsonArrayRequest jsonArrayRequest=new JsonArrayRequest(URL, response -> {
            JSONObject jsonObject = null;
            for (int i = 0; i <response.length(); i++) {
                try {
                    jsonObject=response.getJSONObject(i);
                    id=jsonObject.getString("id_fauna");
                    consultadefotosfauna("https://lamenting-twin.000webhostapp.com/faunora/obtener_fotos.php?id_fauna="+id+"");


                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }

        }, error -> Toast.makeText(getApplicationContext(), "NO esta entrando en el if", Toast.LENGTH_LONG).show());
        requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);
    }


    public void consultadefotosfauna(String Url){
        JsonArrayRequest jsonArrayRequest=new JsonArrayRequest(Url, response -> {
            JSONObject jsonObject = null;
            for (int i = 0; i <response.length(); i++) {
                try {
                    jsonObject=response.getJSONObject(i);
                    String url=jsonObject.getString("ruta_imagen");

                    //para imprimir la foto
                    Picasso.with(this)
                            .load(url)
                            .error(R.mipmap.ic_launcher)
                            .fit()
                            .centerCrop()
                            .into(image);

                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }

        }, error -> Toast.makeText(getApplicationContext(), "NO esta entrando en el if", Toast.LENGTH_LONG).show());
        requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);
    }



    private void collectData()
    {
//Connection
        try{

            urladdress="http://faunora.lighthousecode.com/consultar_informacion.php?nombre=jaguar";

            URL url=new URL(urladdress);
            HttpURLConnection con=(HttpURLConnection)url.openConnection();
            con.setRequestMethod("GET");
            is=new BufferedInputStream(con.getInputStream());

        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        //content
        try{
            BufferedReader br=new BufferedReader(new InputStreamReader(is));
            StringBuilder sb=new StringBuilder();
            while ((line=br.readLine())!=null){
                sb.append(line+"\n");
            }
            is.close();
            result=sb.toString();

        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

//JSON
        try{
            JSONArray ja=new JSONArray(result);


            JSONObject jo=null;
            name = new String[ja.length()];
            email = new String[ja.length()];

            for(int i=0;i<=ja.length();i++){
                jo=ja.getJSONObject(i);
                name[i]=jo.getString("campos");
                email[i]=jo.getString("informacion");
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

}
