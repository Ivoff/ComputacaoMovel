package com.example.computacaomovel;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ViewHolder> {

    public class ViewHolder extends RecyclerView.ViewHolder {
        ViewHolder(View view) {
            super(view);
            TextView header = view.findViewById(R.id.item_header);
            TextView sutext1 = view.findViewById(R.id.subitem_text_1);
            TextView sutext2 = view.findViewById(R.id.subitem_text_2);
            TextView sutext3 = view.findViewById(R.id.subitem_text_3);
        }
    }

    private SQLiteDatabase db;

    ItemsAdapter(SQLiteDatabase database) {
        db = database;
    }

    @NonNull
    @Override
    public ItemsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();;
        LayoutInflater inflater = LayoutInflater.from(context);

        View item = inflater.inflate(R.layout.row_item, parent, false);

        ViewHolder viewHolder = new ViewHolder(item);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//        db.quer
    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
