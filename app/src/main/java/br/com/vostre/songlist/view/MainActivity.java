package br.com.vostre.songlist.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import br.com.vostre.songlist.R;
import br.com.vostre.songlist.databinding.ActivityMainBinding;
import br.com.vostre.songlist.model.api.SonglistAPI;
import br.com.vostre.songlist.util.APIUtils;
import br.com.vostre.songlist.util.Constants;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.databinding.DataBindingUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
//    SobreViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        super.onCreate(savedInstanceState);
        binding.setView(this);
        setTitle("Sobre");
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        SonglistAPI api = APIUtils.criaInstanciaAPI(Constants.API_MUSICBRAINZ);
        Call<String> call = api.buscaArtistas("name:u2", "json");

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                JSONObject array;

                try {
                    array = new JSONObject(response.body());
                    JSONArray artistas = array.getJSONArray("artists");

                    for(int i = 0; i < artistas.length(); i++){
                        System.out.println("ARTISTA: "+artistas.getJSONObject(i).getString("name")+" | "+artistas.getJSONObject(i).getString("disambiguation"));
                    }

                    array.toString();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                System.out.println("AA: "+call.request().url()+" | "+t.getMessage());
            }
        });

//        viewModel = ViewModelProviders.of(this).get(SobreViewModel.class);
    }
}
