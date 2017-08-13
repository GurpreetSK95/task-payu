package me.gurpreetsk.task_payu.ui.MainActivity;

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

public class MainPresenterImpl implements MainPresenter {

    private MainView view;
    @Inject
    ApiInterface apiClient;

    private static final String TAG = MainPresenterImpl.class.getSimpleName();


    public MainPresenterImpl(Context context) {
        ((InitApplication) context).getAppComponent().inject(this);
    }

    @Override
    public void setView(MainView view) {
        this.view = view;
    }

    @Override
    public void getProjects() {
        apiClient.getProjects()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .retry(3)
                .subscribe(new Observer<List<Project>>() {
                    Disposable d;

                    @Override
                    public void onSubscribe(Disposable d) {
                        this.d = d;
                    }

                    @Override
                    public void onNext(List<Project> projects) {
                        view.showLoading();
                        try {
                            view.insertInDb(projects);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError: ", e);
                        view.showErrorMessage(e.getMessage());
                        view.hideLoading();
                    }

                    @Override
                    public void onComplete() {
                        if (!d.isDisposed())
                            d.dispose();
                        view.hideLoading();
                        view.showProjects();
                    }
                });
    }

}
