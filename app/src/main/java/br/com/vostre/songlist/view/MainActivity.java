package br.com.vostre.songlist.view;

import android.Manifest;
import android.app.Activity;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import br.com.vostre.songlist.R;
import br.com.vostre.songlist.databinding.ActivityMainBinding;
import br.com.vostre.songlist.listener.MusicaAPIListener;
import br.com.vostre.songlist.listener.PlaylistAPIListener;
import br.com.vostre.songlist.listener.PlaylistAtualizadaAPIListener;
import br.com.vostre.songlist.model.Artista;
import br.com.vostre.songlist.model.Musica;
import br.com.vostre.songlist.model.Show;
import br.com.vostre.songlist.model.adapter.AutoSuggestAdapter;
import br.com.vostre.songlist.model.pojo.MusicaQuantidade;
import br.com.vostre.songlist.util.APIRetorno;
import br.com.vostre.songlist.util.APIUtils;
import br.com.vostre.songlist.util.Constants;
import br.com.vostre.songlist.util.DBUtils;
import br.com.vostre.songlist.util.PreferenceUtils;
import br.com.vostre.songlist.util.ToolbarUtils;
import br.com.vostre.songlist.view.adapter.ArtistaAdapter;
import br.com.vostre.songlist.view.form.FormNovaPlaylist;
import br.com.vostre.songlist.viewModel.MainViewModel;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;

import net.danlew.android.joda.JodaTimeAndroid;

import org.joda.time.format.DateTimeFormat;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static br.com.vostre.songlist.util.Constants.AUTO_COMPLETE_DELAY;
import static br.com.vostre.songlist.util.Constants.CLIENT_ID;
import static br.com.vostre.songlist.util.Constants.TRIGGER_AUTO_COMPLETE;
import static com.spotify.sdk.android.authentication.LoginActivity.REQUEST_CODE;

public class MainActivity extends BaseActivity implements MusicaAPIListener, PlaylistAPIListener,
        PlaylistAtualizadaAPIListener, NavigationView.OnNavigationItemSelectedListener  {

    ActivityMainBinding binding;
    MainViewModel viewModel;
    AutoSuggestAdapter autoSuggestAdapter;
    private Handler handler;

    MainActivity ctx;

    //Artista artista;
    List<Artista> artistas;

    ArtistaAdapter adapter;

    private static final int REQUEST_CODE = 1337;
    private static final String REDIRECT_URI = "http://localhost:8888";

    DrawerLayout drawer;
    NavigationView navView;
    ActionBarDrawerToggle drawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        super.onCreate(savedInstanceState);
        binding.setView(this);
        setTitle("Songlist");
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        drawer = binding.container;
        navView = binding.nav;

        navView.setNavigationItemSelectedListener(this);

        drawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, 0, 0){

            public void onDrawerClosed(View view){
                super.onDrawerClosed(view);
                drawerToggle.syncState();
            }

            public void onDrawerOpened(View view){
                super.onDrawerOpened(view);
                drawerToggle.syncState();
            }

        };

        drawer.addDrawerListener(drawerToggle);
        drawerToggle.syncState();

//        // APENAS DEBUG
//        Dexter.withActivity(this)
//                .withPermissions(
//                        Manifest.permission.WRITE_EXTERNAL_STORAGE
//                ).withListener(new MultiplePermissionsListener() {
//            @Override
//            public void onPermissionsChecked(MultiplePermissionsReport report) {
//
//                if(report.areAllPermissionsGranted()){
////                    Bundle bundle = new Bundle();
////                    bundle.putString("permissoes", "1");
////                    configuraActivity();
//                } else{
////                    Bundle bundle = new Bundle();
////                    bundle.putString("permissoes", "0");
//
//                    Toast.makeText(getApplicationContext(), "Acesso ao GPS e Armazenamento são necessários para aproveitar ao máximo as funções do Circular!", Toast.LENGTH_LONG).show();
//                }
//
//            }
//
//            @Override
//            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
//                token.continuePermissionRequest();
//            }
//        })
//                .check();
//        // FIM APENAS DEBUG

        JodaTimeAndroid.init(this);

        ctx = this;
        listener = this;

        //DBUtils.exportDB(this);

        viewModel = ViewModelProviders.of(this).get(MainViewModel.class);

        viewModel.artistas.observe(this, artistasObserver);

        GridLayoutManager glm = new GridLayoutManager(ctx, 2);
        binding.listConsultas.setLayoutManager(glm);

        RecyclerView listConsultas = binding.listConsultas;

