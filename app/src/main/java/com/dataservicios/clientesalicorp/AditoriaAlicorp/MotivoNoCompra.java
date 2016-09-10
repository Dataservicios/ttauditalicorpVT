package com.dataservicios.clientesalicorp.AditoriaAlicorp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
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
public class MotivoNoCompra extends Activity {
    private Activity MyActivity = this ;
    private static final String LOG_TAG = MotivoNoCompra.class.getSimpleName();
    private SessionManager session;


    private Button  bt_guardar;
    private EditText et_Comentario,etComent1;
    private TextView tv_Pregunta;


    private String tipo,cadenaruc, fechaRuta, comentario="", type, region;

    private Integer user_id, company_id,store_id,rout_id,audit_id, product_id, poll_id, poll_id2;

    private DatabaseHelper db;

    private ProgressDialog pDialog;

    private RadioGroup rgOpt1;
    private String opt1="";

    private RadioButton[] radioButton1Array;

    private PollDetail mPollDetail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.motivo_no_compra);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setTitle("Motivo no compra");




        //lyAuditoria = (LinearLayout) findViewById(R.id.lyAuditoria);

        rgOpt1=(RadioGroup) findViewById(R.id.rgOpt1);

        radioButton1Array = new RadioButton[] {
                (RadioButton) findViewById(R.id.rbA1),
                (RadioButton) findViewById(R.id.rbB1),
                (RadioButton) findViewById(R.id.rbC1),
                (RadioButton) findViewById(R.id.rbD1),
                (RadioButton) findViewById(R.id.rbE1),
                (RadioButton) findViewById(R.id.rbF1),
                (RadioButton) findViewById(R.id.rbG1),
        };


        tv_Pregunta = (TextView) findViewById(R.id.tvPregunta);
        bt_guardar = (Button) findViewById(R.id.btGuardar);

        //et_Comentario = (EditText) findViewById(R.id.etComentario);
        etComent1 = (EditText) findViewById(R.id.etComent1);

        Bundle bundle = getIntent().getExtras();
        store_id = bundle.getInt("store_id");
        rout_id =  bundle.getInt("rout_id");
        fechaRuta = bundle.getString("fechaRuta");
        audit_id = bundle.getInt("audit_id");

        poll_id = 504;


        //poll_id = 72 , solo para exhibiciones de bayer, directo de la base de datos

        pDialog = new ProgressDialog(MyActivity);
        pDialog.setMessage("Cargando...");
        pDialog.setCancelable(false);

        session = new SessionManager(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        // id
        user_id = Integer.valueOf(user.get(SessionManager.KEY_ID_USER)) ;

        //tv_Pregunta.setText("Motivo no compra");



        rgOpt1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(radioButton1Array[5].isChecked())
                {
                    etComent1.setEnabled(true);
                    etComent1.setVisibility(View.VISIBLE);
                    etComent1.setText("");
                }
                else
                {
                    etComent1.setEnabled(false);
                    etComent1.setVisibility(View.INVISIBLE);
                    etComent1.setText("");
                }
            }
        });






        bt_guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                    long id1 = rgOpt1.getCheckedRadioButtonId();
                    if (id1 == -1){
                        Toast.makeText(MyActivity,"Seleccionar una opción " , Toast.LENGTH_LONG).show();
                        return;
                    }
                    else{
                        for (int x = 0; x < radioButton1Array.length; x++) {
                            if(id1 ==  radioButton1Array[x].getId())  {
                                opt1 = poll_id.toString() + radioButton1Array[x].getTag();
                                comentario = String.valueOf(etComent1.getText()) ;
                            }
                        }

                    }



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
                        mPollDetail.setSino(0);
                        mPollDetail.setOptions(1);
                        mPollDetail.setLimits(0);
                        mPollDetail.setMedia(0);
                        mPollDetail.setComment(0);
                        mPollDetail.setResult(0);
                        mPollDetail.setLimite(0);
                        mPollDetail.setComentario("");
                        mPollDetail.setAuditor(user_id);
                        mPollDetail.setProduct_id(0);
                        mPollDetail.setPublicity_id(0);
                        mPollDetail.setCompany_id(GlobalConstant.company_id);
                        mPollDetail.setCommentOptions(1);
                        mPollDetail.setSelectdOptions(opt1);
                        mPollDetail.setSelectedOtionsComment(comentario);
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
                // loadLoginActivity();
                Intent intent;
                Bundle argRuta = new Bundle();
                argRuta.clear();
                //argRuta.putInt("company_id", GlobalConstant.company_id);
                argRuta.putInt("store_id",store_id);
                argRuta.putInt("rout_id", rout_id);
                argRuta.putString("fechaRuta", fechaRuta);
                argRuta.putInt("audit_id", audit_id);

                intent = new Intent(MyActivity, Merchandising.class);
                intent.putExtras(argRuta);
                startActivity(intent);
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

    /**
     * Desactiva o activa todos los elementos del radiobutton
     * @param radioButtonArray Array de RadioButton
     * @param checked parametro  en cheked
     */
    private void clearRadioButtonCheck(RadioButton[] radioButtonArray, boolean checked){
        for (int x = 0; x < radioButtonArray.length; x++) {
            radioButtonArray[x].setChecked(checked);
        }
    }

    /**
     * Metodo evalua si esta marcado alguna opción de un radio button
     * @param radioButtonArray
     * @return
     */
    private boolean evaluateOptionRadioButton(RadioButton[] radioButtonArray){
        boolean status= true;
        for (int x = 0; x < radioButtonArray.length; x++) {
            if(!radioButtonArray[x].isChecked())  {
                status = false ;
                break;
            }
        }
        return status;
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
