package com.ilcabana.atp.despachocombustible;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.ilcabana.atp.database.DatabaseHandler_;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import com.ilcabana.atp.library.Httppostaux;

public class ClassDescargarInicioApp{
	Config conf;
	String ws_datosConfiguracion   						=   Config.ws_bajarConfiguracionDispositivo;
	String ws_bajarListaEmpleadosRoza   				=   Config.ws_bajarListaEmpleadosRoza;
	String ws_cambiarPasswordUsuario					= 	Config.ws_cambiarPasswordUsuario;
	String ws_bajarQuincenas							= 	Config.ws_bajarQuincenas;
	String ws_eliminarDetallePlanillaUnadas				= 	Config.ws_eliminarDetallePlanillaUnadas;
	String ws_guardarNuevoMovimientoEnvio       		= 	Config.ws_guardarNuevoMovimientoEnvio;
	String ws_bajarMovEnvios						= 	Config.ws_bajarMovEnvios;
	String ws_bajarListadoEnvios 			= 	Config.ws_bajarListadoEnvios;

	static Httppostaux post;
	SQLiteDatabase db;
	static DatabaseHandler_ dbhelper;
	TelephonyManager tm;
	String imei;
	Context ctx;

	public ClassDescargarInicioApp(Context ctx)
	{
		
		post=new Httppostaux();	
		dbhelper = new DatabaseHandler_(ctx);
		tm = (TelephonyManager) ctx.getSystemService(ctx.TELEPHONY_SERVICE);
		imei = "";//tm.getDeviceId();
		conf = new Config();
		this.ctx = ctx;
	}


	public boolean descargarConfiguracionDispositivo( String user, String pass, String dominio )
	{
		try{
			ArrayList<NameValuePair> postparameters2send= new ArrayList<NameValuePair>();
			postparameters2send.add(new BasicNameValuePair("USERDOMINIO",dominio));
			postparameters2send.add(new BasicNameValuePair("USER",user));
			postparameters2send.add(new BasicNameValuePair("PASS",pass));
			postparameters2send.add(new BasicNameValuePair("IMEI", imei));
			postparameters2send.add(new BasicNameValuePair("NOMBREAPP" ,conf.key_nombreApp));
			postparameters2send.add(new BasicNameValuePair("APPVERSION",conf.key_versionApp));
			postparameters2send.add(new BasicNameValuePair("DBVERSION", Integer.toString(dbhelper.DATABASE_VERSION)));

			JSONArray datosjs = post.getserverdata(postparameters2send,ws_datosConfiguracion);
			Log.d(" Informe JSONArray", "Valor que contiene JSONArray los datos: " + datosjs);
			Log.d(" Informe JSONArray", "Valor que contiene POST: " + postparameters2send);
			if (datosjs != null && datosjs.length() > 0) {
				reiniciarSistema();
				for(int i=0;i<datosjs.length();i++){
					JSONObject e = datosjs.getJSONObject(i);

					db = dbhelper.getReadableDatabase();
					db.execSQL("INSERT INTO "+dbhelper.TABLE_ADMINITRACION+" VALUES ("
							+""+e.getString("usuCombId")  	     +"  , " //
							+"'"+e.getString("nombreUsuario")    +"' , " //
							+"'"+pass         					 +"' , " //
							+"'"+user       					 +"' , " //
							+"'"+e.getString("userName")  	     +"' , " //
							+"'"+e.getString("nivelAcceso")  	 +"'   " //
							+")");

					db.close();
				}
				return true;
			}else{
				return false;
			}

		}catch(Exception e){
			e.printStackTrace();
		}
		return true;
	}
	
