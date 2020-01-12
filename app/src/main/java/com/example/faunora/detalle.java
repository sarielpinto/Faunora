package com.example.faunora;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.faunora.datos.CustomListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class detalle extends AppCompatActivity {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle);


        Bundle datos = this.getIntent().getExtras();
        String nombre_recuperado = datos.getString("nombre");

        editText=(TextView) findViewById(R.id.et_name);
        editText.setText(nombre_recuperado);



        //para listview
        listView=(ListView)findViewById(R.id.lview);
        consultadeidfauna("https://lamenting-twin.000webhostapp.com/faunora/saber_idfauna.php?nombre="+nombre_recuperado+"");
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

                    SharedPreferences prefs =
                            getSharedPreferences("MisPreferencias",Context.MODE_PRIVATE);

                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString("id_fauna", id);
                    editor.clear();
                    editor.commit();


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
            SharedPreferences prefs =
                    getSharedPreferences("MisPreferencias",Context.MODE_PRIVATE);

            String Id = prefs.getString("id_fauna", "noexiste");

            Toast.makeText(getApplicationContext(),"Hola"+Id,Toast.LENGTH_LONG).show();
            urladdress="https://lamenting-twin.000webhostapp.com/faunora/consultar_informacion.php?id_fauna=1";
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
            name=new String[ja.length()];
            email=new String[ja.length()];


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
