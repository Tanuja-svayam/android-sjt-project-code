package com.shrikantelectronics;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

public class StockSummaryAgeing_Category_ListViewTab extends TabActivity {
	// TabSpec Names
	private static final String INBOX_SPEC = "Quantity";
	private static final String OUTBOX_SPEC = "Value";

    String companycd;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        Intent i = getIntent();
        companycd = i.getStringExtra("companycd");

        TabHost tabHost = getTabHost();

        // Inbox Tab
        TabSpec inboxSpec = tabHost.newTabSpec(INBOX_SPEC);
        // Tab Icon
        inboxSpec.setIndicator(INBOX_SPEC, getResources().getDrawable(R.drawable.icon_inbox));
        Intent inboxIntent = new Intent(this, StockAgeing_Category.class);
        // Tab Content
        inboxIntent.putExtra("companycd",companycd);
        inboxIntent.putExtra("type","Q");

        inboxSpec.setContent(inboxIntent);

        // Outbox Tab
        TabSpec outboxSpec = tabHost.newTabSpec(OUTBOX_SPEC);
        outboxSpec.setIndicator(OUTBOX_SPEC, getResources().getDrawable(R.drawable.icon_outbox));
        Intent outboxIntent = new Intent(this, StockAgeing_Category.class);
        outboxIntent.putExtra("companycd",companycd);
        outboxIntent.putExtra("type","V");
        outboxSpec.setContent(outboxIntent);

        // Adding all TabSpec to TabHost
        tabHost.addTab(inboxSpec); // Adding Inbox tab
        tabHost.addTab(outboxSpec); // Adding Outbox tab
        //tabHost.addTab(profileSpec); // Adding Profile tab
    }
}