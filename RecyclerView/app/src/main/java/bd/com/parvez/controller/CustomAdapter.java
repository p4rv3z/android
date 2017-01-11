package bd.com.parvez.controller;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import bd.com.parvez.model.Item;
import bd.com.parvez.recyclerview.R;

/**
 * Created by ParveZ on 1/11/2017.
 */

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ItemHolder> {
    private List<Item> data;
    private Context context;
    private LayoutInflater layoutInflater;
    private ItemClickCallBack itemClickCallBack;

    public CustomAdapter(List<Item> data, Context context) {
        this.data = data;
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
    }

    /**
     * set on item click listener
     * @param itemClickCallBack
     */
    public void setItemClickCallBack(final ItemClickCallBack itemClickCallBack) {
        this.itemClickCallBack = itemClickCallBack;
    }

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_layout, parent, false);
        //View view = layoutInflater.inflate(R.layout.item_card_layout, parent, false);
        return new ItemHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(ItemHolder holder, int position) {
        Item item = data.get(position);
        holder.name.setText(item.getName());
        holder.email.setText(item.getEmail());
        holder.image.setImageResource(item.getImage());
        if (item.isStatus()) {
            holder.status.setImageResource(R.mipmap.ic_active);
        } else {
            holder.status.setImageResource(R.mipmap.ic_deactive);
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class ItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView name;
        private TextView email;
        private ImageView image;
        private ImageView status;
        private final View container;

        public ItemHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.item_name);
            email = (TextView) itemView.findViewById(R.id.item_email);
            image = (ImageView) itemView.findViewById(R.id.item_image);
            status = (ImageView) itemView.findViewById(R.id.image_status);
            container = itemView.findViewById(R.id.item_root);
            container.setOnClickListener(this);
            status.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.item_root) {
                itemClickCallBack.onItemClickCallBack(getAdapterPosition());
            }
            if (v.getId() == R.id.image_status) {
                itemClickCallBack.onStatusClickCallBack(getAdapterPosition());
            }
        }
    }

    public interface ItemClickCallBack {
        /**
         * List Item click
         *
         * @param position
         */
        void onItemClickCallBack(int position);

        /**
         * List Status clicl
         *
         * @param position
         */
        void onStatusClickCallBack(int position);
    }
}
