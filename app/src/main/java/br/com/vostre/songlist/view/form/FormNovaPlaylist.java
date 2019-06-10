package br.com.vostre.songlist.view.form;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import br.com.vostre.songlist.R;
import br.com.vostre.songlist.databinding.FormNovaPlaylistBinding;
import br.com.vostre.songlist.listener.MusicaAPIListener;
import br.com.vostre.songlist.listener.PlaylistAPIListener;
import br.com.vostre.songlist.viewModel.BaseViewModel;
import br.com.vostre.songlist.viewModel.RepertorioViewModel;

public class FormNovaPlaylist extends DialogFragment implements PlaylistAPIListener {

    FormNovaPlaylistBinding binding;
    RepertorioViewModel viewModel;

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

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(
                inflater, R.layout.form_nova_playlist, container, false);
        super.onCreate(savedInstanceState);

        viewModel = ViewModelProviders.of(this.getActivity()).get(RepertorioViewModel.class);

        binding.setView(this);

        return binding.getRoot();

    }

    public void onClickBtnSalvar(View v){

        String nome = binding.editTextPlaylist.getText().toString();

        if(nome != null && !nome.isEmpty()){
            viewModel.criarPlaylist(nome, listener, this);
        } else{
            Toast.makeText(getActivity().getApplicationContext(), "Por favor informe o nome!",
                    Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void OnPlaylistAPIResult(String result) {

        if(result != null){
            dismiss();
        }

        pListener.OnPlaylistAPIResult(result);

    }
}
