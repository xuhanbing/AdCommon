package com.hanbing.sample;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.LruCache;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.adcommon.AdEngine;
import com.adcommon.WapsAdEngine;
import com.adcommon.YoumiAdEngine;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.List;

import cn.waps.AdInfo;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    public  void log(String msg)
    {
        System.err.println(msg);

        TextView textView = (TextView) findViewById(R.id.tv_msg);

        textView.setText(msg);

    }

//    AdEngine adEngine = new WapsAdEngine();

    AdEngine adEngine = new YoumiAdEngine();

    WapsAdEngine.AdListener2 adListener = new WapsAdEngine.AdListener2(){
        @Override
        public void onBannerSuccess() {
            super.onBannerSuccess();
            log("onBannerSuccess");
        }

        @Override
        public void onBannerSwitch() {
            super.onBannerSwitch();
            log("onBannerSwitch");
        }

        @Override
        public void onPopSuccess() {
            super.onPopSuccess();
            log("onPopSuccess");
        }

        @Override
        public void onPopClick(boolean isWebPath) {
            super.onPopClick(isWebPath);
            log("onPopClick:" + isWebPath);
        }

        @Override
        public void onBannerClose() {
            super.onBannerClose();

            log("onBannerClose");
        }

        @Override
        public void onBannerFailure() {
            super.onBannerFailure();
            log("onBannerNoData");
        }

        @Override
        public void onGetConfig(String key, String value) {
            super.onGetConfig(key, value);
            log("onGetConfig:key=" + key + ",value=" + value);

            TextView textView = (TextView) findViewById(R.id.tv_config);

            textView.setText("key=" + key + ",value=" + value);
        }

        @Override
        public void onPointsWallClose() {
            super.onPointsWallClose();
            log("onPointsWallClose");
        }

        @Override
        public void onPopClose() {
            super.onPopClose();
            log("onPopClose");
        }

        @Override
        public void onPopFailure() {
            super.onPopFailure();
            log("onPopNoData");
        }

        @Override
        public void onFailure(String message) {
            super.onFailure(message);
            log("onUpdatePointsFailure:" + message);
        }

        @Override
        public void onGetPoints(float amount) {
            super.onGetPoints(amount);
            log("onUpdatePointsSuccess:amount=" + amount);

            TextView textView = (TextView) findViewById(R.id.btn_points);

            textView.setText("积分：" + amount);
        }

        @Override
        public void onGetAdInfoList(List<AdInfo> adInfoList) {
            super.onGetAdInfoList(adInfoList);
            log("onGetAdInfoList");

            showAdInfoList(adInfoList);
        }

        @Override
        public void onGetAdInfo(AdInfo adInfo) {
            super.onGetAdInfo(adInfo);
            log("onGetAdInfo");

            showAdInfo(adInfo);
        }
    };

    class AdInfoListFragment extends AppCompatDialogFragment {

        List<AdInfo> adInfoList;

        public void setAdInfoList(List<AdInfo> adInfoList) {
            this.adInfoList = adInfoList;
        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


//            final LruCache<String, Bitmap> cache = new LruCache<String, Bitmap>(1024) {
//                @Override
//                protected int sizeOf(String key, Bitmap value) {
//                    return value.getWidth() * value.getHeight() * 4;
//                    return value.getWidth() * value.getHeight() * 4;
//                }
//            };
//
//
//            RequestQueue queue = Volley.newRequestQueue(getApplicationContext(), new);
//            ImageLoader imageLoader = new ImageLoader(queue, new ImageLoader.ImageCache() {
//                @Override
//                public Bitmap getBitmap(String url) {
//                    return cache.get(url);
//                }
//
//                @Override
//                public void putBitmap(String url, Bitmap bitmap) {
//                    cache.put(url, bitmap);
//                }
//            });

            final RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

            final ImageLoader imageLoader = new ImageLoader(queue, new ImageLoader.ImageCache() {


                final LruCache<String, Bitmap> cache = new LruCache<String, Bitmap>(10 * 1024 * 1024) {
                @Override
                protected int sizeOf(String key, Bitmap value) {
                    return value.getWidth() * value.getHeight() * 4;
                }
            };

                @Override
                public Bitmap getBitmap(String url) {
                    return cache.get(url);
                }

                @Override
                public void putBitmap(String url, Bitmap bitmap) {
                    cache.put(url, bitmap);
                }
            });




            RecyclerView recyclerView = new RecyclerView(getApplicationContext());

            recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            recyclerView.setAdapter(new RecyclerView.Adapter<ViewHolder>(){

                @Override
                public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

                    View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.layout_ad_info, null);


                    return new ViewHolder(view);
                }

                @Override
                public void onBindViewHolder(ViewHolder holder, int position) {


                    final AdInfo adInfo = adInfoList.get(position);

                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            adEngine.clickAd(MainActivity.this, adInfo.getAdId(), adListener);

                        }
                    });

                    holder.action.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            adEngine.downloadAd(MainActivity.this, adInfo.getAdId(), adListener);
                        }
                    });



                    holder.adId.setText(adInfo.getAdId());
                    holder.adName.setText(adInfo.getAdName());
                    holder.adText.setText(adInfo.getAdText());
                    holder.adIcon.setImageBitmap(adInfo.getAdIcon());
                    holder.adPoint.setText("" + adInfo.getAdPoints());
                    holder.description.setText(adInfo.getDescription());
                    holder.version.setText(adInfo.getVersion());
                    holder.fileSize.setText(adInfo.getFilesize());
                    holder.provider.setText(adInfo.getProvider());
                    holder.adPackage.setText(adInfo.getAdPackage());
                    holder.action.setText(adInfo.getAction());

                    String[] imageUrls = adInfo.getImageUrls();

                    holder.adImageLayout.removeAllViews();
                    if (null != imageUrls){

                        for (String url : imageUrls) {

                            final ImageView imageView = new ImageView(getApplicationContext());
                            imageView.setLayoutParams(new ViewGroup.LayoutParams(200, 200));


                            ImageLoader.ImageListener imageListener =  new ImageLoader.ImageListener() {
                                @Override
                                public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                                    imageView.setImageBitmap(response.getBitmap());
                                }

                                @Override
                                public void onErrorResponse(VolleyError error) {

                                }
                            };

                            imageLoader.get(url, imageListener);

//                            ImageRequest imageRequest = new ImageRequest(url, new Response.Listener<Bitmap>() {
//                                @Override
//                                public void onResponse(Bitmap response) {
//                                    imageView.setImageBitmap(response);
//                                }
//                            }, 0, 0, ImageView.ScaleType.CENTER_CROP, Bitmap.Config.ARGB_8888, imageListener);
//
//
//                            queue.add(imageRequest);


                            holder.adImageLayout.addView(imageView);
                        }

                    }
                }

                @Override
                public int getItemCount() {
                    return adInfoList.size();
                }
            });

            return  recyclerView;
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            TextView adId;
            TextView adName;
            TextView adText;
            ImageView adIcon;
            TextView adPoint;
            TextView description;
            TextView version;
            TextView fileSize;
            TextView provider;
            ViewGroup adImageLayout;
            TextView adPackage;
            TextView action;

            public ViewHolder(View itemView) {
                super(itemView);


                adId = (TextView) itemView.findViewById(R.id.tv_ad_id);
                adName = (TextView) itemView.findViewById(R.id.tv_ad_name);
                adText = (TextView) itemView.findViewById(R.id.tv_ad_text);
                adIcon = (ImageView) itemView.findViewById(R.id.iv_ad_icon);
                adPoint = (TextView) itemView.findViewById(R.id.tv_ad_point);
                description = (TextView) itemView.findViewById(R.id.tv_ad_description);
                version = (TextView) itemView.findViewById(R.id.tv_ad_version);
                fileSize = (TextView) itemView.findViewById(R.id.tv_ad_filesize);
                provider = (TextView) itemView.findViewById(R.id.tv_ad_provider);
                adImageLayout = (ViewGroup) itemView.findViewById(R.id.layout_ad_image_urls);
                adPackage = (TextView) itemView.findViewById(R.id.tv_ad_package);
                action = (TextView) itemView.findViewById(R.id.tv_ad_action);
            }
        }
    }

    private void showAdInfoList(List<AdInfo> adInfoList) {

        AdInfoListFragment fragment = new AdInfoListFragment();
        fragment.setAdInfoList(adInfoList);

        getSupportFragmentManager().beginTransaction().add(fragment, "ads")
        .commit();

    }

    private void showAdInfo(AdInfo adInfo) {

        List<AdInfo> adInfoList = new ArrayList<>();

        adInfoList.add(adInfo);

        showAdInfoList(adInfoList);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        init();
    }

    private void init() {


        if (adEngine instanceof  WapsAdEngine)
        {
            WapsAdEngine wapsAdEngine = (WapsAdEngine)adEngine;
            wapsAdEngine.setEnablePopBack(true)
                    .setEnablePopAd(true)
                    .setEnableCustomAd(true)
                    .setMiniAdBackgroundColor(this, Color.BLACK)
                    .setMiniAdForegroundColor(this, Color.GRAY);

        }

        adEngine.init(this, adListener);

        LinearLayout bannerLayout = (LinearLayout) findViewById(R.id.layout_banner);
        LinearLayout miniLayout = (LinearLayout) findViewById(R.id.layout_mini);

        adEngine.showBannerAd(this, bannerLayout, adListener);
        adEngine.showMiniAd(this, miniLayout, 10, adListener);

        adEngine.getPoints(this, null);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.btn_splash:
            {
                adEngine.showSplashPopAd(this, MainActivity.class, null, adListener);
            }
                break;
            case R.id.btn_splash_layout:
            {
                final LinearLayout layout = new LinearLayout(this);
                addContentView(layout, new ViewGroup.LayoutParams(-1, -1));
                adEngine.showSplashPopAd(this, MainActivity.class, layout, adListener);
            }
                break;
            case R.id.btn_pop:
                adEngine.showPopAd(this, null, adListener);
                break;
            case R.id.btn_pop_layout:
            {
                final LinearLayout layout = new LinearLayout(this);
                layout.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        return true;
                    }
                });
                addContentView(layout, new ViewGroup.LayoutParams(-1, -1));
                adEngine.showPopAd(this, layout, adListener);
            }
                break;
            case R.id.btn_award:
                adEngine.awardPoints(this, null, 10);
                break;
            case R.id.btn_spend:
                adEngine.spendPoints(this, null, 10);
                break;
            case R.id.btn_offer:
                AdEngine.PointsWallAdType type = AdEngine.PointsWallAdType.ALL;
                adEngine.showPointsWallAd(this, null, type, false, adListener);
                break;
            case R.id.btn_offer_dialog:
                type = AdEngine.PointsWallAdType.ALL;
                adEngine.showPointsWallAd(this, null, type, true, adListener);
                break;
            case R.id.btn_offer_app:
                type = AdEngine.PointsWallAdType.APP;
                adEngine.showPointsWallAd(this, null, type, false, adListener);
                break;
            case R.id.btn_offer_game:
                type = AdEngine.PointsWallAdType.GAME;
                adEngine.showPointsWallAd(this, null, type, false, adListener);
                break;
            case R.id.btn_offer_share:
                type = AdEngine.PointsWallAdType.SHARE;
                adEngine.showPointsWallAd(this, null, type, false, adListener);
                break;
            case R.id.btn_offer_share_dialog:
                type = AdEngine.PointsWallAdType.SHARE;
                adEngine.showPointsWallAd(this, null, type, true, adListener);
                break;
            case R.id.btn_custom_single:
            {
                adEngine.getCustomAdInfo(this, adListener);
            }
                break;
            case R.id.btn_custom_list:
            {
                adEngine.getCustomAdInfoList(this, adListener);
            }
                break;
            case R.id.btn_self:
                adEngine.showMore(this, "c8c3dab81e65e695020e69a74ccff196", adListener);
                break;
            case R.id.btn_self_list:
                adEngine.showMore(this, null, adListener);
                break;
            case R.id.btn_get_config:
            {
                adEngine.getConfig(this, "showAd", adListener);
            }
                break;
            case R.id.btn_points:
                adEngine.getPoints(this, null);
                break;

        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (KeyEvent.KEYCODE_BACK == event.getKeyCode())
        {
            if (adEngine instanceof YoumiAdEngine)
            {
                YoumiAdEngine youmiAdEngine = (YoumiAdEngine) adEngine;

                if (youmiAdEngine.dismissPopAd(this))
                    return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    protected void onDestroy() {
        if (adEngine instanceof YoumiAdEngine)
        {
            YoumiAdEngine youmiAdEngine = (YoumiAdEngine) adEngine;

            youmiAdEngine.onDestory(this);
        }
        super.onDestroy();
    }
}
