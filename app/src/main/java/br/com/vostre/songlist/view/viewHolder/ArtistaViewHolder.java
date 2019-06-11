package br.com.vostre.songlist.view.viewHolder;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;

import org.joda.time.format.DateTimeFormat;

import br.com.vostre.songlist.databinding.LinhaArtistasBinding;
import br.com.vostre.songlist.listener.ArtistaListener;
import br.com.vostre.songlist.model.Artista;
import br.com.vostre.songlist.util.FirebaseUtils;
import br.com.vostre.songlist.view.MainActivity;
import br.com.vostre.songlist.view.RepertorioActivity;

public class ArtistaViewHolder extends RecyclerView.ViewHolder {

    private final LinhaArtistasBinding binding;
    Context ctx;
    ArtistaListener listener;
    int posicao;
    FirebaseAnalytics mFirebaseAnalytics;

    public ArtistaViewHolder(LinhaArtistasBinding binding, Context context) {
        super(binding.getRoot());
        this.binding = binding;
        this.ctx = context;
        mFirebaseAnalytics = FirebaseUtils.iniciaAnalytics(context);
    }

    public void bind(final Artista artista) {
        binding.setArtista(artista);

        binding.textViewData.setText("Consultado em "+ DateTimeFormat
                .forPattern("dd/MM/YYYY").print(artista.getDataCadastro()));

        if(!artista.isSelecionado()){
            binding.layout.setBackgroundColor(Color.parseColor("#00FFFFFF"));
        } else{
            binding.layout.setBackgroundColor(Color.parseColor("#DDDDFF"));
        }

        binding.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ctx, RepertorioActivity.class);
                i.putExtra("id", artista.getId());
                i.putExtra("nome", artista.getNome());

                Bundle bundle = new Bundle();
                bundle.putString("artista", artista.getNome());
                bundle.putString("descricao", artista.getDescricao());

                FirebaseUtils.gravaEvento(mFirebaseAnalytics, bundle, "clicou_artista");

                ctx.startActivity(i);
            }
        });

        binding.layout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                if(artista.isSelecionado()){
                    artista.setSelecionado(false);
                    binding.layout.setBackgroundColor(Color.parseColor("#00FFFFFF"));
                } else{
                    artista.setSelecionado(true);
                    binding.layout.setBackgroundColor(Color.parseColor("#DDDDFF"));
                }

                return true;
            }
        });

        binding.executePendingBindings();
    }
}
