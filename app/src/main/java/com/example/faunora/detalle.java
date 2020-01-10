package com.example.faunora;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class detalle extends AppCompatActivity {

    TextView editText;
    String id;
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle);

        Bundle datos = this.getIntent().getExtras();
        String nombre_recuperado = datos.getString("nombre");

        editText=(TextView) findViewById(R.id.et_name);
        editText.setText(nombre_recuperado);

        consultadeidfauna("https://lamenting-twin.000webhostapp.com/faunora/saber_idfauna.php?nombre=Jaguar");
    }
    public void consultadeidfauna(String URL){
        JsonArrayRequest jsonArrayRequest=new JsonArrayRequest(URL, response -> {
            JSONObject jsonObject = null;
            for (int i = 0; i <response.length(); i++) {
                try {
                    jsonObject=response.getJSONObject(i);
                    id=jsonObject.getString("id_fauna");

                    Toast.makeText(getApplicationContext(),"Hola"+id,Toast.LENGTH_LONG).show();

                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }

        }, error -> Toast.makeText(getApplicationContext(), "NO esta entrando en el if", Toast.LENGTH_LONG).show());
        requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);
    }
}
