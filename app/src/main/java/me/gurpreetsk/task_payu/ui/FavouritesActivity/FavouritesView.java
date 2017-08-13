package me.gurpreetsk.task_payu.ui.FavouritesActivity;

/**
 * Interface defining user's interactions with the view for MainActivity
 */

interface FavouritesView {

    void showLoading();

    void hideLoading();

    void showFavourites();

    void showErrorMessage(String error);

}
