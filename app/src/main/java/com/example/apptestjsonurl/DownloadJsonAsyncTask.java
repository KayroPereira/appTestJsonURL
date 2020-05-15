package com.example.apptestjsonurl;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class DownloadJsonAsyncTask extends AsyncTask<String, Void, Clima> {
    //ProgressDialog dialog;

    public interface AsyncResponseJson {
        void processFinish(Clima result);
    }

    public AsyncResponseJson delegate = null;

    public DownloadJsonAsyncTask(AsyncResponseJson delegate){
        this.delegate = delegate;
    }

    //Exibe pop-up indicando que está sendo feito o download do JSON
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        //dialog = ProgressDialog.show(MainActivity.this, "Aguarde", "Fazendo download do JSON");
    }

    //Acessa o serviço do JSON e retorna o clima
    @Override
    protected Clima doInBackground(String... params) {
        HttpURLConnection connection = null;

        try {
            URL url = new URL(params[0]);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();

            InputStream stream = connection.getInputStream();

            String json = getStringFromInputStream(stream);
            stream.close();
            Clima clima = getClima(json);
            return clima;
        }catch (Exception e) {
            Log.e("Erro", "Falha ao acessar Web service", e);
        }
        return null;
    }

    //Depois de executada a chamada do serviço
    @Override
    protected void onPostExecute(Clima result) {
        super.onPostExecute(result);
        delegate.processFinish(result);
        /*
        ///dialog.dismiss();
        if (result != null ) {
            String temp = "";

            temp += "Cidade: " + result.getCityName()+"\n";
            temp += "Data: " + result.getDate() +"\n";
            temp += "Temperatura: " + result.getTemp() +"\n";
            temp += "Humidade: " + result.getHumidity() +"\n\n";

            for (Clima.ClimaDia tempClima : result.getClimaDia()){
                temp += "Data: " + tempClima.getDate() +"\n";
                temp += " Weekday: " + tempClima.getWeekday() +"\n";
                temp += " Max: " + tempClima.getMax() +"\n";
                temp += " Min: " + tempClima.getMin() +"\n";
                temp += " Condition: " + tempClima.getCondition() +"\n\n\n";
            }
            //tvDados.setText(temp);
        }/*
        else {
            AlertDialog.Builder builder = new AlertDialog.Builder(
                    MainActivity.this)
                    .setTitle("Erro")
                    .setMessage("Não foi possível acessar as informações!!")
                    .setPositiveButton("OK", null);
            builder.create().show();
        }*/
    }

    //retorna um objeto Clima com as informações dp JSON
    private Clima getClima(String jsonString) {
        Clima clima = new Clima();
        try {
            JSONObject objetoJson = new JSONObject(jsonString);
            clima.setTemp(objetoJson.getString("temp"));
            clima.setDate(objetoJson.getString("date"));
            clima.setHumidity(objetoJson.getString("humidity"));
            clima.setCityName(objetoJson.getString("city_name"));

            JSONArray arrayJson = objetoJson.getJSONArray("forecast");

            ArrayList<Clima.ClimaDia> dataClimaTemp = new ArrayList<>();
            for (int i = 0; i < arrayJson.length(); i++) {
                JSONObject dadoTemp =  arrayJson.getJSONObject(i);

                dataClimaTemp.add(new Clima.ClimaDia(dadoTemp.getString("date"), dadoTemp.getString("weekday"),
                        dadoTemp.getString("max"), dadoTemp.getString("min"), dadoTemp.getString("condition")));
            }
            clima.setClimaDia(dataClimaTemp);

        } catch (JSONException e) {
            Log.e("Erro", "Erro no parsing do JSON", e);
            return null;
        }
        return clima;
    }

    //Converte objeto InputStream para String
    private String getStringFromInputStream(InputStream is) {

        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();

        String line;
        try {
            br = new BufferedReader(new InputStreamReader(is));
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return sb.toString();
    }
}