package com.ilcabana.atp.despachocombustible;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.text.format.Time;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;


import com.ilcabana.atp.database.DatabaseHandler_;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

//mport java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;

import com.ilcabana.atp.library.Httppostaux;

public class ActListaEnvios extends AppCompatActivity implements SearchView.OnQueryTextListener {
    Httppostaux post;
    private ProgressDialog pDialog;
    ListView lv_envios;
    SQLiteDatabase db;
    DatabaseHandler_ dbhelper;
    int tipoLista;
    TableLayout tbl_btn_fecha_hora_quema,tbl_btn_fecha_hora_Corta,tbl_btn_fecha_hora_llega,tbl_btn_fecha_hora_Carga,tbl_btn_fecha_hora_Salida;
    private int mYear, mMonth, mDay, mHour, mMinute;
    SimpleAdapter simpleAdaptador;
    private SearchView mSearchView;
    Context ctx = this;
    AdapterEnvios adpEnvios = new AdapterEnvios();
    static String COD_QUINCENA, FECHA_DIA, COD_CUADRILLA, ENV0_ID, NOMBRE_DIA;
    static String NUMTACO, CODLOTE, DESCLOTE;
    static int estadoBotonMenu = 2;
    ClassDescargarInicioApp classDescargarInicioApp;
    String TacoNumero = "0";
    String PlacaNumero = "0";
    TextView tv_fecha_quin,txtDate, txtHora,txtDatel, txtHoral,txtDatec, txtHorac,txtDates, txtHoras,tv_searchFechallega,tv_searchFechaCorta,tv_searchFechaCarga,tv_searchFechaSalida,txt_Observaciones,tv_searchHoraCorta,tv_searchHorallega,tv_searchHoraCarga,tv_searchHoraSalida,txt_oc;
    EditText txt_placa;
    TextView txt_Envio;
    EditText txt_num_taco;
    EditText txt_FsalIng;
    Spinner spinner_Cargadora,spinner_Cargador,spinner_Cana;
    String fechaNewAplicacion = "";
    double CantidadOriginal;
    CheckBox checkBox_UltimoEn;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_lista_envios);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tbl_btn_fecha_hora_Corta = (TableLayout)findViewById(R.id.tbl_btn_fecha_hora_Corta);
        tbl_btn_fecha_hora_llega = (TableLayout)findViewById(R.id.tbl_btn_fecha_hora_llega);
        tbl_btn_fecha_hora_Carga = (TableLayout)findViewById(R.id.tbl_btn_fecha_hora_Carga);
        tbl_btn_fecha_hora_Salida = (TableLayout)findViewById(R.id.tbl_btn_fecha_hora_Salida);
        txtDate = (TextView) findViewById(R.id.tv_searchFechaCorta);
        txtHora = (TextView) findViewById(R.id.tv_searchHoraCorta);
        txtDatel = (TextView) findViewById(R.id.tv_searchFechallega);
        txtHoral = (TextView) findViewById(R.id.tv_searchHorallega);
        txtDatec = (TextView) findViewById(R.id.tv_searchFechaCarga);
        txtHorac = (TextView) findViewById(R.id.tv_searchHoraCarga);
        txtDates = (TextView) findViewById(R.id.tv_searchFechaSalida);
        txtHoras = (TextView) findViewById(R.id.tv_searchHoraSalida);

        tbl_btn_fecha_hora_Corta.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                tbl_btn_fecha_hora_Corta.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_light));
                    final Calendar c = Calendar.getInstance();
                    mYear = c.get(Calendar.YEAR);
                    mMonth = c.get(Calendar.MONTH);
                    mDay = c.get(Calendar.DAY_OF_MONTH);
                    // Launch Date Picker Dialog
                    DatePickerDialog dpd = new DatePickerDialog(ActListaEnvios.this,
                            new DatePickerDialog.OnDateSetListener() {

                                @Override
                                public void onDateSet(DatePicker view, int year,
                                                      int monthOfYear, int dayOfMonth) {
                                    // Display Selected date in textbox
                                    txtDate.setText(dayOfMonth +"-"+ (monthOfYear + 1) +"-"+ year);
                                    final Calendar c = Calendar.getInstance();
                                    mYear =  c.get(Calendar.YEAR);
                                    mMonth = c.get(Calendar.MONTH);
                                    mDay =   c.get(Calendar.DAY_OF_MONTH);

                                    // Launch Time Picker Dialog
                                    TimePickerDialog tpd = new TimePickerDialog(ActListaEnvios.this, new TimePickerDialog.OnTimeSetListener() {

                                        @Override
                                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                            txtHora.setText(hourOfDay + ":" + minute);
                                            tbl_btn_fecha_hora_Corta.setBackgroundColor(getResources().getColor(R.color.gris333));
                                        }
                                    }, mHour, mMinute, true);//true= boolean is24HourView
                                    tpd.show();
                                }
                            }, mYear, mMonth, mDay);
                    dpd.show();
            }
        });
        tbl_btn_fecha_hora_llega.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                tbl_btn_fecha_hora_llega.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_light));
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);
                // Launch Date Picker Dialog
                DatePickerDialog dpd = new DatePickerDialog(ActListaEnvios.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // Display Selected date in textbox
                                txtDatel.setText(dayOfMonth +"-"+ (monthOfYear + 1) +"-"+ year);
                                final Calendar c = Calendar.getInstance();
                                mYear =  c.get(Calendar.YEAR);
                                mMonth = c.get(Calendar.MONTH);
                                mDay =   c.get(Calendar.DAY_OF_MONTH);

                                // Launch Time Picker Dialog
                                TimePickerDialog tpd = new TimePickerDialog(ActListaEnvios.this, new TimePickerDialog.OnTimeSetListener() {

                                    @Override
                                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                        txtHoral.setText(hourOfDay + ":" + minute);
                                        tbl_btn_fecha_hora_llega.setBackgroundColor(getResources().getColor(R.color.gris333));
                                    }
                                }, mHour, mMinute, true);//true= boolean is24HourView
                                tpd.show();
                            }
                        }, mYear, mMonth, mDay);
                dpd.show();
            }
        });
        tbl_btn_fecha_hora_Carga.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                tbl_btn_fecha_hora_Carga.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_light));
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);
                // Launch Date Picker Dialog
                DatePickerDialog dpd = new DatePickerDialog(ActListaEnvios.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // Display Selected date in textbox
                                txtDatec.setText(dayOfMonth +"-"+ (monthOfYear + 1) +"-"+ year);
                                final Calendar c = Calendar.getInstance();
                                mYear =  c.get(Calendar.YEAR);
                                mMonth = c.get(Calendar.MONTH);
                                mDay =   c.get(Calendar.DAY_OF_MONTH);

                                // Launch Time Picker Dialog
                                TimePickerDialog tpd = new TimePickerDialog(ActListaEnvios.this, new TimePickerDialog.OnTimeSetListener() {

                                    @Override
                                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                        txtHorac.setText(hourOfDay + ":" + minute);
                                        tbl_btn_fecha_hora_Carga.setBackgroundColor(getResources().getColor(R.color.gris333));
                                    }
                                }, mHour, mMinute, true);
                                tpd.show();
                            }
                        }, mYear, mMonth, mDay);
                dpd.show();
            }
        });
        tbl_btn_fecha_hora_Salida.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                tbl_btn_fecha_hora_Salida.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_light));
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);
                // Launch Date Picker Dialog
                DatePickerDialog dpd = new DatePickerDialog(ActListaEnvios.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // Display Selected date in textbox
                                txtDates.setText(dayOfMonth +"-"+ (monthOfYear + 1) +"-"+ year);
                                final Calendar c = Calendar.getInstance();
                                mYear =  c.get(Calendar.YEAR);
                                mMonth = c.get(Calendar.MONTH);
                                mDay =   c.get(Calendar.DAY_OF_MONTH);

                                // Launch Time Picker Dialog
                                TimePickerDialog tpd = new TimePickerDialog(ActListaEnvios.this, new TimePickerDialog.OnTimeSetListener() {

                                    @Override
                                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                        txtHoras.setText(hourOfDay + ":" + minute);
                                        tbl_btn_fecha_hora_Salida.setBackgroundColor(getResources().getColor(R.color.gris333));
                                    }
                                }, mHour, mMinute, true);
                                tpd.show();
                            }
                        }, mYear, mMonth, mDay);
                dpd.show();
            }
        });


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                String FHsalingS = txt_FsalIng.getText().toString();

                String Fllega = tv_searchFechallega.getText().toString();
                String Hllega = tv_searchHorallega.getText().toString();

                String Fcarga = tv_searchFechaCarga.getText().toString();
                String Hcarga  = tv_searchHoraCarga.getText().toString();

                String Fsalida = tv_searchFechaSalida.getText().toString();
                String Hsalida = tv_searchHoraSalida.getText().toString();

                //String FHsalingS = Fsaling;
                Date FHsaling = null;
                try {
                    FHsaling = sdf.parse(FHsalingS);
                } catch (ParseException e) {
                    e.printStackTrace();
                    Toast.makeText(ctx, FHsalingS, Toast.LENGTH_SHORT).show();
                }

                String FHllegaS = Fllega +" "+ Hllega;
                Date FHllega = null;
                 try {
                    FHllega = sdf.parse(FHllegaS);
                } catch (ParseException e) {
                    e.printStackTrace();
                     Toast.makeText(ctx, FHllegaS, Toast.LENGTH_SHORT).show();

                }
                String FHcargaS = Fcarga  +" "+ Hcarga ;
                Date FHcarga = null;
                try {
                    FHcarga = sdf.parse(FHcargaS);
                } catch (ParseException e) {
                    e.printStackTrace();
                    Toast.makeText(ctx, FHcargaS, Toast.LENGTH_SHORT).show();
                }

                String FHsalidaS = Fsalida  +" "+ Hsalida ;
                Date FHsalida = null;
                try {
                    FHsalida = sdf.parse(FHsalidaS);
                } catch (ParseException e) {
                    e.printStackTrace();
                    Toast.makeText(ctx, FHsalidaS, Toast.LENGTH_SHORT).show();
                }

                if (FHllega.compareTo(FHsaling) > 0) {
                    Toast.makeText(ctx, "La FEHCHA,HORA de LLEGADA no puede ser MENOR  a  SALIDA", Toast.LENGTH_SHORT).show();
                    txt_num_taco.setText("");
                }
                if(FHllega.compareTo(FHcarga) > 0) {
                    Toast.makeText(ctx, "La FEHCA,HORA de CARGA no puede ser MENOR  a  LLEGADA", Toast.LENGTH_SHORT).show();
                    txt_num_taco.setText("");
                }

                if(FHcarga.compareTo(FHsalida) > 0) {
                    Toast.makeText(ctx, "La FECHA,HORA de SALIDA no puede ser MENOR  a CARGA", Toast.LENGTH_SHORT).show();
                    txt_num_taco.setText("");
                }


                if(      txt_num_taco.getText().toString().equals("")
                        || txt_Envio.getText().toString().equals("")
                        || tv_searchFechaCorta.getText().toString().equals("")
                        || tv_searchFechallega.getText().toString().equals("")
                        || tv_searchFechallega.getText().toString().equals("")
                        || tv_searchFechaSalida.getText().toString().equals("")
                        || txt_placa.getText().toString().equals("")

                        )
                {
                    Toast.makeText(ctx, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show();
                    Snackbar.make(view, "Todos los campos son obligatorios", Snackbar.LENGTH_LONG)
                            .setAction("Advertencia", null).show();

                }else {

                            if (setInsertDespachoCombustible(
                                    txt_num_taco.getText().toString(),
                                    getCargadora(spinner_Cargadora.getSelectedItem().toString()),
                                    getCargadorCode(spinner_Cargador.getSelectedItem().toString()),
                                    getCargadorName(spinner_Cargador.getSelectedItem().toString()),
                                    tv_searchFechaCorta.getText().toString(),
                                    tv_searchHoraCorta.getText().toString(),
                                    tv_searchFechallega.getText().toString(),
                                    tv_searchHorallega.getText().toString(),
                                    tv_searchFechaCarga.getText().toString(),
                                    tv_searchHoraCarga.getText().toString(),
                                    tv_searchFechaSalida.getText().toString(),
                                    tv_searchHoraSalida.getText().toString(),
                                    txt_Observaciones.getText().toString(),
                                    checkBox_UltimoEn.isChecked()?"1":"0",
                                    txt_oc.getText().toString(),
                                    txt_Envio.getText().toString(),
                                    getCana(spinner_Cana.getSelectedItem().toString()),
                                    txt_placa.getText().toString()

                            )) {
                                if (verificaConexion(ctx)) {
                                    metodoRespaldarEnvios();
                                    Toast.makeText(ctx, "hay conexión a Internet", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(ctx, "No hay conexión a Internet, verifique y reintentar", Toast.LENGTH_SHORT).show();
                                    Intent i = new Intent(ctx, EnviosRealizados.class);
                                    startActivity(i);
                                }
                            }
                        }

                    }
            private String getCana(String toString) {
                String[] separados = toString.split("-");
                return separados[0];
            }
            private String getCargadora(String toString) {
                String[] separados = toString.split("-");
                return separados[0];
            }
            private String getCargadorCode(String toString) {

                String[] separados = toString.split("-");
                return separados[1];
            }
            private String getCargadorName(String toString) {

                String[] separados = toString.split("-");
                return separados[0];
            }

        });

        txt_num_taco        = (EditText)findViewById(R.id.txt_num_taco);
        spinner_Cargadora = (Spinner) findViewById(R.id.spinner_Cargadora);
        spinner_Cargador = (Spinner) findViewById(R.id.spinner_Cargador);
        txt_placa           = (EditText)findViewById(R.id.txt_placa);
        txt_FsalIng           = (EditText)findViewById(R.id.txt_FsalIng);
        tv_searchFechaCorta     = (TextView) findViewById(R.id.tv_searchFechaCorta);
        tv_searchHoraCorta     = (TextView) findViewById(R.id.tv_searchHoraCorta);
        tv_searchFechallega     = (TextView) findViewById(R.id.tv_searchFechallega);
        tv_searchHorallega     = (TextView) findViewById(R.id.tv_searchHorallega);
        tv_searchFechaCarga     = (TextView) findViewById(R.id.tv_searchFechaCarga);
        tv_searchHoraCarga    = (TextView) findViewById(R.id.tv_searchHoraCarga);
        tv_searchFechaSalida     = (TextView) findViewById(R.id.tv_searchFechaSalida);
        tv_searchHoraSalida     = (TextView) findViewById(R.id.tv_searchHoraSalida);
        txt_Observaciones    = (EditText)findViewById(R.id.txt_Observaciones);
        checkBox_UltimoEn         = (CheckBox) findViewById(R.id.checkBox_UltimoEn);
        txt_oc                 = (TextView)findViewById(R.id.txt_oc );
        txt_Envio         = (TextView) findViewById(R.id.txt_Envio);
        spinner_Cana = (Spinner) findViewById(R.id.spinner_Cana);


        classDescargarInicioApp = new ClassDescargarInicioApp(this);
        post     = new Httppostaux();
        dbhelper = new DatabaseHandler_(this);

        COD_QUINCENA 	= "1";//getIntent().getExtras().getString("COD_QUINCENA");
        FECHA_DIA 		= getFecha();//getIntent().getExtras().getString("FECHA_DIA");
        NOMBRE_DIA      = "";//getIntent().getExtras().getString("NOMBRE_DIA");
        ENV0_ID 		= "";//getIntent().getExtras().getString("ENV0_ID");

        tv_fecha_quin = (TextView)findViewById(R.id.tv_fecha_quin);
        tv_fecha_quin.setText(""+NOMBRE_DIA +" "+FECHA_DIA);

        tbl_btn_fecha_hora_quema = (TableLayout)findViewById(R.id.tbl_btn_fecha_hora_quema);


        if(tipoLista != 1){
            tipoLista = 2;
        }

        post=new Httppostaux();
        lv_envios = (ListView)findViewById(R.id.lv_envios);
        lv_envios.setAdapter(adpEnvios.adapterEnvios(ctx, "0", ""));

        lv_envios.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View vista,
                                    int posicion, long arg3) {
                HashMap<?, ?> itemList = (HashMap<?, ?>) lv_envios.getItemAtPosition(posicion);
                ENV0_ID = itemList.get("K_ENV0_ID").toString();
               txt_num_taco.setText(itemList.get("K_ENV0_ID").toString());
                txt_placa.setText(itemList.get("K_ENV1_PLACA").toString());
                txt_FsalIng.setText(itemList.get("K_ENV11_FSALING").toString());
                txt_oc.setText(itemList.get("K_ENV4_ORDENQUEMA").toString());
            
            }
        });
        spinner_Cana = (Spinner)findViewById(R.id.spinner_Cana);
        ArrayAdapter<String> spinnerCanaArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.Cana));
        spinner_Cana.setAdapter(spinnerCanaArrayAdapter);

        spinner_Cargadora = (Spinner)findViewById(R.id.spinner_Cargadora);
        ArrayAdapter<String> spinnerCargadoraArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.Cargadora));
        spinner_Cargadora.setAdapter(spinnerCargadoraArrayAdapter);

        spinner_Cargador = (Spinner)findViewById(R.id.spinner_Cargador);
        ArrayAdapter<String> spinnerCargadorArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.Cargador));
        spinner_Cargador.setAdapter(spinnerCargadorArrayAdapter);

        addTaco();
    }

    public boolean onQueryTextChange(String newText) {

            estadoBotonMenu = 1;
            lv_envios.setAdapter(null);
            lv_envios.setAdapter(adpEnvios.adapterEnvios(ctx, newText, ""));
            Log.i("home", "Button action_envios!S");

        return true;
    }

    public boolean onQueryTextSubmit(String text) {

            estadoBotonMenu = 1;
            lv_envios.setAdapter(null);
            lv_envios.setAdapter(adpEnvios.adapterEnvios(ctx, text, ""));
            Log.i("home", "Button action_envios!S");


        return true;
    }

    public String getFecha (){
        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());

        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss a");
        String formattedDate = df.format(c.getTime());
        return formattedDate;

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.act_lista_envios, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        mSearchView = (SearchView) searchItem.getActionView();
        mSearchView.setQueryHint("Search...");
        mSearchView.setOnQueryTextListener(this);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case android.R.id.home:
                intentCancel();
                Log.i("home", "Button home!S");
                return true;
            case R.id.action_envios_descargar:
                addTaco();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public  void metodoIniciarDescargaEnvios(){
        Log.i("settings", "Button settings descargar un taco en particular");
        estadoBotonMenu = 1;
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Descarga de envios....");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        new MiTareaAsincronaDialog(this,0, 1).execute();
    }

    public  void metodoRespaldarEnvios(){
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Descarga de envios....");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        new MiTareaAsincronaDialog(this,0, 2).execute();
    }

    public void addTaco(){
        Log.i("ADD NUMERO DE TACO", " 1 ");

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        final EditText txtNumTaco = Config.txtCapturaNumDecimal("Numero Taco","", this);

        Log.i("ADD NUMERO DE TACO", " 2 ");

        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        layout.addView(txtNumTaco);
        builder.setView(layout);

        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {


            public void onClick(DialogInterface dialog, int which) {
                TacoNumero = txtNumTaco.getText().toString();

               // txt_num_taco.setText(txtNumTaco.getText().toString());
                metodoIniciarDescargaEnvios();

            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                //No hacemos nada..
            }
        });

        builder.setTitle("Digite el numero de taco");
        builder.show();

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        switch(keyCode){
            case KeyEvent.KEYCODE_BACK:
                intentCancel();

                return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void intentRESULT_OK(){
        Intent i= getIntent();
        this.setResult(this.RESULT_OK, i);
        finish();
        Intent e= new Intent(this, EnviosRealizados.class);
        startActivity(e);
    }

    public void intentRESULT_FALLO(){
        Intent i= getIntent();
        i.putExtra("ENV0_ID", ENV0_ID);
        this.setResult(this.RESULT_CANCELED, i);
        finish();
        Intent e= new Intent(this, EnviosRealizados.class);
        startActivity(e);
    }


    public void intentCancel(){
        Intent i= getIntent();

        i.putExtra("COD_QUINCENA" , COD_QUINCENA);
        i.putExtra("ID_CUADRILLA" , COD_CUADRILLA);
        i.putExtra("FECHA_DIA"	  , FECHA_DIA);
        i.putExtra("ENV0_ID"	  , ENV0_ID);

        this.setResult(this.RESULT_CANCELED, i);
        finish();
    }

    //clase que se ejecuta mientras se ejecuta la consulta el web service y el  array
    private class MiTareaAsincronaDialog extends AsyncTask<Void, Integer, Boolean> {

        Context ctx;
        int resulCountTabla;
        int numActividad;
        //numActividad numero de actividad
        public MiTareaAsincronaDialog(Context ctx, int resulCountTabla, int numActividad)
        {
            this.ctx = ctx;
            this.resulCountTabla = resulCountTabla;
            this.numActividad = numActividad;

        }

        @Override
        protected Boolean doInBackground(Void... params)
        {
            switch (numActividad) {

                case 1:
                        if (classDescargarInicioApp.ws_bajarListadoEnvios(validarCampoVacio(TacoNumero), "0")
                                ){
                            return true;
                        }else{
                            if(isCancelled())
                            {
                                Log.d("Login: ", "doInBackground, Actividad cancelada TRUE");
                                return false;
                            }
                            Log.d("Login: ", "doInBackground, Actividad cancelada FALSE");

                            return false;
                        }
                case 2:
                    if (classDescargarInicioApp.ws_guardarNuevoMovimientoEnvio("0")){
                        return true;
                    }else{
                        if(isCancelled())
                        {
                            Log.d("Login: ", "doInBackground, Actividad cancelada TRUE");
                            return false;
                        }
                        Log.d("Login: ", "doInBackground, Actividad cancelada FALSE");

                        return false;
                    }


                default:
                    return false;
            }
        }

        @Override
        protected void onProgressUpdate(Integer... values)
        {
            int progreso = values[0].intValue();

            pDialog.setProgress(progreso);
        }

        @Override
        protected void onPreExecute()
        {
            pDialog.setOnCancelListener(new DialogInterface.OnCancelListener()
            {
                @Override
                public void onCancel(DialogInterface dialog) {
                    MiTareaAsincronaDialog.this.cancel(true);
                }
            });
            pDialog.setProgress(0);
            pDialog.setMax(5000);
            pDialog.show();
        }

        @Override
        protected void onPostExecute(Boolean result)
        {
            if(result)
            {
                pDialog.dismiss();
                Toast.makeText(this.ctx, "ACTIVIDAD CORRECTA!", Toast.LENGTH_LONG).show();
                Log.d("Login =", "onPostExecute, BIEN Tarea de descarga de secuencia finalizada exitosamente");
                lv_envios.setAdapter(adpEnvios.adapterEnvios(ctx, "0", ""));
                switch (numActividad) {

                    case 2:
                        intentRESULT_OK();
                }

            }else{
                pDialog.dismiss();
                Toast.makeText(this.ctx, "No se pudo completar la operacion", Toast.LENGTH_LONG).show();
                Log.d("Login =", "onPostExecute, ERROR Tarea de descarga no fue completada exitosamente");
                lv_envios.setAdapter(adpEnvios.adapterEnvios(ctx, "0", ""));
                switch (numActividad) {
                    case 2:
                        intentRESULT_FALLO();
                }
            }
        }

        @Override
        protected void onCancelled() {
            Toast.makeText(this.ctx, "Tarea cancelada!",
                    Toast.LENGTH_SHORT).show();

        }
    }
    public String validarCampoVacio(String valor){
        if (valor.equals(""))
            valor = "0";
        return valor;
    }

    public boolean setInsertDespachoCombustible(
            String K_DESP1_ENVIO,
            String K_DESP2_CARGADORA,
            String K_DESP3_CARGADOR,
            String K_DESP4_NOMBRECARD,
            String K_DESP5_FCORTA,
            String K_DESP6_HCORTA,
            String K_DESP7_FLLEGA,
            String K_DESP8_HLLEGA,
            String K_DESP9_FCARGA,
            String K_DESP10_HCARGA,
            String K_DESP11_FSALIDA,
            String K_DESP12_HSALIDA,
            String K_DESP13_OBSERV,
            String K_DESP14_ULTIMOENV,
            String K_DESP16_OC,
            String K_DESP17_NOENVIO,
            String K_DESP18_TIPCANA,
            String K_DESP19_PLACA)

            {
        try {
            SQLiteDatabase db;
            DatabaseHandler_ dbhelper = new DatabaseHandler_(ctx);
            db = dbhelper.getReadableDatabase();
            String query = "INSERT INTO " + dbhelper.TABLE_DESPACHO_ENVIOS + " ( "
                    + dbhelper.K_DESP1_ENVIO             + " , "
                    + dbhelper.K_DESP2_CARGADORA         + " , "
                    + dbhelper.K_DESP3_CARGADOR          + " , "
                    + dbhelper.K_DESP4_NOMBRECARD       + " , "
                    + dbhelper.K_DESP5_FCORTA            + " , "
                    + dbhelper.K_DESP6_HCORTA            + " , "
                    + dbhelper.K_DESP7_FLLEGA            + " , "
                    + dbhelper.K_DESP8_HLLEGA            + " , "
                    + dbhelper.K_DESP9_FCARGA            + " , "
                    + dbhelper.K_DESP10_HCARGA            + " , "
                    + dbhelper.K_DESP11_FSALIDA          + " , "
                    + dbhelper.K_DESP12_HSALIDA          + " , "
                    + dbhelper.K_DESP13_OBSERV           + " , "
                    + dbhelper.K_DESP14_ULTIMOENV        + " , "
                    + dbhelper.K_DESP15_LLAVE         + " , "
                    + dbhelper.K_DESP16_OC           + " ,  "
                    + dbhelper.K_DESP17_NOENVIO      + " ,  "
                    + dbhelper.K_DESP18_TIPCANA      + " ,  "
                    + dbhelper.K_DESP19_PLACA        + "   "

                    + " ) VALUES ( "
                    + " '" + K_DESP1_ENVIO           + "' , "
                    + " '" + K_DESP2_CARGADORA        + "' , "
                    + " '" + K_DESP3_CARGADOR         + "' , "
                    + " '" + K_DESP4_NOMBRECARD         + "' , "
                    + " '" + K_DESP5_FCORTA         + "' , "
                    + " '" + K_DESP6_HCORTA         + "' , "
                    + " '" + K_DESP7_FLLEGA          + "' , "
                    + " '" + K_DESP8_HLLEGA          + "' , "
                    + " '" + K_DESP9_FCARGA          + "' , "
                    + " '" + K_DESP10_HCARGA          + "' , "
                    + " '" + K_DESP11_FSALIDA         + "' , "
                    + " '" + K_DESP12_HSALIDA         + "' , "
                    + " '" + K_DESP13_OBSERV      + "' , "
                    + " '" + K_DESP14_ULTIMOENV     + "' , "
                    + " '0'  , "
                    + " '" + K_DESP16_OC        + "' , "
                    + " '" + K_DESP17_NOENVIO   + "' , "
                    + " '" + K_DESP18_TIPCANA   + "' , "
                    + " '" + K_DESP19_PLACA     + "'  "

                    +" ) "
                    ;
            db.execSQL(query);
            db.close();
            Log.e("MI QUERY", "Valor:" + query);
            return true;
        }catch( Exception e){
            Log.e("Exception", "Exception: "+e);
            return false;

        }
    }

    public static boolean verificaConexion(Context ctx) {
        boolean bConectado = false;
        ConnectivityManager connec = (ConnectivityManager) ctx
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        // No sólo wifi, también GPRS
        NetworkInfo[] redes = connec.getAllNetworkInfo();
        // este bucle debería no ser tan mapa
        for (int i = 0; i < 2; i++) {
            // Tenemos conexión? ponemos a true
            if (redes[i].getState() == NetworkInfo.State.CONNECTED) {
                bConectado = true;
            }
        }
        return bConectado;
    }

    public void MostrarCalendario(final String IDSECUENCIA, final int tipoOperacion)
    {
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        // FECHA PICKER
        DatePickerDialog dpd = new DatePickerDialog(ctx,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        CharSequence strDate = null;
                        Time chosenDate = new Time();
                        chosenDate.set(dayOfMonth, monthOfYear, year);
                        long dtDob = chosenDate.toMillis(true);

                        strDate = DateFormat.format("dd-MM-yyyy", dtDob);

                        fechaNewAplicacion = strDate.toString();

                        final Calendar c = Calendar.getInstance();

                    }
                }, mYear, mMonth, mDay);
        dpd.show();
    }

}
