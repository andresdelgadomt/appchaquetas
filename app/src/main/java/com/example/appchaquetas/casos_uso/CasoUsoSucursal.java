package com.example.appchaquetas.casos_uso;

import android.database.Cursor;

import com.example.appchaquetas.modelos.Producto;
import com.example.appchaquetas.modelos.Sucursal;

import java.util.ArrayList;

public class CasoUsoSucursal {
    public ArrayList<Sucursal> llenarlistaSucursal(Cursor cursor){
        ArrayList<Sucursal> list = new ArrayList<>();
        if(cursor.getCount() == 0){
            return list;
        }else{
            while (cursor.moveToNext()){
                Sucursal sucursal = new Sucursal(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getBlob(4)
                );
                list.add(sucursal);
            }
            return list;
        }
    }
}
