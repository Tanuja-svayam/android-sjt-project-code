package com.shrikantelectronics;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

public class StockSummaryAgeingListViewTab extends TabActivity {
	// TabSpec Names
	private static final String INBOX_SPEC = "Category";
	private static final String OUTBOX_SPEC = "Brand";
    private static final String MODEL_SPEC = "Model";

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
        Intent inboxIntent = new Intent(this, StockSummaryAgeing_Category_ListViewTab.class);
        // Tab Content
        inboxIntent.putExtra("companycd",companycd);
        inboxIntent.putExtra("type","Q");

        inboxSpec.setContent(inboxIntent);

        // Outbox Tab
        TabSpec outboxSpec = tabHost.newTabSpec(OUTBOX_SPEC);
        outboxSpec.setIndicator(OUTBOX_SPEC, getResources().getDrawable(R.drawable.icon_outbox));
        Intent outboxIntent = new Intent(this, StockSummaryAgeing_Brand_ListViewTab.class);
        outboxIntent.putExtra("companycd",companycd);
        outboxIntent.putExtra("type","V");
        outboxSpec.setContent(outboxIntent);

        // Outbox Tab
        TabSpec modelspec = tabHost.newTabSpec(MODEL_SPEC);
        modelspec.setIndicator(MODEL_SPEC, getResources().getDrawable(R.drawable.icon_outbox));
        Intent modelIntent = new Intent(this, StockSummaryAgeing_Model_ListViewTab.class);
        modelIntent.putExtra("companycd",companycd);
        modelIntent.putExtra("type","V");
        modelspec.setContent(modelIntent);

        // Adding all TabSpec to TabHost
        tabHost.addTab(inboxSpec); // Adding Inbox tab
        tabHost.addTab(outboxSpec); // Adding Outbox tab
        tabHost.addTab(modelspec); // Adding Outbox tab
        //tabHost.addTab(profileSpec); // Adding Profile tab
    }
}