package com.brunix.quieromi.tapalist.ui;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.brunix.quieromi.R;
import com.brunix.quieromi.data.entity.Tapa;
import com.jakewharton.rxbinding.view.RxView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.functions.Action1;

import static com.jakewharton.rxbinding.internal.Preconditions.checkNotNull;

/**
 * Created by dolo on 10/6/16.
 */

public class TapaAdapter extends RecyclerView.Adapter<TapaAdapter.TapaViewHolder> {

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

    public void addNewTapa(Tapa tapa) {
        tapas.add(tapa);
        notifyDataSetChanged();
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
    }


    public static class TapaViewHolder extends RecyclerView.ViewHolder {
        private static final int MAX_WIDTH = 200;
        private static final int MAX_HEIGHT = 200;

        @BindView(R.id.tapaImageView)
        ImageView mTapaImageView;
        @BindView(R.id.tapaNameTextView)
        TextView mNameTextView;
        @BindView(R.id.categoryTextView)
        TextView mCategoryTextView;
        @BindView(R.id.ratingTextView)
        TextView mRatingTextView;

        private final View view;

        private Tapa tapa;

        public TapaViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            view = itemView;
        }

        public void bindTapa(Tapa tapa, Picasso picasso) {
            this.tapa = tapa;
            // TODO Usar como singleton
            picasso.load(tapa.getImageUrl())
                    .resize(MAX_WIDTH, MAX_HEIGHT)
                    .centerCrop()
                    .into(mTapaImageView);

            mNameTextView.setText(tapa.getName());
            mCategoryTextView.setText("Category of the tapa");
            mRatingTextView.setText("Rating: " + tapa.getLongitude() + "/5");

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
