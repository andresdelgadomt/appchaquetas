package com.example.appchaquetas.ui.sucursales;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.appchaquetas.BuildConfig;
import com.example.appchaquetas.R;
import com.example.appchaquetas.adaptadores.SucursalAdapter;
import com.example.appchaquetas.agregar.agregarActivity;
import com.example.appchaquetas.casos_uso.CasoUsoSucursal;
import com.example.appchaquetas.datos.DBHelper;
import com.example.appchaquetas.modelos.Sucursal;

import org.osmdroid.config.Configuration;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.ItemizedOverlayWithFocus;
import org.osmdroid.views.overlay.OverlayItem;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.util.ArrayList;


public class SucursalesFragment extends Fragment {

    private String TABLE_NAME = "SUCURSALES";
    private CasoUsoSucursal casoUsoSucursales;
    private GridView gridView;
    private DBHelper dbHelper;
    private ArrayList<Sucursal> sucursal;
    private MapView myOpenMapView;
    private MapController myMapController;

    GeoPoint Bogota, SanAndresito;



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_sucursales, container,false);

        myOpenMapView = (MapView) root.findViewById(R.id.openmapview);
        /* ---- necesitamos establecer el valor del agente de usuario en la configuración ------- */
        Configuration.getInstance().setUserAgentValue(BuildConfig.APPLICATION_ID);

        /*   punto de geolocalizacion de ejemplo */
        Bogota = new GeoPoint(4.6351, -74.0703);
        SanAndresito = new GeoPoint(4.615462204303617, -74.10092216250449);

        myOpenMapView.setBuiltInZoomControls(true);

        myMapController = (MapController) myOpenMapView.getController();
        myMapController.setCenter(Bogota);
        myMapController.setZoom(6);

        myOpenMapView.setMultiTouchControls(true);

        /* -------------------------------------------------------------------------------------------------- */
        final MyLocationNewOverlay myLocationoverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(getContext()), myOpenMapView);
        myOpenMapView.getOverlays().add(myLocationoverlay); //No añadir si no quieres una marca
        myLocationoverlay.enableMyLocation();

        myLocationoverlay.runOnFirstFix(new Runnable() {
            public void run() {
                myMapController.animateTo(myLocationoverlay.getMyLocation());
            }
        });
        /* -------------------------------------------------------------------------------------------------- */

        /* MARCAS EN EL MAPA */

        ArrayList<OverlayItem> puntos = new ArrayList<OverlayItem>();
        puntos.add(new OverlayItem("Bogota", "Capital de Colombia", Bogota));
        puntos.add(new OverlayItem("SanAndresito", "Tienda 1", SanAndresito));

        ItemizedIconOverlay.OnItemGestureListener<OverlayItem> tap = new ItemizedIconOverlay.OnItemGestureListener<OverlayItem>() {
            @Override
            public boolean onItemLongPress(int arg0, OverlayItem arg1) {
                return false;
            }
            @Override
            public boolean onItemSingleTapUp(int index, OverlayItem item) {
                return true;
            }
        };

        ItemizedOverlayWithFocus<OverlayItem> capa = new ItemizedOverlayWithFocus<OverlayItem>(getContext(), puntos, tap);
        capa.setFocusItemsOnTap(true);
        myOpenMapView.getOverlays().add(capa);

        try{
            casoUsoSucursales = new CasoUsoSucursal();
            dbHelper = new DBHelper(getContext());
            Cursor cursor = dbHelper.getData(TABLE_NAME);
            sucursal = casoUsoSucursales.llenarlistaSucursal(cursor);
            gridView = (GridView) root.findViewById(R.id.gridSucursales);
            SucursalAdapter sucursaladapter = new SucursalAdapter(root.getContext(), sucursal);
            gridView.setAdapter(sucursaladapter);
        }catch (Exception e){
            Toast.makeText(getContext(), e.toString(), Toast.LENGTH_LONG).show();
        }

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_add:
                Intent intent = new Intent(getContext(), agregarActivity.class);
                intent.putExtra("name","SUCURSALES");
                getActivity().startActivity(intent);
                //Toast.makeText(getContext(), "Hola Productos", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}