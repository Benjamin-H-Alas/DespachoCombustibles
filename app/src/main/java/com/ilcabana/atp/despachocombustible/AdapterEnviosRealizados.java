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
 * Created by Juan Jose on 23/12/2015.  //NO USAR ESTE ESTA FUERA DE USO
 */
public class AdapterEnviosRealizados {
    public SimpleAdapter adapterEnviosRealizados (Context ctx, String FILTRO, String IDTACO, String QuinId, String FechaDia, boolean countImporta){
        SQLiteDatabase db;
        DatabaseHandler_ dbhelper = new DatabaseHandler_(ctx);
        db = dbhelper.getReadableDatabase();

        String query = "SELECT "
                + "DISTINCT(a."+dbhelper.K_ENVR0_ID+"),  "
                + " a."+dbhelper.K_ENVR1_ENVIO	    +", "
                + " a."+dbhelper.K_ENVR2_CARGADORA	+", "
                + " a."+dbhelper.K_ENVR3_CARGADOR	+", "
                + " a."+dbhelper.K_ENVR4_NOMBRECARD	    +", "
                + " a."+dbhelper.K_ENVR5_FCORTA  	+", "
                + " a."+dbhelper.K_ENVR6_HCORTA 	+", "
                + " a."+dbhelper.K_ENVR7_FLLEGA 	+", "
                + " a."+dbhelper.K_ENVR8_HLLEGA     +", "
                + " a."+dbhelper.K_ENVR9_FCARGA	    +", "
                + " a."+dbhelper.K_ENVR10_HCARGA	+", "
                + " a."+dbhelper.K_ENVR11_FSALIDA	   +", "
                + " a."+dbhelper.K_ENVR12_HSALIDA	    +" , "
                + " a."+dbhelper.K_ENVR13_OBSERV 	     +", "
                + " a."+dbhelper.K_ENVR14_ULTIMOENV	    +"  "

                + " FROM   "+dbhelper.TABLE_ENVIOS_REALIZADOS+" AS a "
                + " WHERE "
               // + " CASE WHEN '"+QuinId+"' = '' THEN a."+dbhelper.K_ENVR15_QUINID+" = a."+dbhelper.K_ENVR15_QUINID+" ELSE a."+dbhelper.K_ENVR15_QUINID+" = '"+QuinId+"' END "
                //+ " AND CASE WHEN '"+FechaDia+"' = '' THEN a."+dbhelper.K_ENVR12_FECHADIA+" = a."+dbhelper.K_ENVR12_FECHADIA+" ELSE a."+dbhelper.K_ENVR12_FECHADIA+" = '"+FechaDia+"' END "
                + " AND CASE WHEN '"+IDTACO+"' = '' THEN a."+dbhelper.K_ENVR1_ENVIO+" = a."+dbhelper.K_ENVR1_ENVIO+" ELSE a."+dbhelper.K_ENVR1_ENVIO+" = '"+IDTACO+"' END "
                //+"  AND ( CASE WHEN '"+FILTRO+"' = '0' THEN a."+dbhelper.K_ENVR1_ENVCOD+" = a."+dbhelper.K_ENVR1_ENVCOD+" ELSE a."+dbhelper.K_ENVR1_ENVCOD+" LIKE '%"+FILTRO+"%' END "
                //+ " OR    CASE WHEN '"+FILTRO+"' = '0' THEN a."+dbhelper.K_ENVR2_ENVDESC+" = a."+dbhelper.K_ENVR2_ENVDESC+" ELSE a."+dbhelper.K_ENVR2_ENVDESC+" LIKE '%"+FILTRO+"%' END )"
                ;

        Log.i("QUERY", "QUERY: " + query);

        ArrayList<HashMap<String, String>> myListActivosOcaciones = new ArrayList<HashMap<String, String>>();
        String[] fromActivos = new String[] {"tvr_num_taco","tvr_codclie","tvr_placa","tvr_codmotor2","tvr_nomlote2", "tvr_nommotor2", "imgv_candado","tv_llave"};
        int[] toActivos = new int[] {R.id.tvr_num_taco,R.id.tv_llave,R.id.tvr_nomcargador,R.id.tvr_hcorta,R.id.tvr_placa, R.id.tvr_hcarga, R.id.tvr_hsalida, R.id.imgv_candado,R.id.tv_llave };

        Cursor cFrentes = db.rawQuery(query, null);

        if(cFrentes.moveToFirst()){
            do {
                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put("tvr_num_taco"     , "ENVIO: " + cFrentes.getString(1));
                    map.put("tvr_codclie"    , "CLOTE: "+cFrentes.getString(2));
                    map.put("tvr_placa"      , "PLACA: "+cFrentes.getString(3));
                    map.put("tvr_codmotor2"   , "CMOT: "+cFrentes.getString(4));
                    map.put("tvr_nomlote2"    , "LOTE: "+cFrentes.getString(5));
                    map.put("tvr_nommotor2"     , "LOTE: "+cFrentes.getString(6));
                    map.put("imgv_candado"         , "PLACA RASTRA: "+cFrentes.getString(7));

                    map.put("tv_llave", "LLAVE: " + cFrentes.getString(8));
                    map.put("key_llave", cFrentes.getString(8));

                    if (Integer.parseInt(cFrentes.getString(8).toString()) > 0) {
                        map.put("imgv_candado", "" + R.drawable.close);
                    } else {
                        map.put("imgv_candado", "" + R.drawable.open);
                    }

                    map.put("K_ENVR0_ID", cFrentes.getString(0));



                    myListActivosOcaciones.add(map);

            }while(cFrentes.moveToNext());
        }

        cFrentes.close();
        db.close();

        SimpleAdapter adapterlistEmpleados = new SimpleAdapter(ctx, myListActivosOcaciones,R.layout.row_lista_envios_realizados, fromActivos, toActivos);

        return adapterlistEmpleados;
    }
}