package com.example.apptestjsonurl;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

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
                updateClima();
            }
        });
    }

    private void updateClima(){
        //realiza a busca das informações do clima na cidade de Jaboatão dos Guararapes - PE
        DownloadJsonAsyncTask downloadJson = new DownloadJsonAsyncTask(new DownloadJsonAsyncTask.AsyncResponseJson() {
            @Override
            public void processFinish(Clima result) {
                if (result != null ) {
                    String temp = "";

                    temp += "Cidade: " + result.getCityName() + "\n";
                    temp += "Data: " + result.getDate() + "\n";
                    temp += "Temperatura: " + result.getTemp() + "\n";
                    temp += "Humidade: " + result.getHumidity() + "\n\n";

                    for (Clima.ClimaDia tempClima : result.getClimaDia()) {
                        temp += "Data: " + tempClima.getDate() + "\n";
                        temp += " Weekday: " + tempClima.getWeekday() + "\n";
                        temp += " Max: " + tempClima.getMax() + "\n";
                        temp += " Min: " + tempClima.getMin() + "\n";
                        temp += " Condition: " + tempClima.getCondition() + "\n\n\n";
                    }
                    cont++;
                    temp = "Contagem: " + cont + "\n\n" + temp;
                    tvDados.setText(temp);
                }else{
                    tvDados.setText("Contagem: " + cont + "\n\n" + "Erro ao obter os dados");
                }
            }
        });
        downloadJson.execute("https://api.hgbrasil.com/weather?array_limit=3&fields=only_results,humidity,temp,city_name,forecast,condition,weekday,max,min,date&key=bdda2060&lat=-8.114&log=-35.017&user_ip=remote");
    }
}
