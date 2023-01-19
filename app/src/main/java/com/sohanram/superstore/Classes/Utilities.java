package com.sohanram.superstore.Classes;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.sohanram.superstore.R;

public class Utilities {
    public void showDialog(Activity activity, String msg) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.custom_quantity_dialog);

        EditText edtQuantity = dialog.findViewById(R.id.edt_quantity);
        edtQuantity.setText(msg);

        Button btnSubmit = dialog.findViewById(R.id.btn_qty_submit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String quantity = edtQuantity.getText().toString();
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}