//        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(listConsultas.getContext(),
//                GridLayoutManager.HORIZONTAL);
//        listConsultas.addItemDecoration(dividerItemDecoration);

        adapter = new ArtistaAdapter(artistas, this);

        listConsultas.setAdapter(adapter);

        //Setting up the adapter for AutoSuggest
        autoSuggestAdapter = new AutoSuggestAdapter(this,
                android.R.layout.simple_dropdown_item_1line);
        binding.autoCompleteTextView.setThreshold(2);
        binding.autoCompleteTextView.setAdapter(autoSuggestAdapter);
        binding.autoCompleteTextView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {

                        artista = autoSuggestAdapter.getObject(position);

                        viewModel.salvarArtista(artista);

                        Toast.makeText(getApplicationContext(),
                                "Buscando músicas... Só um momento, por favor!", Toast.LENGTH_LONG).show();

                        //binding.container.requestFocus();

                        InputMethodManager imm = (InputMethodManager)
                                getApplicationContext()
                                        .getSystemService(Activity.INPUT_METHOD_SERVICE);

                        imm.hideSoftInputFromWindow(binding.getRoot().getWindowToken(), 0);

                        viewModel.limparDadosArtista(artista);
                        viewModel.retorno.observe(ctx, retornoObserver);

                        binding.autoCompleteTextView.setText("");

                        // abre tela detalhes
                        Intent i = new Intent(ctx, RepertorioActivity.class);
                        i.putExtra("id", artista.getId());
                        i.putExtra("nome", artista.getNome());
                        ctx.startActivity(i);

                    }
                });

        binding.autoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int
                    count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                handler.removeMessages(TRIGGER_AUTO_COMPLETE);
                handler.sendEmptyMessageDelayed(TRIGGER_AUTO_COMPLETE,
                        AUTO_COMPLETE_DELAY);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                if (msg.what == TRIGGER_AUTO_COMPLETE) {
                    if (!TextUtils.isEmpty(binding.autoCompleteTextView.getText())) {
                        makeApiCall(binding.autoCompleteTextView.getText().toString());
                    }
                }
                return false;
            }
        });

    }

    private void makeApiCall(String text) {

        try {
            APIUtils.consultaArtistas(text, this);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void onClickBtnSpotify(View v){

        List<String> ids = new ArrayList<>();

        List<Artista> arts = viewModel.artistas.getValue();

        for(Artista a : arts){

            if(a.isSelecionado()){
                ids.add(a.getId());
            }

        }

        if(ids.size() > 0){
            AuthenticationRequest.Builder builder =
                    new AuthenticationRequest.Builder(CLIENT_ID, AuthenticationResponse.Type.TOKEN, REDIRECT_URI);

            builder.setScopes(new String[]{"playlist-modify-public", "playlist-modify-private"});
            AuthenticationRequest request = builder.build();

            AuthenticationClient.openLoginActivity(this, REQUEST_CODE, request);

            viewModel.setIdArtistas(ids.toArray(new String[0]));

            viewModel.musicas.observe(this, musicasObserver);
        } else{
            Toast.makeText(getApplicationContext(), "Por favor selecione ao menos um artista.",
                    Toast.LENGTH_SHORT).show();
        }

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        // Check if result comes from the correct activity
        if (requestCode == REQUEST_CODE) {
            AuthenticationResponse response = AuthenticationClient.getResponse(resultCode, intent);

            switch (response.getType()) {
                // Response was successful and contains auth token
                case TOKEN:
                    // Handle successful response
                    PreferenceUtils.salvarPreferencia(getApplicationContext(),
                            "access_token", response.getAccessToken());

                    viewModel.salvarUsuarioLogado(response.getAccessToken(), this);

                    String id = PreferenceUtils.carregarPreferencia(ctx, "id");

                    if(!id.isEmpty()){
                        FormNovaPlaylist formPlaylist = new FormNovaPlaylist();
                        //formPlaylist.setCtx(ctx);
                        formPlaylist.setListener(this);
                        formPlaylist.setpListener(this);
                        formPlaylist.show(ctx.getSupportFragmentManager(), "formPlaylist");
                        //viewModel.buscarPlaylists(ctx);
                    } else{
                        Toast.makeText(getApplicationContext(), "Houve erro ao carregar seu usuário. " +
                                "Por favor tente novamente.", Toast.LENGTH_SHORT).show();
                    }

                    break;

                // Auth flow returned an error
                case ERROR:
                    Toast.makeText(getApplicationContext(), "Houve erro ao fazer login. " +
                            "Por favor tente novamente.", Toast.LENGTH_SHORT).show();
                    break;

                // Most likely auth flow was cancelled
                default:
                    // Handle other cases
            }
        }
    }

    Observer<List<Artista>> artistasObserver = new Observer<List<Artista>>() {
        @Override
        public void onChanged(List<Artista> artistas) {
            adapter.artistas = artistas;
            adapter.notifyDataSetChanged();

        }
    };

    Observer<List<MusicaQuantidade>> musicasObserver = new Observer<List<MusicaQuantidade>>() {
        @Override
        public void onChanged(List<MusicaQuantidade> musicas) {
            // busca id das musicas no spotify e adiciona a lista
            viewModel.relacionarMusicasSpotify(viewModel.musicas.getValue(), ctx);
        }
    };

    @Override
    public void OnAPIResult(JSONArray result, APIRetorno tipo, int pagina) {

        switch(tipo){
            case ARTISTA:
//                parsing logic, please change it as per your requirement
                List<Artista> stringList = new ArrayList<>();
                try {
                    for (int i = 0; i < result.length(); i++) {

                        Artista artista = new Artista();

                        JSONObject row = result.getJSONObject(i);

                        artista.setId(row.getString("id"));
                        artista.setNome(row.getString("name"));
                        artista.setDescricao(row.optString("disambiguation"));

                        stringList.add(artista);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //IMPORTANT: set data here and notify
                autoSuggestAdapter.setData(stringList);
                autoSuggestAdapter.notifyDataSetChanged();

                break;
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
                                    viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
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
        }


    }

    @Override
    public void OnMusicaAPIResult(MusicaQuantidade musica, boolean varias) {

        if(!musica.getMusica().getIdSpotify().equals("-1")){
            viewModel.musicasSpotify.add(musica.getMusica().getIdSpotify());
            viewModel.atualizarMusica(musica.getMusica());
        }

    }

    @Override
    public void OnPlaylistAPIResult(String result) {

        if(result != null && viewModel.musicasSpotify != null){

            if(viewModel.musicasSpotify.size() > 0){
                viewModel.adicionarMusicasPlaylist(result, viewModel.musicasSpotify, this);
            }

        } else{
            Toast.makeText(getApplicationContext(), "Erro ao exportar músicas!", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void OnPlaylistAtualizadaAPIResult(boolean atualizou, String idPlaylist) {

        if(atualizou){
            Toast.makeText(getApplicationContext(), "Playlist criada com sucesso!",
                    Toast.LENGTH_SHORT).show();

            viewModel.resetaSelecionados();

            adapter.notifyDataSetChanged();

        } else{
            Toast.makeText(getApplicationContext(), "Erro ao criar playlist! " +
                    "Por favor tente novamente", Toast.LENGTH_SHORT).show();
            viewModel.removerPlaylist(idPlaylist);
        }

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }

    public void onClickBtnSobre(View v){
        drawer.closeDrawers();
//        Intent i = new Intent(getApplicationContext(), SobreActivity.class);
//        startActivity(i);
    }

}
