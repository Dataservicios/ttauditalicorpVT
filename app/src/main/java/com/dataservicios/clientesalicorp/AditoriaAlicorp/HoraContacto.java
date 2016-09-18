package com.dataservicios.clientesalicorp.AditoriaAlicorp;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.dataservicios.clientesalicorp.Model.PollDetail;
import com.dataservicios.clientesalicorp.R;
import com.dataservicios.clientesalicorp.SQLite.DatabaseHelper;
import com.dataservicios.clientesalicorp.util.AuditAlicorp;
import com.dataservicios.clientesalicorp.util.GlobalConstant;
import com.dataservicios.clientesalicorp.util.SessionManager;



import java.util.HashMap;

/**
 * Created by Jaime on 5/09/2016.
 */
public class HoraContacto extends Activity {

    private static final String LOG_TAG = HoraContacto.class.getSimpleName();
    private Integer store_id, rout_id,audit_id , user_id ,poll_id,result;
    private Button btGuardar;
    private DatabaseHelper db ;
    private Activity MyActivity = this ;
    private String fechaRuta;

    private ProgressDialog pDialog;
    private SessionManager session;
    private PollDetail mPollDetail;
    private EditText etDesdeTime, etHastaTime ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hora_contacto);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setTitle("Hora contácto");

        db = new DatabaseHelper(getApplicationContext());


        session = new SessionManager(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        // id
        user_id = Integer.valueOf(user.get(SessionManager.KEY_ID_USER)) ;

        poll_id = 503;

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Cargando...");
        pDialog.setCancelable(false);

        Bundle bundle = getIntent().getExtras();
        store_id = bundle.getInt("store_id");
        rout_id =  bundle.getInt("rout_id");
        fechaRuta = bundle.getString("fechaRuta");
        audit_id = bundle.getInt("audit_id");
        btGuardar = (Button) findViewById(R.id.btGuardar);


         etDesdeTime = (EditText) findViewById(R.id.etDesdeTime);
         etHastaTime = (EditText) findViewById(R.id.etHastaTime);



        btGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (etDesdeTime.getText().equals("")) {
                    Toast.makeText(MyActivity, "Ingrese la Hora",Toast.LENGTH_LONG).show();
                    return;
                }

                if (etHastaTime.getText().equals("")) {
                    Toast.makeText(MyActivity, "Ingrese la Hora",Toast.LENGTH_LONG).show();
                    return;
                }



                AlertDialog.Builder builder = new AlertDialog.Builder(MyActivity);
                builder.setTitle("Guardar Ventana");
                builder.setMessage("Está seguro de guardar todas los datos: ");
                builder.setPositiveButton("Si", new DialogInterface.OnClickListener()

                {
                    @TargetApi(Build.VERSION_CODES.M)
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

//                        TimePicker timePicker = (TimePicker)findViewById(R.id.timePicker);
//                        TimePicker timePicker2 = (TimePicker)findViewById(R.id.timePicker2);







//                        Integer hora = timePicker.getHour();
//                        Integer minute = timePicker.getMinute();


                        Integer horaDesde  ;
                        Integer minuteDesde  ;
                        Integer horaHasta  ;
                        Integer minuteHasta  ;
                        StringBuilder sb=new StringBuilder();
                        String dobStr ;
                        String am_pm_desde;
                        String am_pm_hasta;
//
//                        if(Build.VERSION.SDK_INT > 21) {
//
//                            horaDesde = timePicker.getHour();
//                            minuteDesde = timePicker.getMinute();
//                            horaHasta = timePicker2.getHour();
//                            minuteHasta = timePicker2.getMinute();
//                            if (horaDesde < 12 ) {
//                                if (horaDesde == 0) horaDesde = 12;
//                                am_pm_desde = "AM";
//                            }
//                            else {
//                                if (horaDesde != 12)
//                                    horaDesde-=12;
//                                am_pm_desde = "PM";
//                            }
//
//                            if (horaHasta < 12 ) {
//                                if (horaHasta == 0) horaHasta = 12;
//                                am_pm_hasta = "AM";
//                            }
//                            else {
//                                if (horaHasta != 12)
//                                    horaHasta-=12;
//                                    am_pm_hasta = "PM";
//                            }
//                            //Integer dobDate = timePicker.get();
//
//                            sb.append(horaDesde.toString()).append(":").append(minuteDesde.toString()).append(" ").append(am_pm_desde).append(" - ").append(horaHasta.toString()).append(":").append(minuteHasta.toString()).append(" ").append(am_pm_hasta);
//                            dobStr=sb.toString();
//
//                        } else {
//
//                            horaDesde = timePicker2.getCurrentHour();
//                            minuteDesde = timePicker.getCurrentMinute();
//
//                            horaHasta = timePicker.getCurrentHour();
//                            minuteHasta = timePicker2.getCurrentMinute();
//
//                            sb.append(horaDesde.toString()).append(":").append(minuteDesde.toString()).append(" - ").append(horaHasta.toString()).append(":").append(minuteHasta.toString());
//                            dobStr=sb.toString();
//                        }






                        dobStr = etDesdeTime.getText().toString() +  " - " + etHastaTime.getText().toString();

                        Toast toast;
                        toast = Toast.makeText(MyActivity, dobStr , Toast.LENGTH_LONG);
                        toast.show();


                        mPollDetail = new PollDetail();
                        mPollDetail.setPoll_id(poll_id);
                        mPollDetail.setStore_id(store_id);
                        mPollDetail.setSino(0);
                        mPollDetail.setOptions(0);
                        mPollDetail.setLimits(0);
                        mPollDetail.setMedia(0);
                        mPollDetail.setComment(1);
                        mPollDetail.setResult(0);
                        mPollDetail.setLimite(0);
                        mPollDetail.setComentario(dobStr);
                        mPollDetail.setAuditor(user_id);
                        mPollDetail.setProduct_id(0);
                        mPollDetail.setPublicity_id(0);
                        mPollDetail.setCompany_id(GlobalConstant.company_id);
                        mPollDetail.setCommentOptions(0);
                        mPollDetail.setSelectdOptions("");
                        mPollDetail.setSelectedOtionsComment("");
                        mPollDetail.setPriority("0");
                        new loadPoll().execute(mPollDetail);

                        dialog.dismiss();

                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                builder.show();
                builder.setCancelable(false);

            }
        });


    }


    class loadPoll extends AsyncTask<PollDetail, Integer , Boolean> {
        /**
         * Antes de comenzar en el hilo determinado, Mostrar progresión
         * */
        boolean failure = false;
        @Override
        protected void onPreExecute() {
            //tvCargando.setText("Cargando Product...");
            pDialog.show();
            super.onPreExecute();
        }
        @Override
        protected Boolean doInBackground(PollDetail... params) {
            // TODO Auto-generated method stub

            PollDetail mPD = params[0] ;

            if(!AuditAlicorp.insertPollDetail(mPD)) return false;

            return true;
        }
        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(Boolean result) {
            // dismiss the dialog once product deleted
            if (result){


                    Intent intent;
                    Bundle argRuta = new Bundle();
                    argRuta.clear();
                    //argRuta.putInt("company_id", GlobalConstant.company_id);
                    argRuta.putInt("store_id",store_id);
                    argRuta.putInt("rout_id", rout_id);
                    argRuta.putString("fechaRuta", fechaRuta);
                    argRuta.putInt("audit_id", audit_id);

                    intent = new Intent(MyActivity, MotivoNoCompra.class);
                    intent.putExtras(argRuta);
                    startActivity(intent);
                    finish();

            } else {
                Toast.makeText(MyActivity , "No se pudo guardar la información intentelo nuevamente",Toast.LENGTH_LONG).show();
            }

            hidepDialog();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
//                this.finish();
//                Intent a = new Intent(this,PanelAdmin.class);
//                //a.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(a);
//                overridePendingTransition(R.anim.anim_slide_in_right,R.anim.anim_slide_out_right);
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
        //return super.onOptionsItemSelected(item);
    }



//    public void onBackPressed() {
//        super.onBackPressed();
//        this.finish();
//        overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_right);
//    }

    private void showpDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hidepDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //preventing default implementation previous to android.os.Build.VERSION_CODES.ECLAIR
            //Toast.makeText(MyActivity, "No se puede volver atras, los datos ya fueron guardado, para modificar pongase en contácto con el administrador", Toast.LENGTH_LONG).show();
            onBackPressed();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    @Override
    public void onBackPressed() {
        Toast.makeText(MyActivity, "No se puede volver atras, los datos ya fueron guardado, para modificar póngase en contácto con el administrador", Toast.LENGTH_LONG).show();
//        super.onBackPressed();
//        this.finish();
//
//        overridePendingTransition(R.anim.anim_slide_in_right,R.anim.anim_slide_out_right);
    }

}
