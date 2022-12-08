package com.example.computacaomovel;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ViewHolder> {

    private final Cursor cursor;
    private final String tableName;
    private OnItemClickListener listener;

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView header;
        public TextView sutext1;
        public TextView sutext2;
        public TextView sutext3;

        ViewHolder(View view) {
            super(view);
            header = view.findViewById(R.id.item_header);
            sutext1 = view.findViewById(R.id.subitem_text_1);
            sutext2 = view.findViewById(R.id.subitem_text_2);
            sutext3 = view.findViewById(R.id.subitem_text_3);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        int position = getAbsoluteAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(itemView, position);
                        }
                    }
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View itemView, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    ItemsAdapter(Cursor cursor, String tableName) {
        this.cursor = cursor;
        this.tableName = tableName;
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
        String[] columns;
        if (tableName == DbSchemaContract.Conferencia.TABLE_NAME) {
            columns = DbSchemaContract.Conferencia.COLUMNS_ARRAY;
        } else if (tableName == DbSchemaContract.Periodicos.TABLE_NAME) {
            columns = DbSchemaContract.Periodicos.COLUMNS_ARRAY;
        } else {
            columns = DbSchemaContract.OutrasAreas.COLUMNS_ARRAY;
        }

        cursor.moveToPosition(position);

        if (columns.length == 4) {
            holder.header.setText(cursor.getString(2));
            holder.sutext1.setText(cursor.getString(1));
            holder.sutext2.setText(cursor.getString(3));
            holder.sutext3.setText("");
        } else {
            holder.header.setText(cursor.getString(2));
            holder.sutext1.setText(cursor.getString(1));
            String aux = cursor.getString(3)+"/"+cursor.getString(4);
            holder.sutext2.setText(aux);
            holder.sutext3.setText(cursor.getString(5));
        }
    }

    @Override
    public int getItemCount() {
        return cursor.getCount();
    }
}
