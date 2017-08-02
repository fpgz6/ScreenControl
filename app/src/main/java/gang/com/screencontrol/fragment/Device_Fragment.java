package gang.com.screencontrol.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import gang.com.screencontrol.R;
import gang.com.screencontrol.adapter.BaseAdapter;
import gang.com.screencontrol.adapter.DeviceAdapter;
import gang.com.screencontrol.bean.DeviceBean;
import gang.com.screencontrol.defineview.DividerItemDecoration;
import gang.com.screencontrol.potting.BaseFragment;
import gang.com.screencontrol.service.MainService;
import gang.com.screencontrol.util.LogUtil;
import okhttp3.WebSocket;

/**
 * Created by xiaogangzai on 2017/5/31.
 */

public class Device_Fragment extends BaseFragment implements MainService.MessageCallBackListener {
    public static Device_Fragment instance = null;
    private RecyclerView recycler_device;
    private DeviceAdapter deviceadapter;
    private List<DeviceBean> data = new ArrayList<>();
    private Gson gson = new Gson();
    private WebSocket webSocket;
    public static Device_Fragment getInstance() {
        if (instance == null) {
            instance = new Device_Fragment();
        }
        return instance;
    }
    /**
     * 标志位，标志已经初始化完成
     */
    private boolean isPrepared;
    /**
     * 是否已被加载过一次，第二次就不再去请求数据了
     */
    private boolean mHasLoadedOnce;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_fragment_device, container, false);
        recycler_device = (RecyclerView) view.findViewById(R.id.recyle_device);
        isPrepared=true;
        lazyLoad();
        return view;
    }

    private void getData() {
        //接口回调，调用发送websocket接口
         webSocket = MainService.getWebSocket();
        if (null != webSocket) {
            MainService.addListener(this);
            webSocket.send("    {\n" +
                    "       \"body\" : {\n" +
                    "          \"typeid\" : [ 0 ]\n" +
                    "       },\n" +
                    "       \"guid\" : \"M-117\",\n" +
                    "       \"type\" : \"GETDEVICETYPEFOLDERLIST\"\n" +
                    "    }");
        }
    }

    private void showView() {
        deviceadapter = new DeviceAdapter(getActivity(), data);
        recycler_device.setAdapter(deviceadapter);
        recycler_device.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL));
        recycler_device.setItemAnimator(new DefaultItemAnimator());
        recycler_device.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.HORIZONTAL_LIST));
        deviceadapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void onClick(View v, int position) {

            }
        });
    }

    @Override
    public void onRcvMessage(final String text) {
        LogUtil.d("获取的所有设备list",text);
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject allmodelobject = new JSONObject(text);
                    if (allmodelobject.getString("type").equals("GETDEVICETYPEFOLDERLIST")) {
                        String bodystring = allmodelobject.getString("body");
                        JSONObject basicInfoboj = new JSONObject(bodystring);
                        basicInfoboj.getString("deviceFolderInfo");
                        LogUtil.d("获取的所有设备list", basicInfoboj.getString("deviceFolderInfo"));
                      List<DeviceBean> ps = gson.fromJson(basicInfoboj.getString("deviceFolderInfo"), new TypeToken<List<DeviceBean>>() {
                        }.getType());
                        data = ps;
                        mHasLoadedOnce=true;
                        showView();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    /**
     * 取消预加载方法的实现
     */
    @Override
    protected void lazyLoad() {
        if (!isPrepared || !isVisible || mHasLoadedOnce) {
            return;
        }
        getData();
    }

    @Override
    public void onDestroyView() {
        MainService.removeListener(this);
        super.onDestroyView();
    }


}
