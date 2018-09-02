package com.notebook.idealabs.deepsearch;

import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

/*import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;*/

public class Search extends AppCompatActivity {
    EditText userquery;
    LinearLayout deep;
    LinearLayout normal;
    LinearLayout specific;
    Spinner website;
    Spinner type;
    Spinner engine;
    Spinner engined;
    Spinner mode;

    //private AdView mAdView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        deep = (LinearLayout) findViewById(R.id.deep_layout);
        deep.setVisibility(View.GONE);
        specific = (LinearLayout) findViewById(R.id.website_layout);
        specific.setVisibility(View.GONE);
        normal = (LinearLayout) findViewById(R.id.search_layout);
        website = (Spinner) findViewById(R.id.website);
        type = (Spinner) findViewById(R.id.content_type);
        engine = (Spinner) findViewById(R.id.search_engine);
        engined = (Spinner) findViewById(R.id.deep_engine);
        mode = (Spinner) findViewById(R.id.mode);
        mode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            public void onItemSelected(AdapterView<?> arg0, View v, int position, long id)
            {
                configureWebSpinner(type.getSelectedItemPosition());
                if(position==0){
                    specific.setVisibility(View.GONE);
                    deep.setVisibility(View.GONE);
                    normal.setVisibility(View.VISIBLE);
                }
                else if(position==1){
                    normal.setVisibility(View.GONE);
                    deep.setVisibility(View.VISIBLE);
                    specific.setVisibility(View.GONE);
                }
                else if(position==2){
                    normal.setVisibility(View.GONE);
                    specific.setVisibility(View.VISIBLE);
                    deep.setVisibility(View.GONE);
                }
            }
            public void onNothingSelected(AdapterView<?> arg0)
            {

            }
        });

        userquery = (EditText) findViewById(R.id.Query);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                if(internet()){
                    String query = null;
                    try {
                        query = userquery.getText().toString();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    switch (mode.getSelectedItemPosition()) {
                        case 0:
                            if (query == null || query.replace(" ", "").equals("")) {
                                userquery.setError("This Field Needs To Be filled");
                                return;
                            } else {
                                normalHandle(query);
                                return;
                            }
                        case 1:
                            deepHandle();
                            return;
                        case 2:
                            if (query == null || query.replace(" ", "").equals("")) {
                                return;
                            } else {
                                specificHandle(query);
                                return;
                            }
                        default:
                            return;
                    }
                }
                else
                    Snackbar.make(view, "Network Connection required", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
            }
        });

        /*MobileAds.initialize(this, "ca-app-pub-1364271166547745/3208891580");

        mAdView = (AdView) findViewById(R.id.definesearch);
        AdRequest adRequest = new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build();
        mAdView.loadAd(adRequest);*/

    }

    private void configureWebSpinner(int position){
        if(position==0){
            website.setAdapter(new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.Music)));
        }
        else if(position==1){
            website.setAdapter(new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.Videos)));
        }
        else if(position==2){
            website.setAdapter(new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.Images)));
        }
        else if(position==3){
            website.setAdapter(new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.eBooksandPDFs)));
        }
        else if(position==4){
            website.setAdapter(new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.other)));
        }
    }

    public void normalHandle(final String data) {
        new AlertDialog.Builder(this).setTitle((CharSequence) "Pick One").setItems((int) R.array.normal, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                String url = "";
                int content_type = type.getSelectedItemPosition();
                if (which == 0) {
                    switch (engine.getSelectedItemPosition()) {
                        case 0:
                            if (content_type != 1) {
                                if (content_type != 2) {
                                    url = "https://www.google.com/search?q=" + data;
                                    break;
                                } else {
                                    url = "https://www.google.com/search?q=" + data + "&tbm=isch";
                                    break;
                                }
                            }
                            url = "https://www.google.com/search?q=" + data + "&tbm=vid";
                            break;
                        case 1:
                            if (content_type != 1) {
                                if (content_type != 2) {
                                    url = "https://www.yandex.com/search/?text=" + data;
                                    break;
                                } else {
                                    url = "https://yandex.com/images/search?text=" + data;
                                    break;
                                }
                            }
                            url = "https://yandex.com/video/search?text=" + data;
                            break;
                        case 2:
                            if (content_type != 1) {
                                if (content_type != 2) {
                                    url = "https://duckduckgo.com/?q=" + data;
                                    break;
                                } else {
                                    url = "https://duckduckgo.com/?q=" + data + "&ia=images";
                                    break;
                                }
                            }
                            url = "https://duckduckgo.com/?q=" + data + "&ia=videos";
                            break;
                        case 3:
                            if (content_type != 1) {
                                if (content_type != 2) {
                                    url = "https://www.bing.com/search?q=" + data;
                                    break;
                                } else {
                                    url = "https://www.bing.com/images/search?q=" + data;
                                    break;
                                }
                            }
                            url = "https://www.bing.com/videos/search?q=" + data;
                            break;
                        case 4:
                            if (content_type != 1) {
                                if (content_type != 2) {
                                    url = "https://search.yahoo.com/search;?p=" + data;
                                    break;
                                } else {
                                    url = "https://images.search.yahoo.com/search/images;?p=" + data;
                                    break;
                                }
                            }
                            url = "https://video.search.yahoo.com/search/video;?p=" + data;
                            break;
                        case 5:
                            url = "http://www.baidu.com/s?wd=" + data;
                            break;
                        case 6:
                            url = "http://www.ask.com/web?q=" + data;
                            break;
                        case 7:
                            url = "https://search.aol.com/aol/search?q=" + data;
                            break;
                        case 8:
                            url = "https://torrents.me/search/" + data;
                            break;
                        default:
                            url = "https://www.google.com/search?q=" + data;
                            break;
                    }
                } else if (which == 1) {
                    String dataPost;
                    switch (content_type) {
                        case 0:
                            dataPost = " -html -htm -php -shtml -opendivx -md5 -md5sums -mp3free4 -xxx intitle:Index.of  mp3";
                            break;
                        case 1:
                            dataPost = " -inurl:(htm|html|php|pls|txt)intitle:index.of “last modified” (mp4|wma|aac|avi)";
                            break;
                        case 2:
                            dataPost = " -html -htm -php -shtml -opendivx -md5 -md5sums -xxx intitle:index.of  (gif|jpeg|jpg|png|bmp|tif|tiff)";
                            break;
                        case 3:
                            dataPost = " -html -htm -php -shtml -opendivx -md5 -md5sums -xxx intitle:index.of  (/ebook|/ebooks|/book|/books)  (chm|pdf)";
                            break;
                        default:
                            dataPost = "";
                            break;
                    }
                    switch (engine.getSelectedItemPosition()) {
                        case 0:
                            url = "https://www.google.com/search?q=" + data;
                            break;
                        case 1:
                            url = "https://www.yandex.com/search/?text=" + data;
                            break;
                        case 2:
                            url = "https://duckduckgo.com/?q=" + data;
                            break;
                        case 3:
                            url = "https://www.bing.com/search?q=" + data;
                            break;
                        case 4:
                            url = "https://search.yahoo.com/search;?p=" + data;
                            break;
                        case 5:
                            url = "http://www.baidu.com/s?wd=" + data;
                            break;
                        case 6:
                            url = "http://www.ask.com/web?q=" + data;
                            break;
                        case 7:
                            url = "https://search.aol.com/aol/search?q=" + data;
                            break;
                        case 8:
                            url = "https://torrents.me/search/" + data;
                            break;
                        default:
                            url = "https://www.google.com/search?q=" + data;
                            break;
                    }
                    url = url + dataPost;
                }
                Intent intent = new Intent(Search.this, Webview.class);
                intent.putExtra("webData", url);
                Search.this.startActivity(intent);
            }
        }).show();
    }

    public void deepHandle() {
        String url;
        switch (engined.getSelectedItemPosition()) {
            case 0:
                url = "http://onion.link";
                break;
            case 1:
                url = "http://vlib.org";
                break;
            case 2:
                url = "http://www.stumpedia.com";
                break;
            case 3:
                url = "http://techdeepweb.com";
                break;
            case 4:
                url = "http://lookahead.surfwax.com";
                break;
            case 5:
                url = "https://www.ixquick.com";
                break;
            case 6:
                url = "http://deeperweb.com";
                break;
            case 7:
                url = "http://www.dogpile.com";
                break;
            default:
                url = "http://onion.link";
                break;
        }
        Intent intent = new Intent(this, Webview.class);
        intent.putExtra("webData", url);
        startActivity(intent);
    }

    public void specificHandle(String data) {
        String url;
        switch (type.getSelectedItemPosition()) {
            case 0:
                switch (website.getSelectedItemPosition()) {
                    case 0:
                        url = "https://play.spotify.com/search/" + data;
                        break;
                    case 1:
                        url = "https://soundcloud.com/search?q=" + data;
                        break;
                    case 2:
                        url = "https://www.last.fm/search?q=" + data;
                        break;
                    case 3:
                        url = "https://www.saavn.com/search/" + data;
                        break;
                    case 4:
                        url = "http://gaana.com/search/" + data;
                        break;
                    case 5:
                        url = "http://tunein.com/search/?query=" + data;
                        break;
                    case 6:
                        url = "http://us.napster.com/search?query=" + data;
                        break;
                    case 7:
                        url = "https://bandcamp.com/search?q=" + data;
                        break;
                    default:
                        url = "https://soundcloud.com/search?q=" + data;
                        break;
                }
                break;
            case 1:
                switch (website.getSelectedItemPosition()) {
                    case 0:
                        url = "https://www.youtube.com/results?search_query=" + data;
                        break;
                    case 1:
                        url = "http://www.dailymotion.com/search/" + data;
                        break;
                    case 2:
                        url = "https://vimeo.com/search?q=" + data;
                        break;
                    case 3:
                        url = "https://www.flickr.com/search/?media=videos&text=" + data;
                        break;
                    case 4:
                        url = "http://www.veoh.com/find/?query=" + data;
                        break;
                    case 5:
                        url = "http://www.metacafe.com/videos_about/" + data;
                        break;
                    default:
                        url = "https://www.youtube.com/results?search_query=" + data;
                        break;
                }
                break;
            case 2:
                switch (website.getSelectedItemPosition()) {
                    case 0:
                        url = "https://www.shutterstock.com/search?searchterm=" + data + "&image_type=all";
                        break;
                    case 1:
                        url = "https://negativespace.co/?s=" + data + "&submit=Search";
                        break;
                    case 2:
                        url = "http://deathtothestockphoto.com/?s=" + data;
                        break;
                    case 3:
                        url = "https://picjumbo.com/?s=" + data;
                        break;
                    case 4:
                        url = "http://stokpic.com/?s=" + data;
                        break;
                    case 5:
                        url = "http://kaboompics.com/gallery?search=" + data;
                        break;
                    case 6:
                        url = "http://photobucket.com/images/" + data;
                        break;
                    case 7:
                        url = "https://freerangestock.com/search.php?type=all&search=" + data;
                        break;
                    case 8:
                        url = "https://libreshot.com/?s=" + data;
                        break;
                    case 9:
                        url = "http://fancycrave.com/?s=" + data + "&submit=Search";
                        break;
                    case 10:
                        url = "https://unsplash.com/search/" + data;
                        break;
                    default:
                        url = "https://www.shutterstock.com/search?searchterm=" + data + "&image_type=all";
                        break;
                }
                break;
            case 3:
                switch (website.getSelectedItemPosition()) {
                    case 0:
                        url = "https://www.scribd.com/search?query=" + data;
                        break;
                    case 1:
                        url = "https://en.wikibooks.org/?search=" + data;
                        break;
                    case 2:
                        url = "http://www.getfreeebooks.com/?s=" + data;
                        break;
                    case 3:
                        url = "http://www.baen.com/catalogsearch/result/?q=" + data;
                        break;
                    case 4:
                        url = "https://www.free-ebooks.net/search/" + data;
                        break;
                    case 5:
                        url = "http://manybooks.net/search.php?search=" + data;
                        break;
                    case 6:
                        url = "http://www.planetpublish.com/?s=" + data;
                        break;
                    case 7:
                        url = "https://www.adobe.com/search.html#q=" + data;
                        break;
                    case 8:
                        url = "http://bookboon.com/en/search?q=" + data;
                        break;
                    default:
                        url = "https://www.scribd.com/search?query=" + data;
                        break;
                }
                break;
            case 4:
                url = "https://www.google.com/search?q=" + data;
                break;
            default:
                url = "https://www.google.com/search?q=" + data;
                break;
        }
        Intent intent = new Intent(this, Webview.class);
        intent.putExtra("webData", url);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);
        return true;
    }

    public boolean internet() {
        boolean wifi = false;
        boolean phone = false;
        for (NetworkInfo networkInfo : ((ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE)).getAllNetworkInfo()) {
            if (networkInfo.getTypeName().equalsIgnoreCase("wifi") && networkInfo.isConnected()) {
                wifi = true;
            }
            if (networkInfo.getTypeName().equalsIgnoreCase("mobile") && networkInfo.isConnected()) {
                phone = true;
            }
        }
        return wifi || phone;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.share_menu_id:
                Intent sendIntent = new Intent();
                sendIntent.setAction("android.intent.action.SEND");
                sendIntent.putExtra("android.intent.extra.TEXT", "Hey check out this new app at: https://play.google.com/store/apps/details?id=" + getPackageName());
                sendIntent.setType("text/plain");
                startActivity(Intent.createChooser(sendIntent, "Share via"));
                return true;
            case R.id.rate_us_menu_id:
                String appPackageName = getPackageName();
                try {
                    startActivity(new Intent("android.intent.action.VIEW", Uri.parse("market://details?id=" + appPackageName)));
                    return true;
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent("android.intent.action.VIEW", Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                    return true;
                }
            case R.id.more_apps_menu_id:
                try {
                    startActivity(new Intent("android.intent.action.VIEW", Uri.parse("market://developer?id=7665922803421240272")));
                    return true;
                } catch (ActivityNotFoundException e2) {
                    startActivity(new Intent("android.intent.action.VIEW", Uri.parse("https://play.google.com/store/apps/dev?id=7665922803421240272")));
                    return true;
                }
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
