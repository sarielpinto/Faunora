package com.example.faunora.datos;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.faunora.R;

import java.io.InputStream;

public class CustomListView extends ArrayAdapter<String> {

    private String[] profilename;
    private String[] email;

    private Activity context;
    Bitmap bitmap;
    ImageButton imageButton;

    public CustomListView(Activity context,String[] profilename,String[] email) {
        super(context, R.layout.layout_paradatos,profilename);
        this.context=context;
        this.profilename=profilename;
        this.email=email;
        imageButton=(ImageButton)context.findViewById(R.id.sonido);

    }

    @NonNull
    @Override

    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
        View r=convertView;
        ViewHolder viewHolder=null;
        if(r==null){
            LayoutInflater layoutInflater=context.getLayoutInflater();
            r=layoutInflater.inflate(R.layout.layout_paradatos,null,true);
            viewHolder=new ViewHolder(r);
            r.setTag(viewHolder);
        }
        else {
            viewHolder=(ViewHolder)r.getTag();

        }

        viewHolder.tvw1.setText(profilename[position]);
        viewHolder.tvw2.setText(email[position]);


        return r;
    }

    class ViewHolder{

        TextView tvw1;
        TextView tvw2;
        ImageView ivw;

        ViewHolder(View v){
            tvw1=(TextView)v.findViewById(R.id.tvprofilename);
            tvw2=(TextView)v.findViewById(R.id.tvemail);

        }

    }

    public class GetImageFromURL extends AsyncTask<String,Void,Bitmap>
    {

        ImageView imgView;
        public GetImageFromURL(ImageView imgv)
        {
            this.imgView=imgv;
        }
        @Override
        protected Bitmap doInBackground(String... url) {
            String urldisplay=url[0];
            bitmap=null;

            try{

                InputStream ist=new java.net.URL(urldisplay).openStream();
                bitmap= BitmapFactory.decodeStream(ist);
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }

            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap){

            super.onPostExecute(bitmap);
            imgView.setImageBitmap(bitmap);
        }
    }



}

