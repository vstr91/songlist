package br.com.vostre.songlist.view.viewHolder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import br.com.vostre.songlist.databinding.LinhaMusicasBinding;
import br.com.vostre.songlist.databinding.LinhaPlaylistsBinding;
import br.com.vostre.songlist.model.pojo.MusicaQuantidade;
import br.com.vostre.songlist.model.pojo.Playlist;

public class PlaylistViewHolder extends RecyclerView.ViewHolder {

    private final LinhaPlaylistsBinding binding;
    Context ctx;

    public PlaylistViewHolder(LinhaPlaylistsBinding binding, Context context) {
        super(binding.getRoot());
        this.binding = binding;
        this.ctx = context;
    }

    public void bind(final Playlist playlist) {
        binding.setPlaylist(playlist);

        binding.textViewNome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ctx, "Playlist: "+playlist.getNome(), Toast.LENGTH_SHORT).show();
            }
        });

        binding.executePendingBindings();
    }
}
