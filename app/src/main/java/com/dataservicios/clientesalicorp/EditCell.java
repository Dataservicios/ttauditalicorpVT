package com.dataservicios.clientesalicorp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.dataservicios.clientesalicorp.Model.Pdv;
import com.dataservicios.clientesalicorp.Model.PollDetail;
import com.dataservicios.clientesalicorp.SQLite.DatabaseHelper;
import com.dataservicios.clientesalicorp.util.AuditAlicorp;
import com.dataservicios.clientesalicorp.util.GlobalConstant;
import com.dataservicios.clientesalicorp.util.SessionManager;

import java.util.HashMap;

/**
 * Created by Jaime on 6/09/2016.
 */
public class EditCell extends Activity {
    private static final String LOG_TAG = EditTelephone.class.getSimpleName();
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    private Integer store_id,   user_id ;
    private Button btGuardar, btCancelar;
    private DatabaseHelper db ;
    private Activity MyActivity = this ;
    private String direccion,referencia, userEmail, storeName, userName, telephone, cell;
    private TextView tvCategoria;
    private EditText  etCell;
    private ProgressDialog pDialog;
    private SessionManager session;
    private Switch swSiNo ;
    private LinearLayout lyEdition ;
    private int is_sino , poll_id;
    private PollDetail mPollDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_cell);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        etCell = (EditText) findViewById(R.id.etCell);
        //etReferencia = (EditText) findViewById(R.id.etReferencia);
        btGuardar = (Button) findViewById(R.id.btGuardar);
        btCancelar = (Button) findViewById(R.id.btCancelar);
        swSiNo = (Switch) findViewById(R.id.swSiNo);
        lyEdition = (LinearLayout) findViewById(R.id.lyEdition);

        db = new DatabaseHelper(getApplicationContext());

        session = new SessionManager(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        // id
        user_id = Integer.valueOf(user.get(SessionManager.KEY_ID_USER)) ;
        userEmail = String.valueOf(user.get(SessionManager.KEY_EMAIL));
        userName = String.valueOf(user.get(SessionManager.KEY_NAME));


        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Cargando...");
        pDialog.setCancelable(false);

        Bundle bundle = getIntent().getExtras();
        store_id = bundle.getInt("store_id");
        cell = bundle.getString("cell");
        storeName = bundle.getString("storeName");
        poll_id = 502 ;

        etCell.setText(cell);
        //etDireccion.setText(direccion);


        swSiNo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    is_sino = 1;
                    lyEdition.setVisibility(View.INVISIBLE);
                    lyEdition.setEnabled(false);
                } else {
                    is_sino = 0;
                    lyEdition.setVisibility(View.VISIBLE);
                    lyEdition.setEnabled(true);
                }
            }
        });


        btGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cell=etCell.getText().toString();
                if(is_sino==0) {
                    if (cell.length() < 9) {
                        Toast.makeText(MyActivity,R.string.message_cell,Toast.LENGTH_LONG).show();
                        return ;
                    }
                }



                AlertDialog.Builder builder = new AlertDialog.Builder(MyActivity);
                builder.setTitle("Guardar Ventana");
                builder.setMessage("Está seguro de guardar todas los datos: ");
                builder.setPositiveButton("Si", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        cell = etCell.getText().toString() ;

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

        btCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MyActivity);
                builder.setTitle("Guardar Ventana");
                builder.setMessage("Está seguro que desea salir sin guardar ");
                builder.setPositiveButton("Si", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        finish();

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

            if(is_sino == 0){
                Pdv pdv = new Pdv();
                pdv.setId(store_id);
                pdv.setDireccion("");
                pdv.setReference("");
                pdv.setTelephone("");
                pdv.setCell(cell);
                if(!AuditAlicorp.updateDataStore(pdv)) return false;
            }

            return true;
        }
        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(Boolean result) {
            // dismiss the dialog once product deleted
            hidepDialog();
            if (result){

                finish();

            } else {
                Toast.makeText(MyActivity , "No se pudo guardar la información intentelo nuevamente", Toast.LENGTH_LONG).show();
            }

        }
    }


    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //preventing default implementation previous to android.os.Build.VERSION_CODES.ECLAIR
            //Toast.makeText(MyActivity, "No se puede volver atras, los datos ya fueron guardado, para modificar pongase en contácto con el administrador", Toast.LENGTH_LONG).show();
            //onBackPressed();
            return true;
        }
        return super.onKeyDown(keyCode, event);
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

