package com.lee.kr.STUnitasAOS;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class TableListController extends AppCompatActivity implements SearchView.OnQueryTextListener {

    private ListView list;
    private GridView grid;
    private APIInterface apiInterface;
    private Handler handler;
    private SearchView searchView;
    private Adapter adapter;
    private int page = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tablelist_layout);

        list = findViewById(R.id.list);

        adapter = new Adapter(this);

        list.setAdapter(adapter);

        grid = findViewById(R.id.grid);

        searchView = findViewById(R.id.search);

        searchView.setOnQueryTextListener(this);

        apiInterface = APIClient.getClient().create(APIInterface.class);
        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message message) {

                Call<SearchList> call2 = apiInterface.getSearchList((String) message.obj);
                call2.enqueue(new Callback<SearchList>() {
                    @Override
                    public void onResponse(Call<SearchList> call, Response<SearchList> response) {
                        adapter.setSearchList(response.body());
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
            collection.setText(searchList.documents.get(i).collection);
            TextView display_sitename = view.findViewById(R.id.display_sitename);
            display_sitename.setText(searchList.documents.get(i).display_sitename);
            TextView datetime = view.findViewById(R.id.datetime);
            datetime.setText(searchList.documents.get(i).datetime);

            Glide
                .with(context)
                .load(searchList.documents.get(i).thumbnail_url)
                .centerCrop()
                .into(thumbnail_url);

            return view;
        }

        public void setSearchList(SearchList searchList) {
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