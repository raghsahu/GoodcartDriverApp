package com.logical.driverapp.Adapter;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.logical.driverapp.Activity.OrderDetailsActivity;
import com.logical.driverapp.R;
import com.logical.driverapp.model.Getorderdetail;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Ravindra Birla on 13/11/2019.
 */
public class CustomRecyclerGetorder extends RecyclerView.Adapter<CustomRecyclerGetorder.ViewHolder> implements
        ActivityCompat.OnRequestPermissionsResultCallback{
    private Context context;
    private static final int MAKE_CALL_PERMISSION_REQUEST_CODE = 1;
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS =0 ;
    private List<Getorderdetail> getorderdetails;
    private String mobile_nmbr;

    public CustomRecyclerGetorder(Context context, List getorderdetails) {
        this.context = context;
        this.getorderdetails = getorderdetails;
    }

    @Override
    public CustomRecyclerGetorder.ViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.listfragmentorderitem, parent, false);
       CustomRecyclerGetorder.ViewHolder viewHolder = new CustomRecyclerGetorder.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CustomRecyclerGetorder.ViewHolder holder, final int position) {
        holder.itemView.setTag(getorderdetails.get(position));

       Getorderdetail  pu =getorderdetails.get(position);
       holder.orderid.setText(pu.getOrder_id());
       holder.firstname.setText(pu.getFirst_name());
        holder.lastname.setText(pu.getLast_name());
        holder.price.setText(pu.getTotal_price());
        holder.quantitiy.setText(pu.getQuantity());
        holder.address.setText(pu.getAddress());
        holder.user_id.setText(pu.getUser_id());
        holder.assigndate.setText(pu.getAssign_date());

        Glide.with(context)
                .load("http://logicalsofttech.com/goodcart/assets/uploaded/users/" + getorderdetails.get(position).getImage())
                .override(300, 200)
                .into(holder.userimage);


        holder.location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              //  Intent intent=new Intent(context, LocationActivity.class);
              //  context.startActivity(intent);

                double lati= Double.parseDouble(getorderdetails.get(position).getLat());
                double longi= Double.parseDouble(getorderdetails.get(position).getLng());

             //   redirect to google map*****with lat lng
               // String uri = String.format(Locale.ENGLISH, "http://maps.google.com/maps?daddr=%f,%f (%s)", 12f, 2f, "Bhopal");
                String uri = "http://maps.google.com/maps?daddr=" + lati + "," + longi;
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                intent.setPackage("com.google.android.apps.maps");
                try
                {
                    context.startActivity(intent);
                }
                catch(ActivityNotFoundException ex)
                {
                    try
                    {
                        Intent unrestrictedIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                        context.startActivity(unrestrictedIntent);
                    }
                    catch(ActivityNotFoundException innerEx)
                    {
                        Toast.makeText(context, "Please install a maps application", Toast.LENGTH_LONG).show();
                    }
                }

//*************************************************
//                String url = "http://maps.google.com/maps?daddr=" + 23.2599 + "," + 77.4126;
//                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
//                context.startActivity(intent);

            }
        });


        holder.orderdetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, OrderDetailsActivity.class);
                intent.putExtra("order_id",getorderdetails.get(position).getOrder_id());
                intent.putExtra("tv_address",getorderdetails.get(position).getAddress());
                context.startActivity(intent);
            }
        });
        holder.call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mobile_nmbr=   getorderdetails.get(position).getMobile();

                if (!getorderdetails.get(position).getMobile().isEmpty()) {
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:"+getorderdetails.get(position).getMobile()));

                    if (ActivityCompat.checkSelfPermission(context,
                            Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        checkRunTimePermission(getorderdetails.get(position).getMobile());

                        return;
                    }
                    context.startActivity(callIntent);
                } else {
                    Toast.makeText(context, "Phone number not found", Toast.LENGTH_SHORT).show();
                }
            }
        });

