package ai.portfolio.dev.project.app.com.tictactoe.Objects;

import android.content.Context;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceViewHolder;
import android.util.AttributeSet;

import ai.portfolio.dev.project.app.com.tictactoe.R;

public class TextColorChooserPreference extends Preference
{


    public TextColorChooserPreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public TextColorChooserPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TextColorChooserPreference(Context context) {
        super(context);
    }

    public TextColorChooserPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setWidgetLayoutResource(R.layout.pref_custom_text_color_chooser);
    }

    @Override
    public void onBindViewHolder(PreferenceViewHolder holder) {
        super.onBindViewHolder(holder);
        holder.itemView.setClickable(false); // disable parent click
        /*View button = holder.findViewById(R.id.);
        button.setClickable(true); // enable custom view click
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // persist your value here
            }
        });
        // the rest of the click binding*/
    }
}
