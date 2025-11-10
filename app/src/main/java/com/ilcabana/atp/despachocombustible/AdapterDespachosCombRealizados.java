package com.ilcabana.atp.despachocombustible;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.SimpleAdapter;

import com.ilcabana.atp.database.DatabaseHandler_;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by juan.reyes on 27/09/2017.
 */
public class AdapterDespachosCombRealizados {
    public SimpleAdapter adapterDespachosCombRealizados (Context ctx, String FILTRO){
        SQLiteDatabase db;
        DatabaseHandler_ dbhelper = new DatabaseHandler_(ctx);
        db = dbhelper.getReadableDatabase();

        String query = " SELECT "
                + dbhelper.K_DESP0_ID          		+ " , "
                + dbhelper.K_DESP1_ENVIO          + " , "
                + dbhelper.K_DESP4_NOMBRECARD       + " ,  "
                + dbhelper.K_DESP8_HLLEGA              + " , "
                + dbhelper.K_DESP10_HCARGA       + " , "
                + dbhelper.K_DESP12_HSALIDA     + " , "
                + dbhelper.K_DESP15_LLAVE 	           + " , "
                + dbhelper.K_DESP17_NOENVIO	           + " , "
                + dbhelper.K_DESP19_PLACA            + "  "

                +" FROM  " +dbhelper.TABLE_DESPACHO_ENVIOS	+" a "
                +" WHERE "
                +" ( CASE WHEN '"+FILTRO+"' = '0' THEN a."+dbhelper.K_DESP1_ENVIO+" = a."+dbhelper.K_DESP1_ENVIO+" ELSE a."+dbhelper.K_DESP1_ENVIO+" LIKE '%"+FILTRO+"%' END) "
                +" ORDER BY CAST(a."+dbhelper.K_DESP15_LLAVE+" as INT) DESC "
                ;

        Log.i("QUERY", "QUERY: " + query);

        ArrayList<HashMap<String, String>> myListActivosOcaciones = new ArrayList<HashMap<String, String>>();


        String[] fromActivos = new String[] {"tvr_num_taco","tvr_nomcargador","tvr_hllega","tvr_hcarga","tvr_hsalida","tv_llave","imgv_candado","tvr_Envio","tvr_placa"};
        int[] toActivos = new int[] {R.id.tvr_num_taco,R.id.tvr_nomcargador,R.id.tvr_hllega,R.id.tvr_hcarga,R.id.tvr_hsalida,R.id.tv_llave, R.id.imgv_candado,R.id.tvr_Envio,R.id.tvr_placa};

        Cursor cFrentes = db.rawQuery(query, null);

        if(cFrentes.moveToFirst()){
            do {
                HashMap<String, String> map = new HashMap<String, String>();

                map.put("tvr_num_taco"          , "TACO: "+cFrentes.getString(1));
                map.put("tvr_nomcargador"          , "CARG: "+cFrentes.getString(2));
                map.put("tvr_hllega"            , "LLEGO: "+cFrentes.getString(3));
                map.put("tvr_hcarga"   , "CARGO: "+cFrentes.getString(4));
                map.put("tvr_hsalida"   , "SALIO: "+cFrentes.getString(5));
                map.put("tv_llave", "LLAVE: " + cFrentes.getString(6));
                map.put("tvr_Envio", "NO ENVIO: " + cFrentes.getString(7));
                map.put("tvr_placa", "PLACA: " + cFrentes.getString(8));


                if (Integer.parseInt(cFrentes.getString(6).toString()) > 0) {
                    map.put("imgv_candado", "" + R.drawable.close);
                } else {
                    map.put("imgv_candado", "" + R.drawable.open);
                }

                map.put("K_DESP0_ID", cFrentes.getString(0));
                map.put("K_DESP15_LLAVE", cFrentes.getString(6));

                myListActivosOcaciones.add(map);

            }while(cFrentes.moveToNext());
        }

        cFrentes.close();
        db.close();

        SimpleAdapter adapterlistEmpleados = new SimpleAdapter(ctx, myListActivosOcaciones,R.layout.row_lista_despachos_realizados, fromActivos, toActivos);

        return adapterlistEmpleados;
    }
}
