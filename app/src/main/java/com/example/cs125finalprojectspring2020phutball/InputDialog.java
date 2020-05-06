package com.example.cs125finalprojectspring2020phutball;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatDialogFragment;

public class InputDialog extends AppCompatDialogFragment {
    private EditText editTextXCoordinate;
    private EditText editTextYCoordinate;
    private InputDialogListener listener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.input_dialog, null);
        editTextXCoordinate = view.findViewById(R.id.xEditText);
        editTextYCoordinate = view.findViewById(R.id.yEditText);
        builder.setView(view)
                .setTitle("Enter Coordinates")
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int x = Integer.parseInt(editTextXCoordinate.getText().toString());
                        int y = Integer.parseInt(editTextYCoordinate.getText().toString());
                        listener.applyCoords(x,y);
                    }
                });
        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener = (InputDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "must implement InputDialogListener");
        }
    }

    public interface InputDialogListener{
        void applyCoords(int x, int y);
    }
}