//        if (checkPermission(Manifest.permission.CALL_PHONE)) {
//            holder.call.setEnabled(true);
//        } else {
//           holder.call.setEnabled(false);
//            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.CALL_PHONE}, MAKE_CALL_PERMISSION_REQUEST_CODE);
//        }
    }

    private void checkRunTimePermission(String mobile) {


        if (ContextCompat.checkSelfPermission(context,Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale((Activity)context,
                    Manifest.permission.CALL_PHONE)) {

                ActivityCompat.requestPermissions((Activity)context,
                        new String[]{Manifest.permission.CALL_PHONE},
                        MY_PERMISSIONS_REQUEST_SEND_SMS);
               // Log.e("perm_if", "perm_if");
            } else {
                ActivityCompat.requestPermissions((Activity)context,
                        new String[]{Manifest.permission.CALL_PHONE},
                        MY_PERMISSIONS_REQUEST_SEND_SMS);
               // Log.e("perm_else", "perm_else");
            }
        }else {
           // Log.e("perm_else_try", "perm_else");
            try{
                redirectSms(mobile);
            }catch(Exception e){
               // Log.e("send_sms_error", e.toString());
            }
        }


    }

    private void redirectSms(String mobile) {

        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:"+mobile));

        if (ActivityCompat.checkSelfPermission(context,
                Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            checkRunTimePermission(mobile);

            return;
        }
        context.startActivity(callIntent);

    }


//    private boolean checkPermission(String permission) {
//        return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
//    }




    @Override
    public int getItemCount() {
        return getorderdetails.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout location,call;
       CircleImageView userimage;
        TextView orderid,firstname,lastname,address,price,quantitiy,user_id,assigndate,orderdetails;
        public ViewHolder( View itemView) {
            super(itemView);
            orderid=itemView.findViewById(R.id.orderid);
            firstname=itemView.findViewById(R.id.firstname);
            lastname=itemView.findViewById(R.id.lastname);
            address=itemView.findViewById(R.id.address);
            price=itemView.findViewById(R.id.price);
            quantitiy=itemView.findViewById(R.id.quantitiy);
            user_id=itemView.findViewById(R.id.user_id);
            assigndate=itemView.findViewById(R.id.assigndate);
            userimage=itemView.findViewById(R.id.userimage);
            location=itemView.findViewById(R.id.location);
            orderdetails=itemView.findViewById(R.id.orderdetails);
            call=itemView.findViewById(R.id.call);
        }
    }



    @Override
    public void onRequestPermissionsResult ( int requestCode, String permissions[],
                                             int[] grantResults)
    {
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_SEND_SMS: {
                boolean openDialogOnce = true;
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    redirectSms(mobile_nmbr);
                    //SmsManager smsManager = SmsManager.getDefault();
                    //smsManager.sendTextMessage("7879014", null, "lamba quiz", null, null);
                    // Toast.makeText(context, "SMS sent.",Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(context,"Call faild, please try again.", Toast.LENGTH_LONG).show();

                }
                return;
//***************************************************************************
//                for (int i = 0; i < grantResults.length; i++) {
//                    String permission = permissions[i];
//
//                    isPermitted = grantResults[i] == PackageManager.PERMISSION_GRANTED;
//
//                    if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
//                        // user rejected the permission
//                        boolean showRationale = ActivityCompat.shouldShowRequestPermissionRationale((Activity)context, permission);
//                        if (!showRationale) {
//                            //execute when 'never Ask Again' tick and permission dialog not show
//                        } else {
//
//                            if (openDialogOnce) {
//                                // Toast.makeText(getActivity(), "Contact permission required", Toast.LENGTH_SHORT).show();
//                                // alertView();
//                            }
//                        }
//                    }
//                }
//                if (isPermitted){
//                    redirectSms("123456");
//                }else {
//                    Toast.makeText(context, "SMS faild, please try again.", Toast.LENGTH_SHORT).show();
//                }

            }
        }

    }
    
    
    }

