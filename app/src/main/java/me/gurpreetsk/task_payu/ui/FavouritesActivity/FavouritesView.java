package me.gurpreetsk.task_payu.ui.FavouritesActivity;

import java.util.List;

import me.gurpreetsk.task_payu.data.model.Project;

/**
 * Created by Gurpreet on 11/08/17.
 * Interface defining user's interactions with the view for MainActivity
 */

interface FavouritesView {

    void showLoading();

    void hideLoading();

    void showFavourites();

    void showErrorMessage(String error);

}
