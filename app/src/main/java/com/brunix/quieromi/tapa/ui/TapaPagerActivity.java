package com.brunix.quieromi.tapa.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.brunix.quieromi.R;
import com.brunix.quieromi.ViewIdManager;
import com.brunix.quieromi.application.MyApplication;
import com.brunix.quieromi.data.entity.Tapa;
import com.brunix.quieromi.tapalist.presenter.TapaListPresenterImpl;
import com.brunix.quieromi.tapalist.view.TapaListView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by dolo on 9/21/16.
 */

public class TapaPagerActivity extends AppCompatActivity implements TapaListView {

    private final static String TAG = TapaPagerActivity.class.getSimpleName();

    private static final String EXTRA_TAPA_ID =
            "com.brunix.quieromi.ui.tapa_id";

    @BindView(R.id.activity_tapa_pager_view_pager)
    ViewPager viewPager;

//    private List<Tapa> tapas;

    private TapaPagerAdapter adapter;

    private TapaListPresenterImpl presenter;

    private String selectedTapaId;
    private int viewId;

//    @Inject
//    Dao dao;

    public static Intent newIntent(Context packageContext, String tapaId) {
        Intent intent = new Intent(packageContext, TapaPagerActivity.class);
        intent.putExtra(EXTRA_TAPA_ID, tapaId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewId = ViewIdManager.getViewId(this);
        setContentView(R.layout.activity_tapa_pager);
        ButterKnife.bind(this);

        // Inject components
        MyApplication.get(this).getApplicationComponent().inject(this);

        final String tapaId = (String) getIntent()
                .getSerializableExtra(EXTRA_TAPA_ID);

        selectedTapaId = tapaId;

        //viewPager = (ViewPager) findViewById(R.id.activity_tapa_pager_view_pager);

        initPresenter();
        initAdapter();
        initUI();

        refreshList();
    }

    private void initPresenter() {
        presenter = new TapaListPresenterImpl(MyApplication.get(this).getDatabaseHelper()); //, MyApplication.get(getActivity()).getAuthenticationHelper());
        presenter.setView(this);
    }

    private void initAdapter() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        adapter = new TapaPagerAdapter(fragmentManager);
        viewPager.setAdapter(adapter);
    }

    private void initUI() {
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) { }

            @Override
            public void onPageSelected(int position) {
//                Tapa tapa = tapas.get(position);
                adapter.getItem(position);
//                presenter.
//                if (tapa.getName() != null) {
//                    setTitle(tapa.getName());
//                }
            }

            @Override
            public void onPageScrollStateChanged(int state) { }
        });
    }

    private void refreshList() {
        adapter.clearTapas();
        presenter.requestTapasFromNetwork(viewId);
//        viewPager.setCurrentItem(0);
//        adapter.updateTapas(tapas);
    }

    @Override
    public void sendTapaToAdapter(Tapa tapa) {
        adapter.addNewTapa(tapa);
        viewPager.setCurrentItem(adapter.getPositionFromId(selectedTapaId));
    }

    @Override
    public void removeTapaFromAdapter(String tapaId) {
//        adapter.removeTapa(tapa);
//        recyclerView.scrollToPosition(adapter.getItemCount() - 1);
    }

    @Override
    public void showErrorMessage() {

    }

    @Override
    public void removeCallbacksFromFirebase() {
        presenter.removeTapaCallbacks();
        //presenter.startTapaNotificationService();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }

    @Override
    public int getViewId() {
        return viewId;
    }

}
