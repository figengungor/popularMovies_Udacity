package com.figengungor.popularmovies.ui.movieDetail.cast;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;

import com.figengungor.popularmovies.R;
import com.figengungor.popularmovies.data.model.Credits;
import com.figengungor.popularmovies.data.model.Crew;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by figengungor on 3/18/2018.
 */

public class CastLayout extends LinearLayout {

    @BindView(R.id.castRv)
    RecyclerView castRv;
    @BindView(R.id.emptyMessageTv)
    AppCompatTextView emptyMessageTv;
    @BindView(R.id.directorTv)
    AppCompatTextView directorTv;
    @BindView(R.id.writerTv)
    AppCompatTextView writerTv;

    public CastLayout(Context context) {
        super(context);
        init(context);
    }

    public CastLayout(Context context, CastAdapter.ItemListener itemListener, Credits credits) {
        super(context);
        init(context);
        if (credits.getCast() == null || credits.getCast().size() == 0) {
            emptyMessageTv.setVisibility(View.VISIBLE);
        } else {
            emptyMessageTv.setVisibility(View.GONE);
            CastAdapter adapter = new CastAdapter(credits.getCast(), context);
            adapter.setItemListener(itemListener);
            castRv.setAdapter(adapter);
            castRv.setNestedScrollingEnabled(false);
        }
        List<Crew> crewList = credits.getCrew();
        List<Crew> directorList = new ArrayList<>();
        List<Crew> writerList = new ArrayList<>();
        if(crewList!=null) {
            for(Crew crew: crewList){
                if(crew.getJob().equalsIgnoreCase("Director")){
                    directorList.add(crew);
                } else if(crew.getJob().equalsIgnoreCase("Screenplay")){
                    writerList.add(crew);
                }
            }
            if(directorList.size()>0){
                directorTv.setText(TextUtils.join(", ", directorList));
            }
            if(writerList.size()>0){
                writerTv.setText(TextUtils.join(", ", writerList));
            }
        }
    }

    private void init(Context context) {
        View view = inflate(context, R.layout.layout_cast, this);
        ButterKnife.bind(view);
    }

}
