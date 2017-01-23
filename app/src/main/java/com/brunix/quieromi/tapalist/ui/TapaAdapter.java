package com.brunix.quieromi.tapalist.ui;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.brunix.quieromi.R;
import com.brunix.quieromi.UIConstants;
import com.brunix.quieromi.data.entity.Tapa;
import com.jakewharton.rxbinding.view.RxView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.functions.Action1;

import static com.brunix.quieromi.Utils.orderListByName;
import static com.jakewharton.rxbinding.internal.Preconditions.checkNotNull;

/**
 * Created by dolo on 10/6/16.
 */

public class TapaAdapter extends RecyclerView.Adapter<TapaAdapter.TapaViewHolder> {

    private final static String TAG = TapaAdapter.class.getSimpleName();

    private static TapaViewHolder.OpenTapaListener tapaListener;

    private List<Tapa> tapas = new ArrayList<>();

    private final Picasso picasso;


    public TapaAdapter(List<Tapa> pTapas, Picasso pPicasso) {
        tapas = pTapas;
        picasso = pPicasso;
    }

    public void clearTapas() {
        tapas.clear();
        notifyDataSetChanged();
    }

    public void addTapa(Tapa pTapa) {
        for(Iterator<Tapa> it = tapas.iterator(); it.hasNext();){
            Tapa tapa = it.next();
            if (tapa.getId().equals(pTapa.getId())) {
                it.remove();
                break;
            }
        }
        tapas.add(pTapa);
        orderListByName(tapas);
        notifyDataSetChanged();
    }



    public void removeTapa(String tapaId) {
        int pos = 0;
        for(Iterator<Tapa> it = tapas.iterator(); it.hasNext();){
            Tapa tapa = it.next();
            if (tapa.getId().equals(tapaId)) {
                it.remove();
                break;
            }
            pos++;
        }
        orderListByName(tapas);
        notifyItemRemoved(pos);
    }

    public void updateTapas(List<Tapa> pTapas) {
        tapas.clear();
        tapas.addAll(pTapas);
        notifyDataSetChanged();
    }

    @Override
    public TapaAdapter.TapaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.tapa_list_item, parent, false);
        return new TapaAdapter.TapaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TapaViewHolder holder, int position) {
        if (position < tapas.size()) {
            Tapa tapa = tapas.get(position);
            holder.bindTapa(tapa, picasso);
        }
    }

    @Override
    public int getItemCount() {
        return tapas == null ? 0 : tapas.size();
    }

    public void setOpenTapaListener(TapaViewHolder.OpenTapaListener listener) {
        tapaListener = listener;
    }

    public void cleanup() {
        tapaListener = null;
        tapas = null;
    }


    public static class TapaViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tapaImageView)
        ImageView tapaImageView;
        @BindView(R.id.tapaNameTextView)
        TextView nameTextView;
        @BindView(R.id.categoryTextView)
        TextView categoryTextView;
        @BindView(R.id.ratingTextView)
        TextView ratingTextView;

        private final View view;

        private Tapa tapa;

        public TapaViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            view = itemView;
        }

        public void bindTapa(final Tapa tapa, final Picasso picasso) {
            this.tapa = tapa;
            // TODO Usar como singleton
//            picasso.load(tapa.getImageUrl())
//                    .resize(MAX_WIDTH, MAX_HEIGHT)
//                    .centerCrop()
//                    .into(tapaImageView);

            picasso.load(tapa.getImageUrl())
                    .placeholder(R.mipmap.ic_file_not_found)
                    .resize(UIConstants.IMG_MAX_WIDTH, UIConstants.IMG_MAX_HEIGHT)
                    .centerCrop()
                    .networkPolicy(NetworkPolicy.OFFLINE)
                    .into(tapaImageView, new Callback() {
                        @Override
                        public void onSuccess() {
                            Log.d(TAG, "Fetched image from cache:" + tapa.getImageUrl());
                        }

                        @Override
                        public void onError() {
                            //Try again online if cache failed
                            picasso.load(tapa.getImageUrl())
                                    .placeholder(R.mipmap.ic_file_not_found)
                                    .resize(UIConstants.IMG_MAX_WIDTH, UIConstants.IMG_MAX_HEIGHT)
                                    .centerCrop()
                                    .error(R.drawable.waffles)
                                    .into(tapaImageView, new Callback() {
                                        @Override
                                        public void onSuccess() {
                                            Log.d(TAG, "Fetched image from network:" + tapa.getImageUrl());
                                        }

                                        @Override
                                        public void onError() {
                                            Log.v(TAG,"Could not fetch image");
                                        }
                                    });
                        }
                    });

            nameTextView.setText(tapa.getName());
            categoryTextView.setText("Category of the tapa");
            ratingTextView.setText("Rating: " + tapa.getLongitude() + "/5");

            RxView.clicks(view)
                    .subscribe(new Action1<Void>() {
                        @Override
                        public void call(Void aVoid) {
                            checkNotNull(tapaListener, "Must implement OpenTapaListener");
                            tapaListener.open(TapaViewHolder.this.tapa);
                        }
//                    })
//                    .subscribe(onClickEvent -> {
//                        checkNotNull(tapaListener, "Must implement OpenTapaListener");
//                        tapaListener.open(tapa);
                    });
        }


        public interface OpenTapaListener {
            void open(Tapa tapa);
        }

    }

}
