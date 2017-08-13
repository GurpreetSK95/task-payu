package me.gurpreetsk.task_payu.ui.MainActivity;

import java.util.List;

import me.gurpreetsk.task_payu.data.model.Project;

/**
 * Interface defining user's interactions with the view for MainActivity
 */

interface MainView {

    void showLoading();

    void hideLoading();

    void showProjects();

    void showErrorMessage(String error);

    void insertInDb(List<Project> projects);
}
