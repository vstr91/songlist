package br.com.vostre.songlist.view.viewHolder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import br.com.vostre.songlist.databinding.LinhaArtistasBinding;
import br.com.vostre.songlist.databinding.LinhaMusicasBinding;
import br.com.vostre.songlist.model.Artista;
import br.com.vostre.songlist.model.Musica;
import br.com.vostre.songlist.model.pojo.MusicaQuantidade;

public class MusicaViewHolder extends RecyclerView.ViewHolder {

    private final LinhaMusicasBinding binding;
    Context ctx;

    public MusicaViewHolder(LinhaMusicasBinding binding, Context context) {
        super(binding.getRoot());
        this.binding = binding;
        this.ctx = context;
    }

    public void bind(final MusicaQuantidade musica, int posicao) {
        binding.setMusica(musica);

        binding.textViewPosicao.setText(String.valueOf(posicao));

//        binding.textViewNome.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(ctx, "Música: "+musica.getMusica().getNome(), Toast.LENGTH_SHORT).show();
//            }
//        });

        binding.executePendingBindings();
    }
}
