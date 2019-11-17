package com.lee.kr.STUnitasAOS;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TableListController extends AppCompatActivity implements SearchView.OnQueryTextListener, View.OnClickListener, AbsListView.OnScrollListener {

    private ListView list;
    private GridView grid;
    private APIInterface apiInterface;
    private ApiHandler handler;
    private ApiHandler handler2;
    private SearchView searchView;
    private Adapter adapter;
    private AdapterGrid adapterGrid;
    private int page = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tablelist_layout);

        findViewById(R.id.select).setOnClickListener(this);

        list = findViewById(R.id.list);

        list.setOnScrollListener(this);

        adapter = new Adapter(this);

        adapterGrid = new AdapterGrid(this);

        list.setAdapter(adapter);

        grid = findViewById(R.id.grid);

        grid.setOnScrollListener(this);

        grid.setAdapter(adapterGrid);

        searchView = findViewById(R.id.search);

        searchView.setOnQueryTextListener(this);

        apiInterface = APIClient.getClient().create(APIInterface.class);

        handler = new ApiHandler(new ApiHandler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message message) {

                Call<SearchList> call2 = apiInterface.getSearchList((String) message.obj, Integer.toString(page));
                call2.enqueue(new Callback<SearchList>() {
                    @Override
                    public void onResponse(Call<SearchList> call, Response<SearchList> response) {

                        switch (handler.handlerType){
                            case DOUBLE:{

                                adapter.setSearchList(response.body());
                                adapterGrid.setSearchList(response.body());

                            }break;
                            case LIST:{

                                adapter.setSearchList(response.body());

                            }break;
                            case COLLECTION:{

                                adapterGrid.setSearchList(response.body());

                            }break;
                        }
                    }

                    @Override
                    public void onFailure(Call<SearchList> call, Throwable t) {
                        Log.i("TAG", ">>>>>>>>>>>>>> : "+t.getMessage());
                        call.cancel();
                    }

                });

                return false;
            }
        });

        handler2 = new ApiHandler(new ApiHandler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message message) {

                Call<SearchList> call2 = apiInterface.getSearchList((String) message.obj, Integer.toString(page));
                call2.enqueue(new Callback<SearchList>() {
                    @Override
                    public void onResponse(Call<SearchList> call, Response<SearchList> response) {

                        switch (handler.handlerType){
                            case DOUBLE:{

                                adapter.setSearchList(response.body());
                                adapterGrid.setSearchList(response.body());

                            }break;
                            case LIST:{

                                adapter.setSearchList2(response.body());

                            }break;
                            case COLLECTION:{

                                adapterGrid.setSearchList2(response.body());

                            }break;
                        }
                    }

                    @Override
                    public void onFailure(Call<SearchList> call, Throwable t) {
                        Log.i("TAG", ">>>>>>>>>>>>>> : "+t.getMessage());
                        call.cancel();
                    }

                });

                return false;
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
//        handler.sendEmptyMessage(0);
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        Log.i("TAG", ">>>>>>>>>>>>>> "+s);
        if (!s.isEmpty()){
            Message msg = Message.obtain();
            msg.obj = new String(s);
            handler.sendMessage(msg);
        }else{
            adapter.cleanAll();
        }
        return false;
    }

    @Override
    public void onClick(View view) {
        String tag = (String)view.getTag();
        if (tag.equals("1")){
            view.setTag("0");
            view.setBackgroundResource(R.drawable.list_icon);
            this.list.setVisibility(View.VISIBLE);
            this.grid.setVisibility(View.GONE);
            handler.handlerType = HandlerType.LIST;
            page = 1;
            Message msg = Message.obtain();
            msg.obj = new String(searchView.getQuery().toString());
            handler.sendMessage(msg);
        }else{
            view.setTag("1");
            view.setBackgroundResource(R.drawable.collection_icon);
            this.list.setVisibility(View.GONE);
            this.grid.setVisibility(View.VISIBLE);
            handler.handlerType = HandlerType.COLLECTION;
            page = 1;
            Message msg = Message.obtain();
            msg.obj = new String(searchView.getQuery().toString());
            handler.sendMessage(msg);
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView listView, int scrollState) {

        InputMethodManager imm = (InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = getCurrentFocus();
        if (view == null) {
            view = new View(this);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

        if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && (listView.getLastVisiblePosition()) >= (adapter.getCount() - 1)) {
            Log.i("TAG", ">>>>>>>>>>>>>>>>>>> botom");
            page++;
            Message msg = Message.obtain();
            msg.obj = new String(searchView.getQuery().toString());
            handler2.sendMessage(msg);

        }

    }

    @Override
    public void onScroll(AbsListView absListView, int i, int i1, int i2) {

    }

    public class Adapter extends BaseAdapter{

        private Context context;

        private SearchList searchList;

        public Adapter(Context context) {
            this.context = context;
            searchList = new SearchList();
        }

        @Override
        public int getCount() {
            return searchList.documents.size();
        }

        @Override
        public Object getItem(int i) {
            return searchList.documents.get(i);
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {

            if (view == null){
                view = LayoutInflater.from(context).inflate(R.layout.tablelist_item, null);
            }

            ImageView thumbnail_url = view.findViewById(R.id.thumbnail_url);
            TextView collection = view.findViewById(R.id.collection);
            collection.setText("구분 : "+searchList.documents.get(i).collection);
            TextView display_sitename = view.findViewById(R.id.display_sitename);
            display_sitename.setText("사이트이름 : "+searchList.documents.get(i).display_sitename);
            TextView datetime = view.findViewById(R.id.datetime);
            datetime.setText("생성일 : "+searchList.documents.get(i).datetime);

            Glide
                .with(context)
                .load(searchList.documents.get(i).thumbnail_url)
                .centerCrop()
                .into(thumbnail_url);

            return view;
        }

        public void setSearchList(SearchList searchList) {
            this.searchList.documents.clear();
            this.searchList.documents.addAll(searchList.documents);
            this.notifyDataSetChanged();
        }

        public void setSearchList2(SearchList searchList) {
            this.searchList.documents.addAll(searchList.documents);
            this.notifyDataSetChanged();
        }

        public void cleanAll() {
            this.searchList.documents.clear();
            this.notifyDataSetChanged();
        }
    }


    public class AdapterGrid extends BaseAdapter{

        private Context context;

        private SearchList searchList;

        public AdapterGrid(Context context) {
            this.context = context;
            searchList = new SearchList();
        }

        @Override
        public int getCount() {
            return searchList.documents.size();
        }

        @Override
        public Object getItem(int i) {
            return searchList.documents.get(i);
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {

            if (view == null){
                view = LayoutInflater.from(context).inflate(R.layout.collection_item, null);
            }

            ImageView thumbnail_url = view.findViewById(R.id.thumbnail_url);
            TextView collection = view.findViewById(R.id.collection);
            collection.setText("구분 : "+searchList.documents.get(i).collection);
            TextView display_sitename = view.findViewById(R.id.display_sitename);
            display_sitename.setText("사이트이름 : "+searchList.documents.get(i).display_sitename);
            TextView datetime = view.findViewById(R.id.datetime);
            datetime.setText("생성일 : "+searchList.documents.get(i).datetime);

            Glide
                    .with(context)
                    .load(searchList.documents.get(i).thumbnail_url)
                    .centerCrop()
                    .into(thumbnail_url);

            return view;
        }

        public void setSearchList(SearchList searchList) {
            this.searchList.documents.clear();
            this.searchList.documents.addAll(searchList.documents);
            this.notifyDataSetChanged();
        }

        public void setSearchList2(SearchList searchList) {
            this.searchList.documents.addAll(searchList.documents);
            this.notifyDataSetChanged();
        }

        public void cleanAll() {
            this.searchList.documents.clear();
            this.notifyDataSetChanged();
        }
    }

    //        HTTP/1.1 200 OK
//        Content-Type: application/json;charset=UTF-8
//        {
//          "meta": {
//            "total_count": 422583,
//            "pageable_count": 3854,
//            "is_end": false
//          },
//          "documents": [
//            {
//              "collection": "news",
//              "thumbnail_url": "https://search2.kakaocdn.net/argon/130x130_85_c/36hQpoTrVZp",
//              "image_url": "http://t1.daumcdn.net/news/201706/21/kedtv/20170621155930292vyyx.jpg",
//              "width": 540,
//              "height": 457,
//              "display_sitename": "한국경제TV",
//              "doc_url": "http://v.media.daum.net/v/20170621155930002",
//              "datetime": "2017-06-21T15:59:30.000+09:00"
//            },
//            ...
//          ]
//        }


}