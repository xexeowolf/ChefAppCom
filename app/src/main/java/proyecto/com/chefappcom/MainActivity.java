package proyecto.com.chefappcom;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.linkedin.platform.LISessionManager;
import com.linkedin.platform.errors.LIAuthError;
import com.linkedin.platform.listeners.AuthListener;
import com.linkedin.platform.utils.Scope;

import android.app.ProgressDialog;
import android.widget.ImageView;


import com.linkedin.platform.APIHelper;
import com.linkedin.platform.errors.LIApiError;
import com.linkedin.platform.listeners.ApiListener;
import com.linkedin.platform.listeners.ApiResponse;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;





public class MainActivity extends AppCompatActivity {


    String nombreUsuario,correo;
    Intent intento;
    TextView k;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        k=(TextView)findViewById(R.id.tvtcorreo);
        TextView kk=(TextView)findViewById(R.id.tvtnombre);
        LISessionManager.getInstance(getApplicationContext())
                .init(this, buildScope(), new AuthListener() {
                    @Override
                    public void onAuthSuccess() {

                        Toast.makeText(getApplicationContext(), "success" +
                                        LISessionManager.getInstance(getApplicationContext())
                                                .getSession().getAccessToken().toString(),
                                Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onAuthError(LIAuthError error) {
                        Toast.makeText(getApplicationContext(), "failed "
                                        + error.toString(),
                                Toast.LENGTH_LONG).show();
                    }
                }, true);
    }


    public void login(View view){
        startActivity(intento);
    }

    @Override
    protected void onActivityResult(int requestCode,
                                    int resultCode, Intent data) {

        LISessionManager.getInstance(getApplicationContext())
                .onActivityResult(this,
                        requestCode, resultCode, data);

        connectProfile();
    }

    private static Scope buildScope() {
        return Scope.build(Scope.R_BASICPROFILE,
                Scope.R_EMAILADDRESS);
    }

    private static final String url = "https://api.linkedin.com/v1/people/~:(id,email-address," +
            "first-name,last-name,formatted-name,picture-url)?format=json";

    private ProgressDialog progress;
    private TextView user_name, user_email;
    private ImageView profile_picture;


    protected void connectProfile() {

        progress= new ProgressDialog(this);
        progress.setMessage("Retrieve data...");
        progress.setCanceledOnTouchOutside(false);
        progress.show();

        user_email = (TextView) findViewById(R.id.email);
        user_name = (TextView) findViewById(R.id.name);
        profile_picture = (ImageView) findViewById(R.id.profile_picture);

        linkededinApiHelper();

    }

    public void linkededinApiHelper(){
        APIHelper apiHelper = APIHelper.getInstance(getApplicationContext());
        apiHelper.getRequest(MainActivity.this, url, new ApiListener() {
            @Override
            public void onApiSuccess(ApiResponse result) {
                try {
                    showResult(result.getResponseDataAsJson());
                    progress.dismiss();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onApiError(LIApiError error) {

            }
        });
    }

    public void showResult(JSONObject response){

        try {
            correo=response.get("emailAddress").toString();
            user_email.setText(correo);
            nombreUsuario=response.get("formattedName").toString();
            user_name.setText(nombreUsuario);

            Picasso.with(this).load(response.getString("pictureUrl"))
                    .into(profile_picture);
            intento=new Intent(k.getContext(),navigation.class);
            intento.putExtra("objeto",response.toString());

        } catch (Exception e){
            e.printStackTrace();
        }
    }
}


