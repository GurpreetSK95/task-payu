package me.gurpreetsk.task_payu.dagger;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import me.gurpreetsk.task_payu.ui.MainActivity.MainPresenter;
import me.gurpreetsk.task_payu.ui.MainActivity.MainPresenterImpl;

/**
 * Created by Gurpreet on 11/08/17.
 */

@Module
public class PresenterModule {

    @Provides
    @Singleton
    MainPresenter provideMainPresenter(Context context) {
        return new MainPresenterImpl(context);
    }

//    @Provides
//    @Singleton
//    DetailsPresenter provideDetailsPresenter(Context context) {
//        return new DetailsPresenterImpl(context);
//    }

}
