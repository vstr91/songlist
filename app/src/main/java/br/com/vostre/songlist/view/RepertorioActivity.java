package br.com.vostre.songlist.view;

import android.Manifest;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
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
import java.util.List;
import java.util.UUID;

import br.com.vostre.songlist.R;
import br.com.vostre.songlist.databinding.ActivityMainBinding;
import br.com.vostre.songlist.databinding.ActivityRepertorioBinding;
import br.com.vostre.songlist.listener.APIListener;
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
import br.com.vostre.songlist.util.DBUtils;
import br.com.vostre.songlist.util.NetworkUtils;
import br.com.vostre.songlist.util.PreferenceUtils;
import br.com.vostre.songlist.view.adapter.ArtistaAdapter;
import br.com.vostre.songlist.view.adapter.MusicaAdapter;
import br.com.vostre.songlist.view.form.FormNovaPlaylist;
import br.com.vostre.songlist.view.form.FormPlaylist;
import br.com.vostre.songlist.viewModel.MainViewModel;
import br.com.vostre.songlist.viewModel.RepertorioViewModel;

import static br.com.vostre.songlist.util.Constants.AUTO_COMPLETE_DELAY;
import static br.com.vostre.songlist.util.Constants.CLIENT_ID;
import static br.com.vostre.songlist.util.Constants.TRIGGER_AUTO_COMPLETE;

public class RepertorioActivity extends BaseActivity implements MusicaAPIListener, PlaylistAPIListener, PlaylistAtualizadaAPIListener {

    private static final int REQUEST_CODE = 1337;
    private static final String REDIRECT_URI = "http://localhost:8888";

    ActivityRepertorioBinding binding;
    RepertorioViewModel viewModel;
    MusicaAdapter adapter;
    private Handler handler;

    RepertorioActivity ctx;

    List<MusicaQuantidade> musicas;

    String id;

    List<String> musicasSpotify;
    Integer cont = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_repertorio);
        super.onCreate(savedInstanceState);
        binding.setView(this);
        setTitle("Músicas");
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        ctx = this;
        listener = this;

        id = getIntent().getStringExtra("id");
        String nome = getIntent().getStringExtra("nome");

        viewModel = ViewModelProviders.of(this).get(RepertorioViewModel.class);

        viewModel.setId(id);

        binding.textViewArtista.setText(nome);

        viewModel.musicas.observe(this, musicasObserver);

//        Musica mus = new Musica();
//        mus.setNome("Algorithm");
//        MusicaQuantidade m = new MusicaQuantidade();
//        m.setArtista("Muse");
//        m.setMusica(mus);
//
//        viewModel.buscarMusica(m, this);

        RecyclerView listConsultas = binding.listMusicas;

//        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(listConsultas.getContext(),
//                GridLayoutManager.VERTICAL);
//        listConsultas.addItemDecoration(dividerItemDecoration);

        adapter = new MusicaAdapter(musicas, this);

        listConsultas.setAdapter(adapter);

    }

    public void onClickBtnConsultar(View v){

        if(NetworkUtils.estaConectado(getApplicationContext())){
            Artista artista = new Artista();
            artista.setId(id);
            this.artista = artista;
            viewModel.limparDadosArtista(artista);
            viewModel.retorno.observe(this, retornoObserver);
            Toast.makeText(getApplicationContext(), "Refazendo consulta. Só um momento, por favor!",
                    Toast.LENGTH_SHORT).show();
        } else{
            Toast.makeText(getApplicationContext(), "Não há conexão com a Internet! " +
                            "Por favor verifique e tente novamente.",
                    Toast.LENGTH_SHORT).show();
        }


    }

    public void onClickBtnSpotify(View v){

        if(NetworkUtils.estaConectado(getApplicationContext())){
            AuthenticationRequest.Builder builder =
                    new AuthenticationRequest.Builder(CLIENT_ID, AuthenticationResponse.Type.TOKEN, REDIRECT_URI);

            builder.setScopes(new String[]{"playlist-modify-public", "playlist-modify-private"});
            AuthenticationRequest request = builder.build();

            AuthenticationClient.openLoginActivity(this, REQUEST_CODE, request);
        } else{
            Toast.makeText(getApplicationContext(), "Não há conexão com a Internet! " +
                            "Por favor verifique e tente novamente.",
                    Toast.LENGTH_SHORT).show();
        }

    }

    Observer<List<MusicaQuantidade>> musicasObserver = new Observer<List<MusicaQuantidade>>() {
        @Override
        public void onChanged(List<MusicaQuantidade> musicas) {
            System.out.println("Musicas: "+musicas.size());
            adapter.musicas = musicas;
            adapter.notifyDataSetChanged();

        }
    };

//    Observer<Integer> retornoObserver = new Observer<Integer>() {
//        @Override
//        public void onChanged(Integer retorno) {
//
//            if(retorno == 1){
//                try {
//                    APIUtils.consultaSetlists(artista.getId(), ctx, 1);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//
//        }
//    };

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

                    // busca id das musicas no spotify e adiciona a lista
                    viewModel.relacionarMusicasSpotify(this);

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

    @Override
    public void OnMusicaAPIResult(MusicaQuantidade musica, boolean varias) {

        if(!musica.getMusica().getIdSpotify().equals("-1")){
            viewModel.musicasSpotify.add(musica.getMusica().getIdSpotify());
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
        } else{
            Toast.makeText(getApplicationContext(), "Erro ao criar playlist! " +
                    "Por favor tente novamente", Toast.LENGTH_SHORT).show();
            viewModel.removerPlaylist(idPlaylist);
        }

    }
}
