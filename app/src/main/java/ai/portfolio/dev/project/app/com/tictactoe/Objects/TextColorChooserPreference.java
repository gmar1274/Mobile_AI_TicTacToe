package ai.portfolio.dev.project.app.com.tictactoe.Objects;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.Preference;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import ai.portfolio.dev.project.app.com.tictactoe.R;

public class TextColorChooserPreference extends Preference implements ColorPickerDialog.OnColorChangedListener
{

    private ImageView mImageView;
    private Color mColor;
    private TextView mTextView;

    public TextColorChooserPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public TextColorChooserPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TextColorChooserPreference(Context context) {
        super(context);
    }
    @Override
    protected View onCreateView(ViewGroup parent ) {
        super.onCreateView(parent);
        LayoutInflater li = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = li.inflate(R.layout.pref_custom_text_color_chooser, parent, false);

        String name = this.getNameFromKey();
        Log.e("NAME_PREF: ",this.getKey()==null?"NULL":this.getKey() +" ... name: " +name);
        mTextView = (TextView)view.findViewById(R.id.pref_text);
        mTextView.setText(name);
        mTextView.setTextColor(this.getColor());
        mTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                saveByKey(getKey(),s.toString());
            }
        });

        mImageView = (ImageView)view.findViewById(R.id.image_view_color);
        mImageView.setBackgroundColor(this.getColor());
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ColorPickerDialog colorPickerDialog = new ColorPickerDialog(getContext(),TextColorChooserPreference.this,getContext().getResources().getColor(R.color.colorPlayerOne));
                colorPickerDialog.show();
            }
        });

        return view;
    }

    private void saveByKey(String key, Object val) {
        SharedPreferences.Editor editor = this.getSharedPreferences().edit();
        if(val instanceof String)
        editor.putString(key,(String)val);
        else if(val instanceof Integer)
            editor.putInt(key,(Integer)val);
        editor.commit();
    }

    @Override
    public void colorChanged(int color) {
        mTextView.setTextColor(color);
        mImageView.setBackgroundColor(color);

        String key = null;
        if (this.getKey().equalsIgnoreCase(this.getContext().getString(R.string.pref_player_name_one_key))) {
            key = this.getContext().getString(R.string.pref_player_one_color_key);
        } else {
            key = this.getContext().getString(R.string.pref_player_two_color_key);
        }
        saveByKey(key,color);
    }
    private int getColor(){
        final String player_one_key= this.getContext().getString(R.string.pref_player_name_one_key);
        //String player_two_key = this.getContext().getString(R.string.pref_player_name_two_key);
        String key = this.getKey();
        if(key.equalsIgnoreCase(player_one_key)){
            return this.getSharedPreferences().getInt(this.getContext().getString(R.string.pref_player_one_color_key),this.getContext().getResources().getColor(R.color.colorPlayerOne));
        }else
        {
            return this.getSharedPreferences().getInt(this.getContext().getString(R.string.pref_player_two_color_key),this.getContext().getResources().getColor(R.color.colorPlayerTwo));
        }
    }

    public String getNameFromKey() {
        String key = this.getKey().toString();
        String def = key.contains("one")?"Player 1":"ROVER";//defau;t val
        String name = this.getSharedPreferences().getString(key,def);
        return name;
    }
    /*@Override
    protected void onBindView(View view) {

        super.onBindView(view);

        view.setClickable(true);

        if (view != null && !TextUtils.isEmpty(url)) {
            NetworkImageView gravatar = ((NetworkImageView) view.findViewById(R.id.gravatar));
            if (gravatar != null) {
                gravatar.setImageUrl(url, App.getInstance().getImageLoader());
                gravatar.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.i("test","clicked");
                    }
                }
            }
        }
    }*/
}
