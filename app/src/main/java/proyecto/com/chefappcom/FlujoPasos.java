package proyecto.com.chefappcom;

/**
 * Created by alfredo on 25/10/16.
 */

import android.os.AsyncTask;
import android.os.Bundle;


import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;




public class FlujoPasos extends AppCompatActivity {

    List<String> pasosReceta = new ArrayList<String>();
    private List<String> pasos;

    private TextView tv1,vista;
    private ListView lv1;
    private int remove;
    private double time;
    private ArrayAdapter <String> ad;
    private String nombreC="";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.flujo_pasos);

        final Button terminar = (Button) findViewById(R.id.Terminar);
        terminar.setVisibility(View.INVISIBLE);
        tv1=(TextView)findViewById(R.id.Pasos_Receta);
        vista=(TextView)findViewById(R.id.vista);
        vista.setMovementMethod(new ScrollingMovementMethod());
        vista.setVisibility(View.INVISIBLE);
        lv1 =(ListView)findViewById(R.id.Pasos);
        lv1.setVisibility(View.GONE);

        Bundle extras=getIntent().getExtras();
        if(extras!=null){
            nombreC=extras.getString("nombreC");
        }
        try {
            new ObtenerOrdenes().execute(new URL("http://192.168.1.62:9080/Proyecto2/central/chef/orden"));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        lv1.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int posicion, long id) {
                remove = (int) lv1.getItemIdAtPosition(posicion);
                time = System.currentTimeMillis();
                terminar.setVisibility(View.VISIBLE);
                vista.setVisibility(View.VISIBLE);
                vista.setText(pasosReceta.get(remove));
                lv1.setVisibility(View.INVISIBLE);
                tv1.setVisibility(View.INVISIBLE);
            }
        });

        terminar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (pasosReceta.size() == 0 && pasos.size()==0) {
                    finish();

                }
                else {

                    pasosReceta.remove(remove);
                    pasos.remove(remove);
                    ad.notifyDataSetChanged();


                    vista.setVisibility(View.GONE);
                    terminar.setVisibility(View.INVISIBLE);

                    long timeMillis = System.currentTimeMillis();
                    float timeSeconds = TimeUnit.MILLISECONDS.toSeconds((long)(timeMillis - time));

                    lv1.setVisibility(View.VISIBLE);
                    tv1.setVisibility(View.VISIBLE);

                    if (pasosReceta.size() == 0 && pasos.size()==0) {
                        finish();
                        Toast.makeText(getBaseContext(), "Orden finalizada\nTiempo transcurrido: "+timeSeconds+" segundos", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });



    }

    public ArrayList crearArregloPasos(int len){
        ArrayList<String> arreglo = new ArrayList<String>();
        String text = "Paso";

        for (int i = 0; i < pasosReceta.size(); i++){
            arreglo.add(text + (i+1));
        }
        return arreglo;
    }

    public class ObtenerOrdenes extends AsyncTask<URL, Void, List<String>> {

        @Override
        protected List<String> doInBackground(URL... urls) {
            // Obtener la conexión
            HttpURLConnection con = null;

            try {

                con = (HttpURLConnection) urls[0].openConnection();

                // Activar método POST
                con.setDoOutput(true);

                con.setFixedLengthStreamingMode(nombreC.getBytes().length);
                con.setRequestProperty("Content-Type", "application/json");

                OutputStream out = new BufferedOutputStream(con.getOutputStream());

                out.write(nombreC.getBytes());
                out.flush();
                out.close();


            } catch (IOException e) {
                e.printStackTrace();
            }

            List<String> msj = null;

            try {
                //con = (HttpURLConnection) urls[0].openConnection();
                try {
                    InputStream in = new BufferedInputStream(con.getInputStream());
                    InputStreamReader j=new InputStreamReader(in,"UTF-8");
                    LectorJSON parser = new LectorJSON();

                    msj = parser.readJsonStream(in);

                } finally {
                    con.disconnect();
                }
            } catch (Exception e) {


            }
            return msj;
        }

        @Override
        protected void onPostExecute(List<String> conjunto) {
            if(conjunto.size()==1){
                Toast.makeText(getBaseContext(), "No tiene ordenes pendientes", Toast.LENGTH_LONG).show();
                finish();
            }else{
            String infog=conjunto.get(0);
            String[] InfoG=infog.split("jk");
            conjunto.remove(0);
            infog="Receta a preparar: "+InfoG[0]+"\n\nIngredientes:\n";
            for(int w=1;w<InfoG.length;w++){
                infog=infog+"-->"+InfoG[w]+"\n";
            }
            String todospasos=conjunto.get(0);
            conjunto.remove(0);
            String[] divisiones=todospasos.split("jk");
            for(int u=0;u<divisiones.length;u++){
                String comodin=infog+"\n\n"+"Procedimiento:\n"+"-->"+divisiones[u];
                conjunto.add(comodin);
            }
            pasosReceta=conjunto;
            pasos=crearArregloPasos(conjunto.size());
            ad = new ArrayAdapter<String>(lv1.getContext(),android.R.layout.simple_list_item_1, pasos);
            lv1.setAdapter(ad);
            lv1.setVisibility(View.VISIBLE);}
        }
    }
}