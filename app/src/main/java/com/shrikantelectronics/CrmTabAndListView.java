package com.shrikantelectronics;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

public class CrmTabAndListView extends TabActivity {
	// TabSpec Names
	private static final String COMP_SPEC = "Comp.";
	private static final String DEMO_SPEC = "Demo";
    private static final String INST_SPEC = "Inst";
    private static final String LEADS_SPEC = "Leads";
    private static final String DAMAGE_SPEC = "Damage";
    private static final String TEAM_SPEC = "Team";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crm);

        TabHost tabHost = getTabHost();

        TabSpec ComplaintsSpec = tabHost.newTabSpec(COMP_SPEC);
        ComplaintsSpec.setIndicator(COMP_SPEC, getResources().getDrawable(R.drawable.icon_inbox));
        Intent ActivityComplaintsIntent = new Intent(this, CrmActivityComplaints.class);
        ComplaintsSpec.setContent(ActivityComplaintsIntent);

        TabSpec DemoSpec = tabHost.newTabSpec(DEMO_SPEC);
        DemoSpec.setIndicator(DEMO_SPEC, getResources().getDrawable(R.drawable.icon_outbox));
        Intent ActivityDemoIntent = new Intent(this, CrmActivityDemo.class);
        DemoSpec.setContent(ActivityDemoIntent);

        TabSpec InstallationSpec = tabHost.newTabSpec(INST_SPEC);
        InstallationSpec.setIndicator(INST_SPEC, getResources().getDrawable(R.drawable.icon_outbox));
        Intent ActivityInstallationIntent = new Intent(this, CrmActivityInstallation.class);
        InstallationSpec.setContent(ActivityInstallationIntent);

        TabSpec LeadsSpec = tabHost.newTabSpec(LEADS_SPEC);
        LeadsSpec.setIndicator(LEADS_SPEC, getResources().getDrawable(R.drawable.icon_outbox));
        Intent ActivityLeadsIntent = new Intent(this, CrmActivityLeads.class);
        LeadsSpec.setContent(ActivityLeadsIntent);

        TabSpec DamageSpec = tabHost.newTabSpec(DAMAGE_SPEC);
        DamageSpec.setIndicator(DAMAGE_SPEC, getResources().getDrawable(R.drawable.icon_outbox));
        Intent ActivityDamageIntent = new Intent(this, CrmActivityDamage.class);
        DamageSpec.setContent(ActivityDamageIntent);

        TabSpec teamSpec = tabHost.newTabSpec(TEAM_SPEC);
        teamSpec.setIndicator(TEAM_SPEC, getResources().getDrawable(R.drawable.icon_outbox));
        Intent outstandingIntent  = new Intent(this, CrmActivityEmployeeStatus.class);
        teamSpec.setContent(outstandingIntent);

        // Adding all TabSpec to TabHost
        tabHost.addTab(ComplaintsSpec); // Adding Inbox tab
        tabHost.addTab(DemoSpec); // Adding Outbox tab
        tabHost.addTab(InstallationSpec); // Adding Outbox tab
        tabHost.addTab(LeadsSpec); // Adding Outbox tab
        tabHost.addTab(DamageSpec); // Adding Outbox tab
        tabHost.addTab(teamSpec); // Adding Outbox tab
    }
}