	public boolean descargarCambiarPasswordUsuario( String USER, String PASS , String NEWPASS)
	{
		try{
			Log.d("ConfigDispositivo", "descargarConfiguracionDispositivo");
			
	    	ArrayList<NameValuePair> postparameters2send= new ArrayList<NameValuePair>();
		    postparameters2send.add(new BasicNameValuePair("USER",USER));
		    postparameters2send.add(new BasicNameValuePair("PASS",PASS));
		    postparameters2send.add(new BasicNameValuePair("NEWPASS",NEWPASS));
		    postparameters2send.add(new BasicNameValuePair("IMEI", imei));
		    postparameters2send.add(new BasicNameValuePair("NOMBREAPP" ,conf.key_nombreApp));
		    postparameters2send.add(new BasicNameValuePair("APPVERSION",conf.key_versionApp));
		    postparameters2send.add(new BasicNameValuePair("DBVERSION", Integer.toString(dbhelper.DATABASE_VERSION)));
		    
		    JSONArray datosjs = post.getserverdata(postparameters2send,ws_cambiarPasswordUsuario);
			Log.d(" Informe JSONArray", "Valor que contiene JSONArray los datos: " + datosjs);
			Log.d(" Informe JSONArray", "Valor que contiene POST: " + postparameters2send);
			if (datosjs != null && datosjs.length() > 0) {
				for(int i=0;i<datosjs.length();i++){
		  			JSONObject e = datosjs.getJSONObject(i);
		  			
			        setUpdateCampo(
							dbhelper.TABLE_ADMINITRACION,
							dbhelper.K_ADM2_PASSUSER,
							e.getString("USERPASS"),
							dbhelper.K_ADM0_ID,
							e.getString("USERID")
					);
				}
				return true;
			}else{
				return false;
			}			

		}catch(Exception e){
			e.printStackTrace();
		}
		return true;
	}

	public boolean ws_bajarListadoEnvios(String EmprId, String UsuId, String QuinId)
	{
		try{
			Log.d("MovimientosQuincena", "ws_bajarListadoEnvios");

			ArrayList<NameValuePair> postparameters2send= new ArrayList<NameValuePair>();
			postparameters2send.add(new BasicNameValuePair("EmprId", EmprId));
			postparameters2send.add(new BasicNameValuePair("UsuId", UsuId));
			postparameters2send.add(new BasicNameValuePair("CuaId", QuinId));

			JSONArray datosjs = post.getserverdata(postparameters2send,ws_bajarListadoEnvios);
			Log.d(" Informe JSONArray", "Valor que contiene JSONArray los datos: " + datosjs);
			Log.d(" Informe JSONArray", "Valor que contiene POST: " + postparameters2send);
			if (datosjs != null && datosjs.length() > 0) {

				db = dbhelper.getReadableDatabase();
				String queryDelete = "";
					queryDelete = "DELETE FROM "+dbhelper.TABLE_ENVIOS_REALIZADOS+
						" WHERE "+dbhelper.K_ENVR15_ESTATUSLOCAL +" = '0' "
					;
				db.execSQL(queryDelete);

				Log.d(" Insert delete", "" + queryDelete);
				db.close();
				for(int i=0;i<datosjs.length();i++){
					JSONObject e = datosjs.getJSONObject(i);

					db = dbhelper.getReadableDatabase();
					String query = "INSERT INTO " + dbhelper.TABLE_ENVIOS_REALIZADOS + " ( " +
							dbhelper.K_ENVR1_ENVIO			+",  " +
							//dbhelper.K_ENVR2_ENVDESC		+",  " +
							dbhelper.K_ENVR15_ESTATUSLOCAL	+",  " +
							dbhelper.K_ENVR16_INGRESO_MANUAL	+"   " +
							") VALUES ( " +
							" '"+e.getString("TACO") 		+"', " +
							//" '"+e.getString("TACODESC") 	+"', " +
							" '0', " +
							" '0'  " +
							" ) ";

					Log.d(" Insert query", "" + query);
					db.execSQL(query);

					db.close();
				}

				return true;
			}else{
				return true;
			}

		}catch(Exception e){
			e.printStackTrace();
			return false;

		}
	}


