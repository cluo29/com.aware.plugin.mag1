package com.aware.plugin.mag1;

/**
 * Created by CLU on 4/18/2015.
 */
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.aware.Aware;
import com.aware.plugin.mag1.Provider.Mag1_Data;
import com.aware.utils.IContextCard;


public class ContextCard implements IContextCard {
    private static TextView Text_m_0;
    private static TextView Text_m_1;
    private static TextView Text_m_2;
    private static TextView Text_r_0;
    private static TextView Text_r_1;
    private static TextView Text_r_2;

    public ContextCard() {};

    public View getContextCard(Context context) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View card = inflater.inflate(R.layout.card, null);
        Text_m_0 = (TextView) card.findViewById(R.id.m_0);
        Text_m_1 = (TextView) card.findViewById(R.id.m_1);
        Text_m_2 = (TextView) card.findViewById(R.id.m_2);
        Text_r_0 = (TextView) card.findViewById(R.id.r_0);
        Text_r_1 = (TextView) card.findViewById(R.id.r_1);
        Text_r_2 = (TextView) card.findViewById(R.id.r_2);

            Cursor latest = context.getContentResolver().query(Mag1_Data.CONTENT_URI, null, null, null, Mag1_Data.TIMESTAMP + " DESC LIMIT 1");
            if (latest != null && latest.moveToFirst()) {
                Text_m_0.setText(String.format("%.1f", latest.getDouble(latest.getColumnIndex(Mag1_Data.M_VALUES_0))) + " μT");
                Text_m_1.setText(String.format("%.1f", latest.getDouble(latest.getColumnIndex(Mag1_Data.M_VALUES_1))) + " μT");
                Text_m_2.setText(String.format("%.1f", latest.getDouble(latest.getColumnIndex(Mag1_Data.M_VALUES_2))) + " μT");
                Text_r_0.setText(String.format("%.1f", latest.getDouble(latest.getColumnIndex(Mag1_Data.R_VALUES_0))) + " x*sin(θ/2)");
                Text_r_1.setText(String.format("%.1f", latest.getDouble(latest.getColumnIndex(Mag1_Data.R_VALUES_1))) + " y*sin(θ/2)");
                Text_r_2.setText(String.format("%.1f", latest.getDouble(latest.getColumnIndex(Mag1_Data.R_VALUES_2))) + " z*sin(θ/2)");
            } else {
                Text_m_0.setText("no data");
            }
            if (latest != null && !latest.isClosed()) latest.close();


        return card;
    }
}