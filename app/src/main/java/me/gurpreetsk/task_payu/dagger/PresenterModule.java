package me.gurpreetsk.task_payu.dagger;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import me.gurpreetsk.task_payu.ui.DetailsActivity.DetailsPresenter;
import me.gurpreetsk.task_payu.ui.DetailsActivity.DetailsPresenterImpl;
import me.gurpreetsk.task_payu.ui.FavouritesActivity.FavouritesPresenter;
import me.gurpreetsk.task_payu.ui.FavouritesActivity.FavouritesPresenterImpl;
import me.gurpreetsk.task_payu.ui.MainActivity.MainPresenter;
import me.gurpreetsk.task_payu.ui.MainActivity.MainPresenterImpl;


@Module
public class PresenterModule {

    @Provides
    @Singleton
    MainPresenter provideMainPresenter(Context context) {
        return new MainPresenterImpl(context);
    }

    @Provides
    @Singleton
    DetailsPresenter provideDetailsPresenter(Context context) {
        return new DetailsPresenterImpl(context);
    }

    @Provides
    @Singleton
    FavouritesPresenter providesFavouritesPresenter(Context context) {
        return new FavouritesPresenterImpl(context);
    }

}
