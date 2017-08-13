package me.gurpreetsk.task_payu.ui.DetailsActivity;

import android.content.Context;
import android.util.Log;

import com.squareup.picasso.Picasso;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import me.gurpreetsk.task_payu.InitApplication;
import me.gurpreetsk.task_payu.util.Constants;

/**
 * Created by Gurpreet on 11/08/17.
 */

public class DetailsPresenterImpl implements DetailsPresenter {

    private DetailsView view;

    private static final String TAG = DetailsPresenterImpl.class.getSimpleName();


    public DetailsPresenterImpl(Context context) {
        ((InitApplication) context).getAppComponent().inject(this);
    }

    @Override
    public void setView(DetailsView view) {
        this.view = view;
    }

    @Override
    public void getImage(String url) {
        Observable.create((ObservableOnSubscribe<String>) emitter ->
                emitter.onNext(url))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(s -> view.showLoading())
                .observeOn(Schedulers.io())
                .map(s -> {
                    String imageUrl = "";
                    try {
                        Document doc = Jsoup.connect(s).get();
                        Elements img = doc.getElementsByClass("js-feature-image");
                        imageUrl = img.get(0).absUrl("src");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return imageUrl;
                })
                .retry(5)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    Disposable d;

                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        this.d = d;
                    }

                    @Override
                    public void onNext(@NonNull String s) {
                        Log.d(TAG, "onNext: " + s);
                        try {
                            view.setImage(s);
                            view.hideLoading();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.e(TAG, "onError: ", e);
                        view.showErrorMessage(e.getMessage());
                        view.hideLoading();
                    }

                    @Override
                    public void onComplete() {
                        if (!d.isDisposed())
                            d.dispose();
                        view.hideLoading();
                    }
                });
    }

}
