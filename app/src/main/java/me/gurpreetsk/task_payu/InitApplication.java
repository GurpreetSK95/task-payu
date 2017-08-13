package me.gurpreetsk.task_payu;

import android.app.Application;

import com.facebook.stetho.Stetho;

import me.gurpreetsk.task_payu.dagger.AppComponent;
import me.gurpreetsk.task_payu.dagger.AppModule;
import me.gurpreetsk.task_payu.dagger.DaggerAppComponent;
import me.gurpreetsk.task_payu.dagger.NetModule;
import me.gurpreetsk.task_payu.dagger.PresenterModule;

public class InitApplication extends Application {

    private AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        appComponent = initDagger(this);
        Stetho.initializeWithDefaults(this);
    }

    protected AppComponent initDagger(InitApplication application) {
        return DaggerAppComponent.builder()
                .appModule(new AppModule(application))
                .netModule(new NetModule())
                .presenterModule(new PresenterModule())
                .build();
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }

}
