package org.itcube.notetoself;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

import org.w3c.dom.Text;

public class DialogShowNote extends DialogFragment {

    private Note note;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_show_note, null);

        TextView txtTitle = (TextView) dialogView.findViewById(R.id.txtTitle);
        TextView txtDescription = (TextView) dialogView.findViewById(R.id.txtDescription);
        TextView textImportant = (TextView) dialogView.findViewById(R.id.textViewImportant);
        TextView textTodo = (TextView) dialogView.findViewById(R.id.textViewTodo);
        TextView textIdea = (TextView) dialogView.findViewById(R.id.textViewIdea);
        Button btnOk = (Button) dialogView.findViewById(R.id.btnOk);

        txtTitle.setText(note.getTitle());
        txtDescription.setText(note.getDescription());

        if (!note.isImportant()) {
            textImportant.setVisibility(View.GONE);
        }

        if (!note.isTodo()) {
            textTodo.setVisibility(View.GONE);
        }

        if (!note.isIdea()) {
            textIdea.setVisibility(View.GONE);
        }

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        builder.setView(dialogView).setMessage("Your Note");
        return builder.create();

    }

    public void sendNoteSelected(Note noteSelected) {
        note = noteSelected;
    }
}
