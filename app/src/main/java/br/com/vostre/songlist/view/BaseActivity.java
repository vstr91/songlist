package br.com.vostre.songlist.view;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import org.joda.time.format.DateTimeFormat;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import br.com.vostre.songlist.R;
import br.com.vostre.songlist.listener.APIListener;
import br.com.vostre.songlist.model.Artista;
import br.com.vostre.songlist.model.Musica;
import br.com.vostre.songlist.model.Show;
import br.com.vostre.songlist.util.APIRetorno;
import br.com.vostre.songlist.util.APIUtils;
import br.com.vostre.songlist.util.PreferenceUtils;
import br.com.vostre.songlist.util.ToolbarUtils;
import br.com.vostre.songlist.viewModel.BaseViewModel;

public class BaseActivity extends AppCompatActivity implements APIListener, View.OnClickListener {

    Artista artista;
    BaseViewModel viewModel;
    BaseActivity ctx;
    APIListener listener;
    int consulta = 1;

    JSONArray setlists;

    public Toolbar toolbar;
    Menu menu;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        viewModel = ViewModelProviders.of(this).get(BaseViewModel.class);

        toolbar = findViewById(R.id.toolbar);

        if(toolbar != null){
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        ctx = this;
        listener = this;
    }

    @Override
    public void OnAPIResult(JSONArray result, APIRetorno tipo, int pagina) {

        switch(tipo){
            case SETLIST:

                try{

                    if(consulta < 2 && result != null){

                        setlists = result;

                        APIUtils.consultaSetlists(artista.getId(), listener, 2);
                        consulta++;
                    } else{

                        if(result != null){

                            for(int w = 0; w < result.length(); w++){
                                setlists.put(result.get(w));
                            }

                        }

                        int contList = 0;
                        int flagRepertorio = 0;

                        if(setlists != null){
                            for(int i = 0; i < setlists.length() && contList < 20; i++){

                                Show show = new Show();

                                String dataEvento = setlists.getJSONObject(i).getString("eventDate");

                                JSONObject local = setlists.getJSONObject(i).getJSONObject("venue");

                                JSONObject cidade = local.getJSONObject("city");
                                JSONObject pais = cidade.getJSONObject("country");

                                show.setId(setlists.getJSONObject(i).getString("id"));
                                show.setArtista(artista.getId());
                                show.setNome(local.getString("name"));
                                show.setCidade(cidade.getString("name"));
                                show.setEstado(cidade.getString("state"));
                                show.setPais(pais.getString("name"));
                                show.setData(DateTimeFormat.forPattern("dd-MM-YYYY").parseDateTime(dataEvento));

                                if(viewModel == null){
                                    viewModel = ViewModelProviders.of(this).get(BaseViewModel.class);
                                }

                                viewModel.salvarShow(show);

                                System.out.println(i+"| "+contList+" EVENTO: "+local.getString("name")
                                        +" | "+cidade.getString("name")+", "+cidade.getString("state")+" - "
                                        +pais.getString("name")+" - "+dataEvento);
//
//                        System.out.println("=== REPERTORIO ===");

                                JSONArray sets = setlists.getJSONObject(i).getJSONObject("sets")
                                        .getJSONArray("set");

                                for(int y = 0; y < sets.length(); y++){
                                    flagRepertorio = 0;
                                    JSONArray musicas = sets.getJSONObject(y).getJSONArray("song");

                                    if(musicas.length() > 0){
                                        flagRepertorio++;
                                    }

                                    for(int z = 0; z < musicas.length(); z++){

                                        Musica musica = new Musica();
                                        musica.setId(UUID.randomUUID().toString());
                                        musica.setNome(musicas.getJSONObject(z).getString("name"));
                                        musica.setShow(show.getId());
                                        musica.setObservacao(musicas.getJSONObject(z).optString("info"));

                                        viewModel.salvarMusica(musica);

//                                System.out.println(musicas.getJSONObject(z).getString("name"));
                                    }


                                }

//                        System.out.println("=== FIM REPERTORIO ===");

                                if(flagRepertorio > 0){
                                    contList++;
                                }

                            }
                        } else{
                            Toast.makeText(getApplicationContext(),
                                    "Erro ao buscar informações. Por favor tente novamente.", Toast.LENGTH_LONG).show();
                        }

                        if(contList == 0){
                            Toast.makeText(getApplicationContext(),
                                    "Nenhum registro encontrado!", Toast.LENGTH_LONG).show();
                        }

                    }

//                    Intent i = new Intent(getApplicationContext(), RepertorioActivity.class);
//                    i.putExtra("id", artista.getId());
//                    i.putExtra("nome", artista.getNome());
//                    getApplicationContext().startActivity(i);

                } catch(JSONException e){
                    System.out.println(e.getLocalizedMessage());
                }

                break;
            case USUARIO:
                try {
                    String id = result.getJSONObject(0).getString("id");
                    String usuario = result.getJSONObject(0).getString("display_name");

                    PreferenceUtils.salvarPreferencia(getApplicationContext(),
                            "id", id);

                    PreferenceUtils.salvarPreferencia(getApplicationContext(),
                            "usuario", usuario);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
        }


    }

    Observer<Integer> retornoObserver = new Observer<Integer>() {
        @Override
        public void onChanged(Integer retorno) {

            if(retorno == 1){
                try {
                    APIUtils.consultaSetlists(artista.getId(), listener, 1);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else{
                Toast.makeText(getApplicationContext(), "Erro ao buscar repertório.", Toast.LENGTH_SHORT).show();
            }

        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.

        /*if(iniciaModoCamera){
            getMenuInflater().inflate(R.menu.realidade_aumentada, menu);
        } else{
            getMenuInflater().inflate(R.menu.main, menu);
        }*/

        this.menu = menu;

        ToolbarUtils.preparaMenu(menu, this, this);


        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id){
            case android.R.id.home:
                onBackPressed();
                break;
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        ToolbarUtils.onMenuItemClick(view, this);
    }
}
