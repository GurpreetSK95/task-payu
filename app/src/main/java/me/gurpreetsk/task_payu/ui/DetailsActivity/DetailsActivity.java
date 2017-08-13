package me.gurpreetsk.task_payu.ui.DetailsActivity;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import me.gurpreetsk.task_payu.InitApplication;
import me.gurpreetsk.task_payu.R;
import me.gurpreetsk.task_payu.data.model.Project;
import me.gurpreetsk.task_payu.util.Constants;

public class DetailsActivity extends AppCompatActivity implements DetailsView {

    @BindView(R.id.imageview_details)
    ImageView imageviewProjectImage;
    @BindView(R.id.textview_description)
    TextView textviewDescription;
    @BindView(R.id.textview_strength)
    TextView textviewStrength;
    @BindView(R.id.textview_owner_origin)
    TextView textviewOwnerOrigin;
    @BindView(R.id.loading_bar)
    ProgressBar loadingProgressBar;

    @Inject
    Gson gson;
    @Inject
    DetailsPresenter presenter;

    Project project;

    private static final String TAG = DetailsActivity.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((InitApplication) getApplication()).getAppComponent().inject(this);
        setContentView(R.layout.activity_details);
        ButterKnife.bind(this);

        presenter.setView(this);

        project = gson.fromJson(getIntent().getStringExtra(Constants.KEY_PROJECT), Project.class);

        presenter.getImage(Constants.KICKSTARTER_BASE_URL + project.getUrl());
        setUi();

    }

    private void setUi() {
        textviewStrength.setText("Amount pledged: " + project.getCurrency().toUpperCase() + " "
                + project.getAmtPledged() + ", backed by " + project.getNumBackers() + " individuals.");
        textviewDescription.setText(project.getBlurb());
        textviewOwnerOrigin.setText("Started by " + project.getBy()
                + " in " + project.getLocation() + ", " + project.getCountry());
    }

    @Override
    public void showLoading() {
        loadingProgressBar.setIndeterminate(true);
        loadingProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        loadingProgressBar.setVisibility(View.GONE);
    }

    @Override
    @OnClick(R.id.button_view_project)
    public void openLink() {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse(Constants.KICKSTARTER_BASE_URL + project.getUrl()));
        startActivity(browserIntent);
    }

    @Override
    public void setImage(String image) {
        Picasso.with(DetailsActivity.this)
                .load(image)
                .resize(640, 480)
                .into(imageviewProjectImage);
    }

    @Override
    public void showErrorMessage(String error) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }

}
