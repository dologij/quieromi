package com.brunix.quieromi.tapalist.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.brunix.quieromi.R;
import com.brunix.quieromi.ViewIdManager;
import com.brunix.quieromi.application.MyApplication;
import com.brunix.quieromi.data.RemoteServiceImpl;
import com.brunix.quieromi.data.entity.Tapa;
import com.brunix.quieromi.tapa.ui.TapaPagerActivity;
import com.brunix.quieromi.tapalist.presenter.TapaListPresenterImpl;
import com.brunix.quieromi.tapalist.view.TapaListView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by dolo on 9/20/16.
 */

//public class TapaListFragment extends BaseFragmentOld<TapaListPresenterImpl> implements TapaListView, TapaAdapter.TapaViewHolder.OpenTapaListener{
public class TapaListFragment extends Fragment implements TapaListView {

    private final static String TAG = TapaListFragment.class.getSimpleName();

    @Inject
    Picasso picasso;

    @Inject
    RemoteServiceImpl remoteService;

    @BindView(R.id.fab)
    FloatingActionButton fab;

    @BindView(R.id.tapas_recycler_view)
    RecyclerView recyclerView;

//    @BindView(R.id.tapas_swipe)
//    SwipeRefreshLayout mSwipe;

    private TapaAdapter adapter;

    private TapaListPresenterImpl presenter;

    private Unbinder unbinder;

    private int viewId;


    public static TapaListFragment newInstance() {
        TapaListFragment fragment = new TapaListFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tapa_list, container, false);

    }

    // Code moved from onCreateView(). As per the onViewCreated()'s comment:
    // "Any view setup should happen here. E.g., view lookups, attaching listeners."
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);

        // Inject components
        MyApplication.get(getActivity()).getApplicationComponent().inject(this);

        initPresenter();
        initAdapter();
        initUI();

        refreshList();

    }


    private void initPresenter() {
        presenter = new TapaListPresenterImpl(MyApplication.get(getActivity()).getDatabaseHelper()); //, MyApplication.get(getActivity()).getAuthenticationHelper());
        presenter.setView(this);
    }

    private void initAdapter() {
        if(adapter == null) {
            adapter = new TapaAdapter(new ArrayList<Tapa>(), picasso);
            adapter.setOpenTapaListener(new TapaAdapter.TapaViewHolder.OpenTapaListener() {
                @Override
                public void open(Tapa tapa) {
//                  Toast.makeText(getActivity(), tapa.getId() + "," + tapa.getName() + " clicked...", Toast.LENGTH_SHORT).show();
                    Intent intent = TapaPagerActivity.newIntent(getActivity(),tapa.getId());
                    startActivity(intent);
                }
            });
            recyclerView.setAdapter(adapter);
        }
    }

    private void initUI() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);

//        mSwipe.setOnRefreshListener(this::refreshList);
    }

    private void refreshList() {
        showRefresh(true);

        adapter.clearTapas();
        presenter.requestTapasFromNetwork(viewId);

        showRefresh(false);
    }

    private void showRefresh(boolean show) {
//        mSwipe.setRefreshing(show);
        int visibility = show ? View.GONE : View.VISIBLE;
        recyclerView.setVisibility(visibility);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();

        adapter.cleanup();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        MyApplication.instance.mustDie(this);

        presenter.onDestroy();
        //removeCallbacksFromFirebase();
    }



/*
    @Override
    protected TapaListPresenterImpl getPresenter() {
        if (presenter == null) {
            presenter = new TapaListPresenterImpl();//(RemoteServiceImpl.getInstance());
        }
        return presenter;
    }

    @NonNull
    @Override
    protected View getFallbackView() {
        return fab;
    }
*/

    @Override
    public void sendTapaToAdapter(Tapa tapa) {
        adapter.addTapa(tapa);
        recyclerView.scrollToPosition(0);
    }

    @Override
    public void removeTapaFromAdapter(String tapaId) {
        adapter.removeTapa(tapaId);
        recyclerView.scrollToPosition(adapter.getItemCount() - 1);
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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewIdManager.getViewId(this);
        viewId = ViewIdManager.getViewId(this);
    }

    @Override
    public int getViewId() {
        return viewId;
    }

}
