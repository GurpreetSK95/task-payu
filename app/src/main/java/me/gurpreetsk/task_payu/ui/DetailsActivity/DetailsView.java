package me.gurpreetsk.task_payu.ui.DetailsActivity;

/**
 * Created by Gurpreet on 11/08/17.
 */

interface DetailsView {

    void showLoading();

    void hideLoading();

    void openLink();

    void setImage(String image);

    void showErrorMessage(String error);

}
