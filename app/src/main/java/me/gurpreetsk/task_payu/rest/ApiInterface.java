package me.gurpreetsk.task_payu.rest;

import java.util.List;

import io.reactivex.Observable;
import me.gurpreetsk.task_payu.data.model.Project;
import retrofit2.http.GET;

public interface ApiInterface {

    @GET("/kickstarter")
    Observable<List<Project>> getProjects();

}
