package com.example.gestionlocationnew;

        import android.app.Service;
        import android.content.BroadcastReceiver;
        import android.content.Context;
        import android.content.Intent;
        import android.database.Cursor;
        import android.database.sqlite.SQLiteDatabase;
        import android.os.AsyncTask;
        import android.os.Bundle;
        import android.os.Handler;
        import android.os.IBinder;
        import android.os.Looper;
        import android.text.TextUtils;
        import android.util.Log;
        import android.view.View;
        import android.widget.Toast;

        import com.google.android.material.snackbar.Snackbar;
        import com.vishnusivadas.advanced_httpurlconnection.PutData;

        import org.json.JSONArray;
        import org.json.JSONException;
        import org.json.JSONObject;

        import java.net.URL;
        import java.sql.Timestamp;

public class service_vehicule extends Service {

    gestion_location db;
    Cursor dbCursor;
    int nbLocal,nbGlobal;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {




        db= new gestion_location(this);



        Bundle extras = intent.getExtras();
        String Login = (String) extras.get("Login");
        SQLiteDatabase table = db.getReadableDatabase ();
        String requet = "select count(*) from véhicules where  login ='"+Login+"'";
        dbCursor = table.rawQuery ( requet, null );
        while (true){

            /**
             * check server first
             */

            //Start ProgressBar first (Set visibility VISIBLE)
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    //Starting Write and Read data with URL
                    //Creating array for parameters
                    String[] field = new String[1];
                    field[0] = "Login";

                    //Creating array for data
                    String[] data = new String[1];
                    data[0] = Login;

                    String host = getResources().getString(R.string.hosting);
                    PutData putData = new PutData(host+"/gesloc/vehicules/nbVehicule.php", "POST", field, data);
                    if (putData.startPut()) {
                        if (putData.onComplete()) {
                            String result = putData.getResult();

                            if(!result.equals("error") || !result.equals("Error: Database connection") || !result.equals("All fields are required")) {


                                nbGlobal = Integer.parseInt(result);
                                if(nbGlobal !=0){

                                    /**
                                     * compare Mysql to Sqlite
                                     */
                                    if (dbCursor.getCount() > 0) {
                                        while (dbCursor.moveToNext()) {
                                            nbLocal = Integer.parseInt(dbCursor.getString(0));
                                        }

                                        /**
                                         * nbGlobal > nbLocal // if count database mysql bigger than count database SQLite
                                         */
                                        if(nbGlobal > nbLocal){

                                            /**
                                             * sync data to local
                                             */
                                            syncDatabase(Login);

                                        }


                                        /**
                                         * nbGlobal <= nbLocal // if count database mysql smaller than count database SQLite
                                         */
                                        if(nbGlobal <= nbLocal){

                                            /**
                                             * sync data to local
                                             */
                                            sync_Deleted_Database(Login);

                                        }



                                    } else {

                                        /**
                                         * get all data to local
                                         */
                                        syncDatabase(Login);
                                    }


                                }

                            }else {
                                Toast.makeText(service_vehicule.this, result, Toast.LENGTH_SHORT).show();

                                Log.i("message hosting",result);
                            }
                        }
                    }
                    //End Write and Read data with URL
                }
            });
            return START_STICKY;
        }
        //SyncTask myTask= new SyncTask();
        //start asynctask
        //myTask.execute(Login);




    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "Service destroyed by user.", Toast.LENGTH_LONG).show();
    }





    class SyncTask extends AsyncTask<String,String,Boolean>
    {


        @Override
        protected void onPreExecute()
        {

        }
        @Override
        protected Boolean doInBackground(String... params)
        {
            return false;
        }
        @Override
        protected void onProgressUpdate(String... progress)
        {


        }
        @Override
        protected void onPostExecute(Boolean checker1)
        {


        }
    }


    public void sync_Deleted_Database(String Login){

        SQLiteDatabase table = db.getReadableDatabase ();
        String requet = "select * from véhicules where login = '"+Login+"'";
        Cursor c = table.rawQuery ( requet, null );

        while (c.moveToNext()){
            checkVehicules_for_delete(c.getString(2),Login);
        }

        syncDatabase(Login);
    }

    private void checkVehicules_for_delete(String matricule, String login) {

        //Start ProgressBar first (Set visibility VISIBLE)
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                //Starting Write and Read data with URL
                //Creating array for parameters
                String[] field = new String[2];
                field[0] = "Login";
                field[1] = "immatriculation";

                //Creating array for data
                String[] data = new String[2];
                data[0] = login;
                data[1] = matricule;


                String host = getResources().getString(R.string.hosting);
                PutData putData = new PutData(host+"/gesloc/vehicules/getNBVehicules.php", "POST", field, data);
                if (putData.startPut()) {
                    if (putData.onComplete()) {
                        String result = putData.getResult();



                        if(!result.equals("Error: Database connection") || !result.equals("All fields are required") || !result.equals("error")){

                            /**
                             * delete database to sqlite
                             */
                            Log.i("data deleted",result);
                            if(result.equals("0")){
                                db.suprimer_vihucle(matricule,login);
                            }else {
                                //syncDatabase(login);
                            }



                        }else {
                            Toast.makeText(service_vehicule.this, result, Toast.LENGTH_SHORT).show();
                        }


                    }
                }
                //End Write and Read data with URL
            }
        });

    }




    public void syncDatabase(String Login){

        //Start ProgressBar first (Set visibility VISIBLE)
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                //Starting Write and Read data with URL
                //Creating array for parameters
                String[] field = new String[1];
                field[0] = "Login";

                //Creating array for data
                String[] data = new String[1];
                data[0] = Login;


                String host = getResources().getString(R.string.hosting);
                PutData putData = new PutData(host+"/gesloc/vehicules/getVehicules.php", "POST", field, data);
                if (putData.startPut()) {
                    if (putData.onComplete()) {
                        String result = putData.getResult();


                        if(!result.equals("Error: Database connection") || !result.equals("All fields are required") || !result.equals("error")){

                            /**
                             * add all database to sqlite
                             */
                            try {
                                Log.i("Json",""+result);
                                GetUserJson(result,db);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }else {
                            Toast.makeText(service_vehicule.this, result, Toast.LENGTH_SHORT).show();
                        }


                    }
                }
                //End Write and Read data with URL
            }
        });


    }



    private void GetUserJson(String json,gestion_location db) throws JSONException {
        JSONArray jsonArray = new JSONArray(json);
        String[] webchrz = new String[jsonArray.length()];
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        for (int i = 0; i < jsonArray.length(); i++) {

            JSONObject obj = jsonArray.getJSONObject(i);

           Boolean result_insert = db.insert_vehiucle(obj.getString("MarqueVihicule"), obj.getString("DateCirculation"), obj.getString("immatriculation"), obj.getString("MarqueCombustion"), Integer.parseInt(obj.getString("ValeurDentrée")), obj.getString("Date_Effet_Assurance"), obj.getString("Date_Echeance"), Integer.parseInt(obj.getString("Couleur_Vehicule")),obj.getString("Login"));
            if(!result_insert){
                db.modifier_vihucle(obj.getString("MarqueVihicule"), obj.getString("DateCirculation"), obj.getString("immatriculation"), obj.getString("MarqueCombustion"), Integer.parseInt(obj.getString("ValeurDentrée")), obj.getString("Date_Effet_Assurance"), obj.getString("Date_Echeance"), Integer.parseInt(obj.getString("Couleur_Vehicule")), obj.getString("Login"), timestamp);

                Log.i("update done","#################    "+obj.getString("MarqueVihicule"));
            }else {
                Log.i("ajoute done","#################    "+obj.getString("MarqueVihicule"));
            }
        }

    }






}