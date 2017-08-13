package me.gurpreetsk.task_payu.ui.DetailsActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.gurpreetsk.task_payu.InitApplication;
import me.gurpreetsk.task_payu.R;
import me.gurpreetsk.task_payu.data.model.FavoritesTable;
import me.gurpreetsk.task_payu.data.model.Project;
import me.gurpreetsk.task_payu.data.model.ProjectsTable;
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
    @BindView(R.id.imagebutton_fav)
    ImageButton favButton;
    @BindView(R.id.toolbar_details)
    Toolbar toolbar;

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

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        presenter.setView(this);

        project = gson.fromJson(getIntent().getStringExtra(Constants.KEY_PROJECT), Project.class);

        presenter.getImage(Constants.KICKSTARTER_BASE_URL + project.getUrl());
        setUi();

    }

    private void setUi() {
        setTitle(project.getTitle());
        textviewStrength.setText("Amount pledged: " + project.getCurrency().toUpperCase() + " "
                + project.getAmtPledged() + ", backed by " + project.getNumBackers() + " individuals.");
        textviewDescription.setText(project.getBlurb());
        textviewOwnerOrigin.setText("Started by " + project.getBy()
                + " in " + project.getLocation() + ", " + project.getCountry());
        Cursor isFav = getContentResolver().query(FavoritesTable.CONTENT_URI, null,
                "title = \"" + project.getTitle() + "\"", null, null);
        try {
            if (isFav != null && isFav.getCount() == 1)
                favButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_favorite_red));
            else
                favButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_favorite_white));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (isFav != null)
                isFav.close();
        }
    }

    @OnClick(R.id.imagebutton_fav)
    public void manipulateFavDB() {
        try {
            getContentResolver().insert(FavoritesTable.CONTENT_URI,
                    ProjectsTable.getContentValues(project, true));
            favButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_favorite_red));
            Toast.makeText(this, "Favourite added!", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
//            if (e instanceof SQLiteConstraintException) {
            showErrorMessage("Favourite removed");
            favButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_favorite_white));
            getContentResolver()
                    .delete(FavoritesTable.CONTENT_URI, "title = \"" + project.getTitle() + "\"", null);
        }
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
