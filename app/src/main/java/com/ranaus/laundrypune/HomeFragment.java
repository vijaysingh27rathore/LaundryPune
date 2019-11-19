package com.ranaus.laundrypune;


import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

import static com.ranaus.laundrypune.R.id.item;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {
    HomeFragment home;

    ListView listView;
    String item[] = {"Coat","Tie","Shirt","Pant","T-Shirt","Saree","Saree(Roll)","Saree/Blows First wash","Blouse","Ladies Item/Anarkali per Piece","Bedsheet Single/Double","Curtain/Plates Single/Double",
            "Blanket Single/Double"};
    String price1[] = {"70","5","7","7","7","35","60","0","7","7/20","25/40","40/60 60/80","0"};
    String price2[] = {"250","30","50","50","50","180","180","220/50","50","50/70","80/120","100/180","300/450"};

    String abc[];
    String abc1[];
    String abc2[];

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        listView = (ListView) view.findViewById(R.id.list_view);

        MyAdapter adapter = new MyAdapter(getActivity().getApplicationContext(),item,price1,price2);
        listView.setAdapter(adapter);
        //XYZ();
        return view;
    }

    class MyAdapter extends ArrayAdapter<String>
    {
        HomeFragment homeFragment;
        Context context;
        String classItem[];
        String classPrice1[];
        String classPrice2[];

        public  MyAdapter(Context context,String item[],String price1[], String price2[])
        {
            super(context,R.layout.main_item_list, R.id.item,item);
            this.context = context;
            this.classItem = item;
            this.classPrice1 = price1;
            this.classPrice2 = price2;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = layoutInflater.inflate(R.layout.main_item_list,parent,false);
            TextView textView = row.findViewById(R.id.item);
            TextView textView1 = row.findViewById(R.id.price1);
            TextView textView2 = row.findViewById(R.id.price2);

            textView.setText(classItem[position]);
            textView1.setText(classPrice1[position]);
            textView2.setText(classPrice2[position]);
            return row;
        }
    }
    private void XYZ()
    {
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("AppUserRequest");
        query.whereEqualTo("status","active");

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null && objects.size() > 0)
                {
                    for (ParseObject status : objects)
                    {
                        int x = 0;
                        String objId;
                        objId = status.getObjectId();
                        abc[x] = String.valueOf(status.get("status"+""));
                        //abc1[x] = String.valueOf(status.get(""))
                        x++;
                    }
                }
            }
        });
    }
}
