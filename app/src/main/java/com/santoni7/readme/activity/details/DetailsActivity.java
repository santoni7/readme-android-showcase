package com.santoni7.readme.activity.details;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.santoni7.readme.MyApplication;
import com.santoni7.readme.R;
import com.santoni7.readme.common.OnSwipeTouchListener;
import com.santoni7.readme.dagger.MyComponent;
import com.santoni7.readme.data.Person;

import io.reactivex.Observable;
import io.reactivex.Single;

public class DetailsActivity extends AppCompatActivity implements DetailsContract.View {

    public static final String EXTRA_PERSON_ID = "person_id";
    private static final String TAG = DetailsActivity.class.getSimpleName();

    private DetailsPresenter presenter;

    TextView txtName;
    TextView txtAge;
    ImageView imgAvatar;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);

        presenter = new DetailsPresenter();
        presenter.attachView(this);

        initView();

        MyComponent component = ((MyApplication) getApplication()).getComponent();
        presenter.init(component);
        presenter.viewReady();
    }

    private void initView() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        toolbar.setTitle(R.string.details_activity_title);

        txtName = findViewById(R.id.txtName);
        txtAge = findViewById(R.id.txtAge);
        imgAvatar = findViewById(R.id.imgAvatar);

        findViewById(R.id.rootLayout).setOnTouchListener(new OnSwipeTouchListener(this) {
            @Override
            public void onSwipeRight() {
                finish();
            }
        });
    }

    @Override
    public void displayPerson(Person person) {
        toolbar.setSubtitle(person.getFullName());
        txtName.setText(person.getFullName());
        txtAge.setText(getString(R.string.age_string_format, person.getAge()));
        if(person.getImage() != null) {
            imgAvatar.setImageBitmap(person.getImage());
//            Bitmap img = person.getImage();
//            img.reconfigure(40, 40, Bitmap.Config.ARGB_8888);
//            Drawable d = new BitmapDrawable(getResources(), img);
//            d.set
//            toolbar.setLogo(d);
        }
    }


    @Override
    public Single<String> getPersonIdExtra() {
        Intent i = getIntent();
        String id = i.getStringExtra(EXTRA_PERSON_ID);
        return Observable.just(id).singleOrError();
    }

    @Override
    public void finish() {
        super.finish();

        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onStop() {
        super.onStop();
        presenter.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }
}
