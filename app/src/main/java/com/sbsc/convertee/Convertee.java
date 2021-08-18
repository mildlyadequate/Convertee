package com.sbsc.convertee;

import android.app.Application;
import android.content.Context;

import org.acra.ACRA;
import org.acra.config.CoreConfigurationBuilder;
import org.acra.config.MailSenderConfigurationBuilder;
import org.acra.config.ToastConfigurationBuilder;
import org.acra.data.StringFormat;

public class Convertee extends Application {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);

        CoreConfigurationBuilder builder = new CoreConfigurationBuilder(this);

        //core configuration:
        builder
                .withBuildConfigClass(BuildConfig.class)
                .withReportFormat(StringFormat.JSON);

        //each plugin you chose above can be configured with its builder like this:
        builder.getPluginConfigurationBuilder(ToastConfigurationBuilder.class)
                .withResText(R.string.acra_toast_text)
                //make sure to enable all plugins you want to use:
                .withEnabled(true);

        builder.getPluginConfigurationBuilder(MailSenderConfigurationBuilder.class)
                //required
                .withMailTo("sbsc-support@protonmail.com")
                //defaults to true
                .withReportAsFile(true)
                //defaults to ACRA-report.stacktrace
                .withReportFileName("stacktrace.txt")
                //defaults to "<applicationId> Crash Report"
                .withSubject("Convertee Crash Report")
                //defaults to empty
                .withBody("").withEnabled(true);



        ACRA.init(this, builder);
    }


}
