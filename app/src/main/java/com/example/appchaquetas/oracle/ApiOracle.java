package com.example.appchaquetas.oracle;

import android.content.Context;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.appchaquetas.adaptadores.SucursalAdapter;
import com.example.appchaquetas.modelos.Sucursal;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


public class ApiOracle {

    //static List<String> listaDatos;

    private SucursalAdapter adaptador;

    private RequestQueue queue;
    private String urlSucursales = "https://ga6c1640681b7b6-db202112192236.adb.sa-santiago-1.oraclecloudapps.com/ords/admin/chaquetas/sucursales";
    private String urlSucursalesId ="https://ga6c1640681b7b6-db202112192236.adb.sa-santiago-1.oraclecloudapps.com/ords/admin/chaquetas/Sucursal/:id";

    private Context context;

    public ApiOracle(Context context) {
        this.context = context;
        this.queue = Volley.newRequestQueue(context);
    }

    public void create(String [] data){
        JSONObject json = new JSONObject();
        try {
            json.put("nombre",data[0]);
            json.put("descripcion",data[1]);
            json.put("direccion",data[2]);
        }catch (JSONException e){
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, urlSucursales, json, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        queue.add(jsonObjectRequest);
    }

    public void update(int id,String [] data){
        JSONObject json = new JSONObject();
        try {
            json.put("nombre",data[0]);
            json.put("descripcion",data[1]);
            json.put("direccion",data[2]);
        }catch (JSONException e){
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, urlSucursalesId+String.valueOf(id), json, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        queue.add(jsonObjectRequest);
    }

    public void delete(int id){

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.DELETE, urlSucursalesId+String.valueOf(id), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        queue.add(jsonObjectRequest);
    }






    public void readAll(RecyclerView contenedor){
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, urlSucursales, null, new Response.Listener<JSONObject>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(JSONObject response) {
                List<Sucursal> ListAll = new ArrayList<Sucursal>();
                JSONArray data = null;
                try {
                    data = response.getJSONArray("items");
                    for(int i=0;i<data.length();i++){
                        JSONObject newData = data.getJSONObject(i);
                        String id = newData.getString("id");
                        String nombre = newData.getString("nombre");
                        String direccion = newData.getString("direccion");
                        String descripcion = newData.getString("telefono");
                        String favorito = newData.getString("favoritos");
                        String foto = newData.getString("foto");
                        byte[] byteArray = stringToByte(foto);
                        Sucursal sucursal = new Sucursal(id,nombre,descripcion,direccion,byteArray);
                        ListAll.add(sucursal);
                    }

                    adaptador = new SucursalAdapter(ListAll);
                    contenedor.setAdapter(adaptador);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        queue.add(jsonObjectRequest);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public byte[] stringToByte(String string){
        byte[] result = java.util.Base64.getDecoder().decode(string);
        return result;
    }


}
