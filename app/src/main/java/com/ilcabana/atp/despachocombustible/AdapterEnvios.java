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
// es para el row donde muestra la info del taco
 */
public class AdapterEnvios {
    public SimpleAdapter adapterEnvios (Context ctx, String FILTRO, String IDTACO){

        SQLiteDatabase db;
        DatabaseHandler_ dbhelper = new DatabaseHandler_(ctx);
        db = dbhelper.getReadableDatabase();


        String query = "SELECT "
                + "DISTINCT(a."+dbhelper.K_ENV0_ID+"),  "
                + " a."+dbhelper.K_ENV1_PLACA	+", "
               + " a."+dbhelper.K_ENV2_CODMOTOR	+", "
                + " a."+dbhelper.K_ENV3_CODCLIE	    +", "
                + " a."+dbhelper.K_ENV4_ORDENQUEMA	+", "
                + " a."+dbhelper.K_ENV5_PROCEDENCIA  +", "
                + " a."+dbhelper.K_ENV6_TIPOTRACAR	    +", "
                + " a."+dbhelper.K_ENV7_CODLOTE     	+", "
                + " a."+dbhelper.K_ENV8_NOMLOTE    	+", "
                + " a."+dbhelper.K_ENV9_CODTRA +", "
                + " a."+dbhelper.K_ENV10_NOMMOTOR     +" , "
                + " a."+dbhelper.K_ENV11_FSALING     +"  "
                + " FROM   "+dbhelper.TABLE_ENVIOS+" AS a "
                + " WHERE "
                + " CASE WHEN '"+IDTACO+"' = '' THEN a."+dbhelper.K_ENV0_ID+" = a."+dbhelper.K_ENV0_ID+" ELSE a."+dbhelper.K_ENV0_ID+" = '"+IDTACO+"' END "
                //+ " OR    CASE WHEN '"+FILTRO+"' = '0' THEN a."+dbhelper.K_ENV0_ID+" = a."+dbhelper.K_ENV0_ID+" ELSE a."+dbhelper.K_ENV0_ID+" LIKE '%"+FILTRO+"%' END "
               // + " OR    CASE WHEN '"+FILTRO+"' = '0' THEN a."+dbhelper.K_ENV1_PLACA+" = a."+dbhelper.K_ENV1_PLACA+" ELSE a."+dbhelper.K_ENV1_PLACA+" LIKE '%"+FILTRO+"%' END "
               // + " OR    CASE WHEN '"+FILTRO+"' = '0' THEN a."+dbhelper.K_ENV6_TIPOTRACAR+" = a."+dbhelper.K_ENV6_TIPOTRACAR+" ELSE a."+dbhelper.K_ENV6_TIPOTRACAR+" LIKE '%"+FILTRO+"%' END )"
                + " LIMIT 1"
                ;

        Log.i("QUERY", "QUERY: " + query);

        ArrayList<HashMap<String, String>> myListActivosOcaciones = new ArrayList<HashMap<String, String>>();
        String[] fromActivos = new String[] {"tvr_num_envio", "tvr_num_placa", "tvr_codmotor", "tvr_codclie", "tvr_ordenquema","tvr_procedencia","tvr_tipotracar","tvr_codlote","tvr_nomlote","tvr_codtra","tvr_nommotor","tvr_fsaling"};
        int[] toActivos = new int[] { R.id.tvr_num_envio, R.id.tvr_num_placa, R.id.tvr_codmotor, R.id.tvr_nomcargador, R.id.tvr_ordenquema, R.id.tvr_procedencia, R.id.tvr_tipotracar, R.id.tvr_codlote, R.id.tvr_nomlote, R.id.tvr_codtra, R.id.tvr_nommotor,R.id.tvr_fsaling};

        Cursor cFrentes = db.rawQuery(query, null);

        if(cFrentes.moveToFirst()){
            do{
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("tvr_num_envio"      , "TACO: "+cFrentes.getString(0));
                map.put("tvr_num_placa"	, "PLACA:"+cFrentes.getString(1));
                map.put("tvr_codmotor"	    , "CODMOT: "+cFrentes.getString(2));
                map.put("tvr_codclie"      , "CLIE: "+cFrentes.getString(3));
                map.put("tvr_ordenquema"	, "OC:"+cFrentes.getString(4));
                map.put("tvr_procedencia"	    , "PROC: "+cFrentes.getString(5));
                map.put("tvr_tipotracar"      , ""+cFrentes.getString(6));
                map.put("tvr_codlote"	, "COD:"+cFrentes.getString(7));
                map.put("tvr_nomlote"	, "LOTE:"+cFrentes.getString(8));
                map.put("tvr_codtra"	    , "CTRA: "+cFrentes.getString(9));
                map.put("tvr_nommotor"	    , ""+cFrentes.getString(10));
                map.put("tvr_fsaling"	    , "FS: "+cFrentes.getString(11));

                map.put("K_ENV0_ID"         , cFrentes.getString(0));
                map.put("K_ENV1_PLACA"	, cFrentes.getString(1));
                map.put("K_ENV2_CODMOTOR"	, cFrentes.getString(2));
                map.put("K_ENV3_CODCLIE"	    , cFrentes.getString(3));
                map.put("K_ENV4_ORDENQUEMA"	, cFrentes.getString(4));
                map.put("K_ENV5_PROCEDENCIA"	, cFrentes.getString(5));
                map.put("K_ENV6_TIPOTRACAR"	    , cFrentes.getString(6));
                map.put("K_ENV7_CODLOTE"	    , cFrentes.getString(7));
                map.put("K_ENV8_NOMLOTE"	    , cFrentes.getString(8));
                map.put("K_ENV9_CODTRA"	    , cFrentes.getString(9));
                map.put("K_ENV10_NOMMOTOR"	    , cFrentes.getString(10));
                map.put("K_ENV11_FSALING"	    , cFrentes.getString(11));
                myListActivosOcaciones.add(map);

            }while(cFrentes.moveToNext());
        }

        cFrentes.close();
        db.close();

        SimpleAdapter adapterlistEmpleados = new SimpleAdapter(ctx, myListActivosOcaciones,R.layout.row_lista_envios, fromActivos, toActivos);

        return adapterlistEmpleados;
    }

