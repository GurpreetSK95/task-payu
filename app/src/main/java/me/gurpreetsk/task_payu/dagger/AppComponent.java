package me.gurpreetsk.task_payu.dagger;

import javax.inject.Singleton;

import dagger.Component;
import me.gurpreetsk.task_payu.ui.DetailsActivity.DetailsActivity;
import me.gurpreetsk.task_payu.ui.DetailsActivity.DetailsPresenterImpl;
import me.gurpreetsk.task_payu.ui.FavouritesActivity.FavouritesActivity;
import me.gurpreetsk.task_payu.ui.FavouritesActivity.FavouritesPresenterImpl;
import me.gurpreetsk.task_payu.ui.MainActivity.MainActivity;
import me.gurpreetsk.task_payu.ui.MainActivity.MainPresenterImpl;

@Singleton
@Component(modules = {
        AppModule.class,
        NetModule.class,
        PresenterModule.class
})
public interface AppComponent {

    void inject(MainActivity target);

    void inject(MainPresenterImpl target);

    void inject(DetailsActivity target);

    void inject(DetailsPresenterImpl target);

    void inject(FavouritesActivity target);

    void inject(FavouritesPresenterImpl target);

}
