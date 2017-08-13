package me.gurpreetsk.task_payu.ui.FavouritesActivity;

import android.content.Context;
import android.util.Log;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import me.gurpreetsk.task_payu.InitApplication;
import me.gurpreetsk.task_payu.data.model.Project;
import me.gurpreetsk.task_payu.rest.ApiInterface;

/**
 * Created by Gurpreet on 11/08/17.
 * Presenter for handling all the business logic for MainActivity
 */

public class FavouritesPresenterImpl implements FavouritesPresenter {

    private FavouritesView view;
    @Inject
    ApiInterface apiClient;

    private static final String TAG = FavouritesPresenterImpl.class.getSimpleName();


    public FavouritesPresenterImpl(Context context) {
        ((InitApplication) context).getAppComponent().inject(this);
    }

    @Override
    public void setView(FavouritesView view) {
        this.view = view;
    }

    @Override
    public void getFavourites() {


        view.showFavourites();
    }

}