    public Cursor cursorEnvios(Context ctx, String FILTRO, String IDTACO){
        SQLiteDatabase db;
        DatabaseHandler_ dbhelper = new DatabaseHandler_(ctx);
        db = dbhelper.getReadableDatabase();

        String queryIntroSubConsultaPendientes = " SELECT COUNT(*)  "//AS TOTALPAGAR //m."+dbhelper.K_DET7_CANTIDAD+" * m."+dbhelper.K_DET12_PRECIO+"
                +" FROM "
                +dbhelper.TABLE_PLANILLA_DETALLE+" AS n"
                +" WHERE "
                +" n."+dbhelper.K_DET9_NOTACO    +" = a."+dbhelper.K_ENV0_ID+" "
                +" AND   n."+dbhelper.K_DET5_LLAVE     +" = '0' "
                ;

        String queryIntroSubConsultaTotalUnadas = " SELECT IFNULL(SUM(m."+dbhelper.K_DET4_CANTUNADAS+"), 0)  "//AS TOTALPAGAR //m."+dbhelper.K_DET7_CANTIDAD+" * m."+dbhelper.K_DET12_PRECIO+"
                +" FROM "
                +dbhelper.TABLE_PLANILLA_DETALLE+" AS m"
                +" WHERE "
                +" m."+dbhelper.K_DET9_NOTACO    +" = a."+dbhelper.K_ENV0_ID+" "
                ;


        String query = "SELECT "
                + "DISTINCT(a."+dbhelper.K_ENV0_ID+"),  "
                + " a."+dbhelper.K_ENV1_PLACA	+", "
                + " a."+dbhelper.K_ENV2_CODMOTOR	+", "
                + " a."+dbhelper.K_ENV3_CODCLIE	    +", "
                + " a."+dbhelper.K_ENV4_ORDENQUEMA	+", "
                + " a."+dbhelper.K_ENV5_PROCEDENCIA  +", "
                + " a."+dbhelper.K_ENV6_TIPOTRACAR	    +", "
                + " a."+dbhelper.K_ENV7_CODLOTE     	+", "
                + " a."+dbhelper.K_ENV8_NOMLOTE    	+", "
                + " a."+dbhelper.K_ENV9_CODTRA +", "
                + " a."+dbhelper.K_ENV10_NOMMOTOR     +" , "
                + " a."+dbhelper.K_ENV11_FSALING     +"  "
                + " FROM   "+dbhelper.TABLE_ENVIOS+" AS a "
                + " WHERE "
                + " CASE WHEN '"+IDTACO+"' = '' THEN a."+dbhelper.K_ENV0_ID+" = a."+dbhelper.K_ENV0_ID+" ELSE a."+dbhelper.K_ENV0_ID+" = '"+IDTACO+"' END "
                ;

        Log.i("QUERY", "QUERY: " + query);

        Cursor cEnvios = db.rawQuery(query, null);
        return cEnvios;
    }

    public boolean setInsertTacos(String ENVCOD, String ENVDESC, Context ctx)
    {
        try {
            SQLiteDatabase db;
            DatabaseHandler_ dbhelper = new DatabaseHandler_(ctx);
            db = dbhelper.getReadableDatabase();
            String query = "INSERT INTO " + dbhelper.TABLE_ENVIOS_REALIZADOS + " ( "
                    + dbhelper.K_ENVR1_ENVIO           + " , "
                    //+ dbhelper.K_ENVR2_ENVDESC          + " , "
                    + dbhelper.K_ENVR15_ESTATUSLOCAL     + " , "
                    + dbhelper.K_ENVR16_INGRESO_MANUAL
                    + " ) VALUES ( "
                    + " '" + ENVCOD     + "', "
                   // + " '" + ENVDESC    + "', "
                    + " '1', "
                    + " '0'  "
                    +") ";

            db.execSQL(query);
            db.close();
            Log.e("MI QUERY", "Valor:" + query);
            return true;
        }catch( Exception e){
            Log.e("Exception", "Exception: " + e);
            return false;

        }

    }

}