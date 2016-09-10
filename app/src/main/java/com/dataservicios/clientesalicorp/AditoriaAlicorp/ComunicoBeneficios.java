package com.dataservicios.clientesalicorp.AditoriaAlicorp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.dataservicios.clientesalicorp.Model.Audit;
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
public class ComunicoBeneficios extends Activity {
    private Activity MyActivity = this ;
    private static final String LOG_TAG = ComunicoBeneficios.class.getSimpleName();
    private SessionManager session;

    private Switch swSiNo ;
    private Button bt_guardar;
    private EditText et_Comentario,etComent1;
    private TextView tv_Pregunta;


    private String fechaRuta,   type, region;

    private Integer user_id, company_id,store_id,rout_id,audit_id,  poll_id;

    int  is_sino=0 ;


    private DatabaseHelper db;

    private ProgressDialog pDialog;
    private PollDetail mPollDetail;
    private Audit mAudit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.comunico_beneficios);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setTitle("Tienda");

        swSiNo = (Switch) findViewById(R.id.swSiNo);


        tv_Pregunta = (TextView) findViewById(R.id.tvPregunta);
        bt_guardar = (Button) findViewById(R.id.btGuardar);


        etComent1 = (EditText) findViewById(R.id.etComent1);

        Bundle bundle = getIntent().getExtras();
        store_id = bundle.getInt("store_id");
        rout_id =  bundle.getInt("rout_id");
        fechaRuta = bundle.getString("fechaRuta");
        audit_id = bundle.getInt("audit_id");

        poll_id = 507;


        //poll_id = 72 , solo para exhibiciones de bayer, directo de la base de datos

        pDialog = new ProgressDialog(MyActivity);
        pDialog.setMessage("Cargando...");
        pDialog.setCancelable(false);

        session = new SessionManager(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        // id
        user_id = Integer.valueOf(user.get(SessionManager.KEY_ID_USER)) ;

//        tv_Pregunta.setText("¿Co?");





        swSiNo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    is_sino = 1;
                } else {
                    is_sino = 0;
                }
            }
        });


        bt_guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(MyActivity);
                builder.setTitle("Guardar Encuesta");
                builder.setMessage("Está seguro de guardar todas las encuestas: ");
                builder.setPositiveButton("Si", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        mPollDetail = new PollDetail();
                        mPollDetail.setPoll_id(poll_id);
                        mPollDetail.setStore_id(store_id);
                        mPollDetail.setSino(1);
                        mPollDetail.setOptions(0);
                        mPollDetail.setLimits(0);
                        mPollDetail.setMedia(0);
                        mPollDetail.setComment(0);
                        mPollDetail.setResult(is_sino);
                        mPollDetail.setLimite(0);
                        mPollDetail.setComentario("");
                        mPollDetail.setAuditor(user_id);
                        mPollDetail.setProduct_id(0);
                        mPollDetail.setPublicity_id(0);
                        mPollDetail.setCompany_id(GlobalConstant.company_id);
                        mPollDetail.setCommentOptions(0);
                        mPollDetail.setSelectdOptions("");
                        mPollDetail.setSelectedOtionsComment("");
                        mPollDetail.setPriority("0");

                        mAudit = new Audit();
                        mAudit.setCompany_id(GlobalConstant.company_id);
                        mAudit.setStore_id(store_id);
                        mAudit.setId(audit_id);
                        mAudit.setRoute_id(rout_id);

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
            if(!AuditAlicorp.closeAuditRoadStore(mAudit)) return false;


            return true;
        }
        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(Boolean result) {
            // dismiss the dialog once product deleted

            if (result){
                // loadLoginActivity();

                finish();


            } else {
                Toast.makeText(MyActivity , "No se pudo guardar la información intentelo nuevamente", Toast.LENGTH_LONG).show();
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




    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
        overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_right);
    }

    private void showpDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hidepDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

}


