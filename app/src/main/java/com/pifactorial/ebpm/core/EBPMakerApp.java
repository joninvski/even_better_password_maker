package com.pifactorial.ebpm;

import android.app.Application;

import com.pifactorial.BuildConfig;

import org.spongycastle.jce.provider.BouncyCastleProvider;

import static timber.log.Timber.DebugTree;

import timber.log.Timber;
import java.security.Security;

public class EBPMakerApp extends Application {

    static {
        Security.addProvider(new BouncyCastleProvider());
    }


  @Override public void onCreate() {
    super.onCreate();

    if (BuildConfig.DEBUG) {
      Timber.plant(new DebugTree());
    } else {
      Timber.plant(new CrashReportingTree());
    }
  }

  /** A tree which logs important information for crash reporting. */
  private static class CrashReportingTree extends Timber.HollowTree {
    @Override public void i(String message, Object... args) {
      // TODO e.g., Crashlytics.log(String.format(message, args));
    }

    @Override public void i(Throwable t, String message, Object... args) {
      i(message, args); // Just add to the log.
    }

    @Override public void e(String message, Object... args) {
      i("ERROR: " + message, args); // Just add to the log.
    }

    @Override public void e(Throwable t, String message, Object... args) {
      e(message, args);

      // TODO e.g., Crashlytics.logException(t);
    }
  }
}
