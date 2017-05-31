package gang.com.screencontrol.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import gang.com.screencontrol.R;

/**
 * Created by xiaogangzai on 2017/5/31.
 */

public class Media_Fragment extends Fragment {
    public static Media_Fragment instance = null;//给代码加一个单例模式

    public static Media_Fragment getInstance() {
        if (instance == null) {
            instance = new Media_Fragment();
        }
        return instance;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_fragment_media, container, false);
        return view;
    }
}
