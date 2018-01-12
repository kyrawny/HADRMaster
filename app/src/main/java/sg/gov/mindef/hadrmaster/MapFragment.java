package sg.gov.mindef.hadrmaster;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.style.layers.Layer;
import com.mapbox.mapboxsdk.style.layers.PropertyFactory;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * Use the {@link MapFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MapFragment extends Fragment {

    private MapView mapView;

    private boolean isAfterDis = true;

    public MapFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     *
     * @return A new instance of fragment MapFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MapFragment newInstance() {
        MapFragment fragment = new MapFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        final View rootView = inflater.inflate(R.layout.fragment_map, container, false);
        getActivity().setTitle("Map");

        Mapbox.getInstance(getActivity(), getString(R.string.access_token));

        mapView = (MapView) rootView.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);

        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(final MapboxMap mapboxMap) {

                final Layer afterLayer = mapboxMap.getLayer("disaster-after");

                rootView.findViewById(R.id.fab_beforeafter).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (isAfterDis) {
                            Toast.makeText(view.getContext(), "Changed to before disaster", Toast.LENGTH_LONG).show();
                            afterLayer.setProperties(PropertyFactory.rasterOpacity(Float.valueOf(0)));
                            isAfterDis = false;
                        } else {
                            Toast.makeText(view.getContext(), "Changed to after disaster", Toast.LENGTH_LONG).show();
                            afterLayer.setProperties(PropertyFactory.rasterOpacity(Float.valueOf(1)));
                            isAfterDis = true;
                        }
                    }
                });

            }
        });
        return rootView;
    }
}
