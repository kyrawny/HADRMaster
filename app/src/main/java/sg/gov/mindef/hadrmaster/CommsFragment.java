package sg.gov.mindef.hadrmaster;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * Use the {@link CommsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CommsFragment extends Fragment {

    private FirebaseListAdapter<ChatMessage> adapter;

    public CommsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment CommsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CommsFragment newInstance() {
        CommsFragment fragment = new CommsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_comms, container, false);
        getActivity().setTitle("Map");

        FloatingActionButton fabSend = (FloatingActionButton) rootView.findViewById(R.id.fab_send);

        final EditText input = (EditText) rootView.findViewById(R.id.text_input);

        ListView listOfMessages = (ListView) rootView.findViewById(R.id.list_messages);

        Query query = FirebaseDatabase.getInstance().getReference();

        FirebaseListOptions<ChatMessage> options = new FirebaseListOptions.Builder<ChatMessage>()
                .setQuery(query, ChatMessage.class)
                .build();

        FirebaseListAdapter<ChatMessage> firebaseListAdapter = new FirebaseListAdapter<ChatMessage>(options) {
            @Override
            protected void populateView(View v, ChatMessage model, int position) {
                // Get references to the views of message.xml
                TextView messageText = (TextView)v.findViewById(R.id.message_text);
                TextView messageUser = (TextView)v.findViewById(R.id.message_user);
                TextView messageTime = (TextView)v.findViewById(R.id.message_time);

                // Set their text
                messageText.setText(model.getMessageText());
                messageUser.setText(model.getMessageUser());

                // Format the date before showing it
                messageTime.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)",
                        model.getMessageTime()));
            }
        };

        listOfMessages.setAdapter(adapter);

        fabSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase.getInstance()
                        .getReference()
                        .push()
                        .setValue(new ChatMessage(input.getText().toString(),
                                FirebaseAuth.getInstance().
                                    getCurrentUser().
                                        getEmail())
                        );
                input.setText("");
            }
        });
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_comms, container, false);
    }
}