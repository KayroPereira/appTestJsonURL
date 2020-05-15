package com.example.apptestjsonurl;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

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

public class MainActivity extends AppCompatActivity {
    private TextView tvDados;
    private Button btUpdate;

    private int cont;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cont = 0;
        tvDados = (TextView) findViewById(R.id.tvDados);
        btUpdate = (Button) findViewById((R.id.btUpdate));

        btUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //realiza a busca das informações do clima na cidade de Jaboatão dos Guararapes - PE
                new DownloadJsonAsyncTask().execute("https://api.hgbrasil.com/weather?array_limit=3&fields=only_results,humidity,temp,city_name,forecast,condition,weekday,max,min,date&key=bdda2060&lat=-8.114&log=-35.017&user_ip=remote");
                cont++;
                tvDados.setText("Contagem: "+cont+"\n" + tvDados.getText());
            }
        });
    }

    private class DownloadJsonAsyncTask extends AsyncTask<String, Void, Clima> {
        ProgressDialog dialog;

        //Exibe pop-up indicando que está sendo feito o download do JSON
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = ProgressDialog.show(MainActivity.this, "Aguarde",
                    "Fazendo download do JSON");
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
            dialog.dismiss();
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
                tvDados.setText(temp);
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(
                        MainActivity.this)
                        .setTitle("Erro")
                        .setMessage("Não foi possível acessar as informações!!")
                        .setPositiveButton("OK", null);
                builder.create().show();
            }
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
}
