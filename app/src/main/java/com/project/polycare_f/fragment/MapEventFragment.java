package com.project.polycare_f.fragment;

import android.app.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.project.polycare_f.R;
import com.project.polycare_f.data.Event;

public class MapEventFragment extends Fragment {
    private Event event;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.event_map_infos, container, false);
        TextView titre = view.findViewById(R.id.titre_incident);
        TextView categorie = view.findViewById(R.id.incident_description);
        TextView reporter = view.findViewById(R.id.reporter_name);
        TextView assigne = view.findViewById(R.id.asignee_name);
        return view;
    }

}