	public String ws_eliminarDetallePlanillaUnadas( String USER, String LLAVE_DETALLE )
	{
		String mensaje = "";
		try{
	    	ArrayList<NameValuePair> postparameters2send= new ArrayList<NameValuePair>();
		    postparameters2send.add(new BasicNameValuePair("UsuId",Config.getKey_UsuId_()));
		    postparameters2send.add(new BasicNameValuePair("LLAVE_DETALLE",LLAVE_DETALLE));
		    int ESTADO_DIA, LLAVE;
		    
		    JSONArray datosjs = post.getserverdata(postparameters2send,ws_eliminarDetallePlanillaUnadas);
			Log.d(" Informe JSONArray", "Valor que contiene JSONArray los datos: " + datosjs);
			Log.d(" Informe JSONArray", "Valor que contiene POST: " + postparameters2send);
			if (datosjs != null && datosjs.length() > 0) {
				
				JSONObject json_data; //creamos un objeto JSON
				json_data = datosjs.getJSONObject(0); //leemos el primer segmento en nuestro caso el unico
				ESTADO_DIA = json_data.getInt("cantidad");//accedemos al valor

					if(ESTADO_DIA == 0){
					eliminarDetalle("'"+LLAVE_DETALLE+"'");
					}else{
						mensaje = "No se pudo eliminar tarea";
					}
				return mensaje;
			}else{
				return mensaje;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return mensaje;
	}
// este sirve para el row del taco
	public boolean ws_bajarListadoEnvios (String Taco,String Placa)
	{
		try{
			Log.d("descargarInsertActivos", "descargarInsertActivos");
			ArrayList<NameValuePair> postparameters2send= new ArrayList<NameValuePair>();
			postparameters2send.add(new BasicNameValuePair("Taco",Taco));
			postparameters2send.add(new BasicNameValuePair("Placa", Placa));

			JSONArray datosjs = post.getserverdata(postparameters2send,ws_bajarListadoEnvios);
			Log.e(" Informe JSONArray", "Valor que contiene JSONArray los datos: " + datosjs);
			db = dbhelper.getReadableDatabase();
			String queryDelete = "";
			queryDelete = "DELETE FROM "+dbhelper.TABLE_ENVIOS;

			db.execSQL(queryDelete);
			Log.d(" Insert delete", "" + queryDelete);
			db.close();
			if (datosjs != null && datosjs.length() > 0) {

				for(int i=0; i<datosjs.length(); i++){
					JSONObject e = datosjs.getJSONObject(i);
					db = dbhelper.getReadableDatabase();
					db.execSQL("INSERT INTO "+dbhelper.TABLE_ENVIOS+" VALUES ("
							+""+e.getString("TACO")				+",   " //0 CORRELATIVO QUE VIENE DEL WEB SERVICE
							+"'"+e.getString("PLACA")     		+"' , "	//1
							+"'"+e.getString("CODMOTOR")  		+"' , "	//2
							+"'"+e.getString("CODCLIE")  		+"' , "	//3
							+"'"+e.getString("ORDENQUEMA")  		+"' , "	//4
							+"'"+e.getString("PROCEDENCIA")  	+"' , "	//5
							+"'"+e.getString("TIPOTRACAR")   	+"' , "	//6
							+"'"+e.getString("CODLOTE")  		+"' , "	//7
							+"'"+e.getString("NOMLOTE")  		+"' , "	//8
							+"'"+e.getString("CODTRA")  		    +"' , "	//9
							+"'"+e.getString("NOMMOTOR")  		+"' , "	//10
							+"'"+e.getString("FSALING")  		+"'  "	//11
							+")");

					db.close();

				}
				return true;
			}else{
				return false;
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}


	public boolean ws_bajarMovEnvios(String EmprId, String UsuId)
	{
		try{

			Log.d("descargarInsertActivos", "descargarInsertActivos");
	    	ArrayList<NameValuePair> postparameters2send= new ArrayList<NameValuePair>();
		    postparameters2send.add(new BasicNameValuePair("EmprId", EmprId));
			postparameters2send.add(new BasicNameValuePair("UsuId", UsuId));
		    JSONArray datosjs = post.getserverdata(postparameters2send,ws_bajarMovEnvios);
		    Log.d(" Informe JSONArray", "Valor que contiene JSONArray los datos: " + datosjs);
		if (datosjs != null && datosjs.length() > 0) {

			db = dbhelper.getReadableDatabase();
			String queryDelete = "";
				queryDelete = "DELETE FROM "+dbhelper.TABLE_ENVIOS;

			db.execSQL(queryDelete);
			Log.d(" Insert query", "" + queryDelete);
			db.close();

			for(int i=0; i<datosjs.length(); i++){
	  			JSONObject e = datosjs.getJSONObject(i);
		        db = dbhelper.getReadableDatabase();
		        db.execSQL("INSERT INTO "+dbhelper.TABLE_ENVIOS+" VALUES ("
		        		+""+e.getString("TACO")				+",   " //0 CORRELATIVO QUE VIENE DEL WEB SERVICE
	        			+"'"+e.getString("PLACA")     		+"' , "	//1
	        			+"'"+e.getString("CODMOTOR")  	+"'   "	//2
						+""+e.getString("CODCLIE")				+",   " //3
						+"'"+e.getString("ORDENQUEMA")     		+"' , "	//4
						+"'"+e.getString("PROCEDENCIA")  	+"'   "	//5
						+""+e.getString("TIPOTRACAR")				+",   " //6
						+"'"+e.getString("CODLOTE")     		+"' , "	//7
						+"'"+e.getString("NOMLOTE")  	+"'   "	//8
						+""+e.getString("CODTRA")				+",   " //9
						+"'"+e.getString("NOMMOTOR")     		+"' , "	//10
						+"'"+e.getString("FSALING")  	+"'   "	//11
	        			+")");  
		        db.close();
			}
			return true;
		}else{
			return false;
		}			
			
		}catch (Exception e) {
			e.printStackTrace();
		}	
		return true;		
	}

	public boolean ws_guardarNuevoMovimientoEnvio(String MovEnvio){
		try{
			ArrayList<NameValuePair> postparameters2send= new ArrayList<NameValuePair>();

			db = dbhelper.getReadableDatabase();
			int LLAVE = 0;
			int resp_id = 0;

			String query = " SELECT "
					+ dbhelper.K_DESP0_ID          		+ " , "
					+ dbhelper.K_DESP1_ENVIO            + " , "
					+ dbhelper.K_DESP2_CARGADORA        + " , "
					+ dbhelper.K_DESP3_CARGADOR         + " , "
					+ dbhelper.K_DESP4_NOMBRECARD      + " , "
					+ dbhelper.K_DESP5_FCORTA           + " , "
					+ dbhelper.K_DESP6_HCORTA           + " , "
					+ dbhelper.K_DESP7_FLLEGA           + " , "
					+ dbhelper.K_DESP8_HLLEGA           + " , "
					+ dbhelper.K_DESP9_FCARGA           + " , "
					+ dbhelper.K_DESP10_HCARGA           + " , "
					+ dbhelper.K_DESP11_FSALIDA         + " , "
					+ dbhelper.K_DESP12_HSALIDA         + " , "
					+ dbhelper.K_DESP13_OBSERV          + " , "
					+ dbhelper.K_DESP14_ULTIMOENV       + " ,  "
					+ dbhelper.K_DESP16_OC               + " ,  "
					+ dbhelper.K_DESP17_NOENVIO               + " ,  "
					+ dbhelper.K_DESP18_TIPCANA               + "   "
					+" FROM  " +dbhelper.TABLE_DESPACHO_ENVIOS	+" a "
					+" WHERE 	a."+dbhelper.K_DESP1_ENVIO +" <> '"+MovEnvio+"' "
					+" ORDER BY a."+dbhelper.K_DESP0_ID+" DESC  LIMIT 1 "
					;
			Log.e("INFORME QUERY", "MI QUERY: " + query);
			Cursor c = db.rawQuery(query, null);
			if(c.moveToFirst()){
				do{

					resp_id = Integer.parseInt(c.getString(0));
					postparameters2send.add(new BasicNameValuePair("NUMTACO"  		,c.getString(1)));
					postparameters2send.add(new BasicNameValuePair("CARGADORA" 			,c.getString(2)));
					postparameters2send.add(new BasicNameValuePair("CARGADOR" 		,c.getString(3)));
					postparameters2send.add(new BasicNameValuePair("NOMBRECARD"  ,c.getString(4)));
					postparameters2send.add(new BasicNameValuePair("FCORTA" 	,c.getString(5)));
					postparameters2send.add(new BasicNameValuePair("HCORTA" 	,c.getString(6)));
					postparameters2send.add(new BasicNameValuePair("FLLEGA" 			,c.getString(7)));
					postparameters2send.add(new BasicNameValuePair("HLLEGA" 			,c.getString(8)));
					postparameters2send.add(new BasicNameValuePair("FCARGA" 		,c.getString(9)));
					postparameters2send.add(new BasicNameValuePair("HCARGA" 		,c.getString(10)));
					postparameters2send.add(new BasicNameValuePair("FSALIDA" ,c.getString(11)));
					postparameters2send.add(new BasicNameValuePair("HSALIDA" ,c.getString(12)));
					postparameters2send.add(new BasicNameValuePair("OBSERV"  ,c.getString(13)));
					postparameters2send.add(new BasicNameValuePair("ULTIMOENV"  ,c.getString(14)));
					postparameters2send.add(new BasicNameValuePair("OC"  ,c.getString(15)));
					postparameters2send.add(new BasicNameValuePair("NOENVIO"  ,c.getString(16)));
					postparameters2send.add(new BasicNameValuePair("TIPCANA"  ,c.getString(17)));

					JSONArray datosjs = post.getserverdata(postparameters2send, ws_guardarNuevoMovimientoEnvio);
					Log.e(" Informe JSONArray", "Valor que contiene JSONArray los datos: " + datosjs);
					Log.e(" Informe JSONArray", "Valor que contiene JSONArray los datos: " + postparameters2send);

					db = dbhelper.getReadableDatabase();
					String queryDelete = "";
					queryDelete = "DELETE FROM "+dbhelper.TABLE_ENVIOS;

					db.execSQL(queryDelete);
					Log.d(" Insert delete", "" + queryDelete);

					if (datosjs != null && datosjs.length() > 0) {

						JSONObject json_data; //creamos un objeto JSON
						json_data = datosjs.getJSONObject(0); //leemos el primer segmento en nuestro caso el unico
						LLAVE = json_data.getInt("NUMTACO");//accedemos al valor

						Log.e("Informe", "Resultado web service resultado:" + LLAVE);//muestro por log que obtuvimos

						setUpdateCampo(
								dbhelper.TABLE_DESPACHO_ENVIOS,
								dbhelper.K_DESP15_LLAVE,
								"'"+LLAVE+"'",
								dbhelper.K_DESP0_ID ,
								Integer.toString(resp_id)
						);
					}
				}while(c.moveToNext());
			}
			Log.e("INFORME POST", "valor que lo valores post: " + postparameters2send);

			c.close();
			db.close();

		}catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	public boolean ws_bajarMovEnvios(String EmprId)
	{
		try{
			Log.d("ConfigDispositivo", "descargarConfiguracionDispositivo");

			ArrayList<NameValuePair> postparameters2send= new ArrayList<NameValuePair>();
			postparameters2send.add(new BasicNameValuePair("EmprId", EmprId));

			JSONArray datosjs = post.getserverdata(postparameters2send,ws_bajarMovEnvios);
			Log.d(" Informe JSONArray", "Valor que contiene JSONArray los datos: " + datosjs);
			Log.d(" Informe JSONArray", "Valor que contiene POST: " + postparameters2send);
			if (datosjs != null && datosjs.length() > 0) {

				db = dbhelper.getReadableDatabase();
				db.execSQL("DELETE FROM "+dbhelper.TABLE_ENVIOS);
				db.close();
				for(int i=0;i<datosjs.length();i++){
					JSONObject e = datosjs.getJSONObject(i);

					db = dbhelper.getReadableDatabase();
					String queryInsert = "INSERT INTO " + dbhelper.TABLE_ENVIOS + " ( "
							+ dbhelper.K_ENV0_ID            + " , "
							+ dbhelper.K_ENV1_PLACA        + " , "
							+ dbhelper.K_ENV2_CODMOTOR         + " , "
							+ dbhelper.K_ENV3_CODCLIE      + "   "
							+ dbhelper.K_ENV4_ORDENQUEMA           + " , "
							+ dbhelper.K_ENV5_PROCEDENCIA           + " , "
							+ dbhelper.K_ENV6_TIPOTRACAR            + " , "
							+ dbhelper.K_ENV7_CODLOTE           + " , "
							+ dbhelper.K_ENV8_NOMLOTE           + " , "
							+ dbhelper.K_ENV9_CODTRA           + " , "
							+ dbhelper.K_ENV10_NOMMOTOR         + " , "
							+ dbhelper.K_ENV11_FSALING         + "  "


							+ " ) VALUES ( "
							+ " '" + e.getString("TACO")   	+ "' , "
							+ " '" + e.getString("PLACA")   	+ "' , "
							+ " '" + e.getString("CARGADOR")   	+ "' , "
							+ " '" + e.getString("CODMOTOR")   + "' ,  "
							+ " '" + e.getString("CODCLIE")   	+ "' , "
							+ " '" + e.getString("ORDENQUEMA")   	+ "' , "
							+ " '" + e.getString("PROCEDENCIA")   	+ "' , "
							+ " '" + e.getString("TIPOTRACAR")   	+ "' , "
							+ " '" + e.getString("NOMLOTE")       + "' , "
							+ " '" + e.getString("CODTRA")       + "' , "
							+ " '" + e.getString("NOMMOTOR")      + "' , "
							+ " '" + e.getString("FSALING")      + "'  "
							+" ) "
							;

					db.execSQL(queryInsert);

					db.close();
				}

				return true;
			}else{
				return false;
			}

		}catch(Exception e){
			e.printStackTrace();
		}
		return true;
	}




	public boolean bajarListaEmpleadosRoza(String EmprId)
	{
		try{
			Log.d("ConfigDispositivo", "descargarConfiguracionDispositivo");
			
	    	ArrayList<NameValuePair> postparameters2send= new ArrayList<NameValuePair>();
		    postparameters2send.add(new BasicNameValuePair("EmprId", EmprId));

		    JSONArray datosjs = post.getserverdata(postparameters2send,ws_bajarListaEmpleadosRoza);
			Log.d(" Informe JSONArray", "Valor que contiene JSONArray los datos: " + datosjs);
			Log.d(" Informe JSONArray", "Valor que contiene POST: " + postparameters2send);
			if (datosjs != null && datosjs.length() > 0) {
				
		        db = dbhelper.getReadableDatabase();
		        db.execSQL("DELETE FROM "+dbhelper.TABLE_EMPLEADOS); 
		        db.close();
				for(int i=0;i<datosjs.length();i++){
		  			JSONObject e = datosjs.getJSONObject(i);
		  			
			        db = dbhelper.getReadableDatabase();
			        db.execSQL("INSERT INTO "+dbhelper.TABLE_EMPLEADOS+" VALUES ( "
			        			+""+e.getString("CORRELATIVO")    		+"  , " //0 CORRELATIVO QUE VIENE DEL WEB SERVICE
			        			+"'"+e.getString("COD_EMPLEADO")        +"' , " //1	
			        			+"'"+e.getString("NOM_EMPLEADO")   		+"' , " //2
			        			+" "+e.getString("COD_CUADRILLA") 		+"  , " //3
			        			+"'"+e.getString("NOM_CUADRILLA")  	  	+"' , " //4
			        			+"'"+e.getString("COD_FRENTE")   	 	+"' , " //5
			        			+"'"+e.getString("NOM_FRENTE")    		+"' , " //6	
			        			+"'"+e.getString("NOM_TIPO")    		+"' , " //6	
			        			+"'0' " //ESTATUS DISPONIBLE
			        			+")");  
			        
			        db.close();
				}

				return true;
			}else{
				return false;
			}			

		}catch(Exception e){
			e.printStackTrace();
		}
		return true;
	}

	
	public boolean bajarQuincenas(String EmprId)
	{
		try{
			Log.d("Metodo descargarConfi", "descargarConfiguracionDispositivo");
			
	    	ArrayList<NameValuePair> postparameters2send= new ArrayList<NameValuePair>();
		    postparameters2send.add(new BasicNameValuePair("EmprId",EmprId));

		    JSONArray datosjs = post.getserverdata(postparameters2send,ws_bajarQuincenas);
			Log.d(" Informe JSONArray", "Valor que contiene JSONArray los datos: " + datosjs);
			Log.d(" Informe JSONArray", "Valor que contiene POST: " + postparameters2send);
			if (datosjs != null && datosjs.length() > 0) {
				
		        db = dbhelper.getReadableDatabase();
		        db.execSQL("DELETE FROM "+dbhelper.TABLE_QUINCENAS); 
		        db.close();
				for(int i=0;i<datosjs.length();i++){
		  			JSONObject e = datosjs.getJSONObject(i);
		  			
			        db = dbhelper.getReadableDatabase();
			        db.execSQL("INSERT INTO "+dbhelper.TABLE_QUINCENAS+" VALUES ( "
			        			+""+e.getString("IDQUINCENA")      	+"  , " //0 CORRELATIVO QUE VIENE DEL WEB SERVICE
			        			+"'"+e.getString("FECHADESDE")     	+"' , " //1	
			        			+"'"+e.getString("FECHAHASTA")   	+"' , " //2
			        			+"'"+e.getString("ZAFRA") 			+"' , " //3
			        			+"'"+e.getString("CORTE") 			+"' , " //3
			        			+"'"+Config.getFecha()				+"'   " //3
			        			+")");  

			        db.close();
				}

				return true;
			}else{
				return false;
			}			

		}catch(Exception e){
			e.printStackTrace();
		}
		return true;
	}


	public void setUpdateCampo(String nombreTabla, String campoTablaUpdate, String miCampoValue, String campoTablaCondicion, String miCondicion)
	{
        db = dbhelper.getReadableDatabase();
        String query = "UPDATE "+nombreTabla+" SET "+
        		campoTablaUpdate+"  = "+miCampoValue+" "+
        		" WHERE "+campoTablaCondicion+" = "+miCondicion;
        db.execSQL(query);
        db.close();
		Log.e("MI QUERY", "Valor:" + query);

          
	}

	public boolean reiniciarSistema(){
		SQLiteDatabase db;
		DatabaseHandler_ dbhelper = new DatabaseHandler_(ctx);
		db = dbhelper.getReadableDatabase();

		db.execSQL("DELETE FROM " + dbhelper.TABLE_ADMINITRACION);
		db.execSQL("DELETE FROM " + dbhelper.TABLE_EMPLEADOS);
		db.execSQL("DELETE FROM " + dbhelper.TABLE_PLANILLA_DETALLE);
		db.execSQL("DELETE FROM " + dbhelper.TABLE_PROVEDORES);
		db.execSQL("DELETE FROM " + dbhelper.TABLE_ACTIVIDADES);

		db.close();
		return true;
	}
	
	public void eliminarDetalle(String LLAVE_DETALLE)
	{

        db = dbhelper.getReadableDatabase();
        String query = "DELETE FROM "+dbhelper.TABLE_PLANILLA_DETALLE +" WHERE "+dbhelper.K_DET5_LLAVE+ " = "+LLAVE_DETALLE;
        Log.d("MI QUERY DELETE", "Valor:" + query);
        db.execSQL(query);
        db.close();

          
	}
	


}
