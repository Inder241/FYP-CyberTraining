package com.example.fyp;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class CategoryAdapter extends BaseAdapter {
    private List<CategoryModel> cat_List;

    public CategoryAdapter(List<CategoryModel> cat_List) {
        this.cat_List = cat_List;
    }

    @Override
    public int getCount() {
        return cat_List.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        View myView;
        if (view == null){
            myView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cat_item_layout,viewGroup,false);

        } else{
            myView = view;
        }

        myView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(),TestActivity.class);
                intent.putExtra("CAT_INDEX",i);
                view.getContext().startActivity(intent);
            }
        });

        TextView catName = myView.findViewById(R.id.CatName);
        TextView noOfTest = myView.findViewById(R.id.no_of_test);

        catName.setText(cat_List.get(i).getName());
        noOfTest.setText(String.valueOf(cat_List.get(i).getNoOfTests()) + "Tests");
        return myView;
    }
}
