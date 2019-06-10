package br.com.vostre.songlist.view.form;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import br.com.vostre.songlist.R;
import br.com.vostre.songlist.databinding.FormListaPlaylistBinding;
import br.com.vostre.songlist.databinding.FormPlaylistBinding;
import br.com.vostre.songlist.listener.MusicaAPIListener;
import br.com.vostre.songlist.listener.PlaylistAPIListener;

public class FormListaPlaylist extends DialogFragment {

    FormListaPlaylistBinding binding;
    AppCompatActivity ctx;

    MusicaAPIListener listener;
    PlaylistAPIListener pListener;

    public MusicaAPIListener getListener() {
        return listener;
    }

    public void setListener(MusicaAPIListener listener) {
        this.listener = listener;
    }

    public PlaylistAPIListener getpListener() {
        return pListener;
    }

    public void setpListener(PlaylistAPIListener pListener) {
        this.pListener = pListener;
    }

    public Context getCtx() {
        return ctx;
    }

    public void setCtx(AppCompatActivity ctx) {
        this.ctx = ctx;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(
                inflater, R.layout.form_playlist, container, false);
        super.onCreate(savedInstanceState);

        binding.setView(this);

        return binding.getRoot();

    }

    public void onClickBtnNova(View v){
        FormNovaPlaylist formNovaPlaylist = new FormNovaPlaylist();
        formNovaPlaylist.setListener(listener);
        formNovaPlaylist.setpListener(pListener);
        formNovaPlaylist.show(ctx.getSupportFragmentManager(), "formPlaylist");

        this.dismiss();
    }

    public void onClickBtnAdicionar(View v){

    }

}
