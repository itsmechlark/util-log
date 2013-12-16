/*
 * Copyright 2013 John Chlark Sumatra
 * 
 * based on roboguice.util.temp.Ln
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
*/
package com.itsmechlark.util;

import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

public final class Log {


    private static final boolean DEBUGGING = true;
    /**
     * Priority constant for the println method; use Log.v.
     */
    public static final int VERBOSE = 2;

    /**
     * Priority constant for the println method; use Log.d.
     */
    public static final int DEBUG = 3;

    /**
     * Priority constant for the println method; use Log.i.
     */
    public static final int INFO = 4;

    /**
     * Priority constant for the println method; use Log.w.
     */
    public static final int WARN = 5;

    /**
     * Priority constant for the println method; use Log.e.
     */
    public static final int ERROR = 6;

    /**
     * Priority constant for the println method.
     */
    public static final int ASSERT = 7;

    private static Log helper;

    private static Context mContext;

    private static String log_name;

    /**
     * config is initially set to BaseConfig() with sensible defaults, then
     * replaced by BaseConfig(ContextSingleton) during guice static injection
     * pass.
     */
    private static final BaseConfig CONFIG = new BaseConfig();

    /**
     * print is initially set to Print(), then replaced by guice during static
     * injection pass. This allows overriding where the log message is delivered
     * to.
     */
    private static Print print = new Print();

    public static synchronized Log getInstance(Context context, String name) {
        log_name = name;
        if (mContext == null) helper = new Log(context);
        return helper;
    }

    public Log(Context context) {
        mContext = context;
    }

    public static String getStackTraceString(Throwable tr) {
        return android.util.Log.getStackTraceString(tr);
    }

    public static boolean isLoggable(String tag, int level) {
        return android.util.Log.isLoggable(tag, level);
    }

    public static int println(int priority, String tag, String msg) {
        writeToFile(mContext, logLevelToString(priority), Print.getScope(), msg);
        return print.println(priority, msg);
    }

    public static int v(Throwable tr) {
        writeToFile(mContext, "VERBOSE", Print.getScope(), getStackTraceString(tr));
        return print.println(Log.VERBOSE, getStackTraceString(tr));
    }

    public static int v(String msg) {
        writeToFile(mContext, "VERBOSE", Print.getScope(), msg);
        return print.println(Log.VERBOSE, msg);
    }

    /**
     * Send a {@link #VERBOSE} log message.
     *
     * @param tag Used to identify the source of a log message.  It usually identifies
     *            the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     */
    public static int v(String tag, String msg) {
        writeToFile(mContext, "VERBOSE", Print.getScope(), msg);
        return print.println(Log.VERBOSE, msg);
    }

    /**
     * Send a {@link #VERBOSE} log message.
     *
     * @param tag Used to identify the source of a log message.  It usually identifies
     *            the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     */
    public static int v(Context context, String tag, String msg) {
        writeToFile(context, "VERBOSE", Print.getScope(), msg);
        return print.println(Log.VERBOSE, msg);
    }

    /**
     * Send a {@link #VERBOSE} log message and log the exception.
     *
     * @param tag Used to identify the source of a log message.  It usually identifies
     *            the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     * @param tr  An exception to log
     */
    public static int v(String tag, String msg, Throwable tr) {
        writeToFile(mContext, "VERBOSE", Print.getScope(), msg + "\n" + Log.getStackTraceString(tr));
        return print.println(Log.VERBOSE, msg + "\n" + Log.getStackTraceString(tr));
    }

    /**
     * Send a {@link #VERBOSE} log message and log the exception.
     *
     * @param tag Used to identify the source of a log message.  It usually identifies
     *            the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     * @param tr  An exception to log
     */
    public static int v(Context context, String tag, String msg, Throwable tr) {
        writeToFile(context, "VERBOSE", Print.getScope(), msg + "\n" + Log.getStackTraceString(tr));
        return print.println(Log.VERBOSE, msg + "\n" + Log.getStackTraceString(tr));
    }

    /**
     * Send a {@link #DEBUG} log message and log the exception.
     *
     * @param tr  An exception to log
     */
    public static int d(Throwable tr) {
        writeToFile(mContext, "DEBUG", Print.getScope(), getStackTraceString(tr));
        return print.println(Log.DEBUG, getStackTraceString(tr));
    }

    /**
     * Send a {@link #DEBUG} log message.
     *
     * @param msg The message you would like logged.
     */
    public static int d(String msg) {
        writeToFile(mContext, "DEBUG", Print.getScope(), msg);
        return print.println(Log.DEBUG, msg);
    }

    /**
     * Send a {@link #DEBUG} log message.
     *
     * @param tag Used to identify the source of a log message.  It usually identifies
     *            the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     */
    public static int d(String tag, String msg) {
        writeToFile(mContext, "DEBUG", Print.getScope(), msg);
        return print.println(Log.DEBUG, msg);
    }

    /**
     * Send a {@link #DEBUG} log message.
     *
     * @param tag Used to identify the source of a log message.  It usually identifies
     *            the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     */
    public static int d(Context context, String tag, String msg) {
        writeToFile(context, "DEBUG", Print.getScope(), msg);
        return print.println(Log.DEBUG, msg);
    }

    /**
     * Send a {@link #DEBUG} log message and log the exception.
     *
     * @param tag Used to identify the source of a log message.  It usually identifies
     *            the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     * @param tr  An exception to log
     */
    public static int d(String tag, String msg, Throwable tr) {
        writeToFile(mContext, "DEBUG", Print.getScope(), msg + "\n" + Log.getStackTraceString(tr));
        return print.println(Log.DEBUG, msg + "\n" + Log.getStackTraceString(tr));
    }

    /**
     * Send a {@link #DEBUG} log message and log the exception.
     *
     * @param tag Used to identify the source of a log message.  It usually identifies
     *            the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     * @param tr  An exception to log
     */
    public static int d(Context context, String tag, String msg, Throwable tr) {
        writeToFile(context, "DEBUG", Print.getScope(), msg + "\n" + Log.getStackTraceString(tr));
        return print.println(Log.DEBUG, msg + "\n" + Log.getStackTraceString(tr));
    }

    /**
     * Send a {@link #INFO} log message and log the exception.
     *
     * @param tr  An exception to log
     */
    public static int i(Throwable tr) {
        writeToFile(mContext, "INFO", Print.getScope(), getStackTraceString(tr));
        return print.println(Log.INFO, getStackTraceString(tr));
    }

    /**
     * Send an {@link #INFO} log message.
     *
     * @param msg The message you would like logged.
     */
    public static int i(String msg) {
        writeToFile(mContext, "INFO", Print.getScope(), msg);
        return print.println(Log.INFO, msg);
    }

    /**
     * Send an {@link #INFO} log message.
     *
     * @param tag Used to identify the source of a log message. It usually
     *            identifies the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     */
    public static int i(String tag, String msg) {
        writeToFile(mContext, "INFO", Print.getScope(), msg);
        return print.println(Log.INFO, msg);
    }

    /**
     * Send an {@link #INFO} log message.
     *
     * @param tag Used to identify the source of a log message.  It usually identifies
     *            the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     */
    public static int i(Context context, String tag, String msg) {
        writeToFile(context, "INFO", Print.getScope(), msg);
        return print.println(Log.INFO, msg);
    }

    /**
     * Send a {@link #INFO} log message and log the exception.
     *
     * @param tag Used to identify the source of a log message.  It usually identifies
     *            the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     * @param tr  An exception to log
     */
    public static int i(String tag, String msg, Throwable tr) {
        writeToFile(mContext, "INFO", Print.getScope(), msg + "\n" + Log.getStackTraceString(tr));
        return print.println(Log.INFO, msg + "\n" + Log.getStackTraceString(tr));
    }

    /**
     * Send a {@link #INFO} log message and log the exception.
     *
     * @param tag Used to identify the source of a log message.  It usually identifies
     *            the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     * @param tr  An exception to log
     */
    public static int i(Context context, String tag, String msg, Throwable tr) {
        writeToFile(context, "INFO", Print.getScope(), msg + "\n" + Log.getStackTraceString(tr));
        return print.println(Log.INFO, msg + "\n" + Log.getStackTraceString(tr));
    }

    /**
     * Send a {@link #WARN} log message and log the exception.
     *
     * @param tr  An exception to log
     */
    public static int w(Throwable tr) {
        writeToFile(mContext, "WARN", Print.getScope(), getStackTraceString(tr));
        return print.println(Log.WARN, getStackTraceString(tr));
    }

    /**
     * Send a {@link #WARN} log message.
     *
     * @param msg The message you would like logged.
     */
    public static int w(String msg) {
        writeToFile(mContext, "WARN", Print.getScope(), msg);
        return print.println(Log.WARN, msg);
    }

    /**
     * Send a {@link #WARN} log message.
     *
     * @param tag Used to identify the source of a log message.  It usually identifies
     *            the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     */
    public static int w(String tag, String msg) {
        writeToFile(mContext, "WARN", Print.getScope(), msg);
        return print.println(Log.WARN, msg);
    }

    /**
     * Send a {@link #WARN} log message.
     *
     * @param tag Used to identify the source of a log message.  It usually identifies
     *            the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     */
    public static int w(Context context, String tag, String msg) {
        writeToFile(context, "WARN", Print.getScope(), msg);
        return print.println(Log.WARN, msg);
    }

    /**
     * Send a {@link #WARN} log message and log the exception.
     *
     * @param tag Used to identify the source of a log message.  It usually identifies
     *            the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     * @param tr  An exception to log
     */
    public static int w(String tag, String msg, Throwable tr) {
        writeToFile(mContext, "WARN", Print.getScope(), msg + "\n" + Log.getStackTraceString(tr));
        return print.println(Log.WARN, msg + "\n" + Log.getStackTraceString(tr));
    }

    /**
     * Send a {@link #WARN} log message and log the exception.
     *
     * @param tag Used to identify the source of a log message.  It usually identifies
     *            the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     * @param tr  An exception to log
     */
    public static int w(Context context, String tag, String msg, Throwable tr) {
        writeToFile(context, "WARN", Print.getScope(), msg + "\n" + Log.getStackTraceString(tr));
        return print.println(Log.WARN, msg + "\n" + Log.getStackTraceString(tr));
    }

    /**
     * Send a {@link #WARN} log message and log the exception.
     *
     * @param tag Used to identify the source of a log message.  It usually identifies
     *            the class or activity where the log call occurs.
     * @param tr  An exception to log
     */
    public static int w(String tag, Throwable tr) {
        writeToFile(mContext, "WARN", Print.getScope(), Log.getStackTraceString(tr));
        return print.println(Log.WARN, Log.getStackTraceString(tr));
    }

    /**
     * Send a {@link #WARN} log message and log the exception.
     *
     * @param tag Used to identify the source of a log message.  It usually identifies
     *            the class or activity where the log call occurs.
     * @param tr  An exception to log
     */
    public static int w(Context context, String tag, Throwable tr) {
        writeToFile(context, "WARN", Print.getScope(), Log.getStackTraceString(tr));
        return print.println(Log.WARN, Log.getStackTraceString(tr));
    }

    /**
     * Send a {@link #ERROR} log message and log the exception.
     *
     * @param tr  An exception to log
     */
    public static int e(Throwable tr) {
        writeToFile(mContext, "ERROR", Print.getScope(), getStackTraceString(tr));
        return print.println(Log.ERROR, getStackTraceString(tr));
    }

    /**
     * Send an {@link #ERROR} log message.
     *
     * @param msg The message you would like logged.
     */
    public static int e(String msg) {
        writeToFile(mContext, "ERROR", Print.getScope(), msg);
        return print.println(Log.ERROR, msg);
    }

    /**
     * Send an {@link #ERROR} log message.
     *
     * @param tag Used to identify the source of a log message.  It usually identifies
     *            the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     */
    public static int e(String tag, String msg) {
        writeToFile(mContext, "ERROR", Print.getScope(), msg);
        return print.println(Log.ERROR, msg);
    }

    /**
     * Send an {@link #ERROR} log message.
     *
     * @param tag Used to identify the source of a log message.  It usually identifies
     *            the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     */
    public static int e(Context context, String tag, String msg) {
        writeToFile(mContext, "ERROR", Print.getScope(), msg);
        return print.println(Log.ERROR, msg);
    }

    /**
     * Send a {@link #ERROR} log message and log the exception.
     *
     * @param tag Used to identify the source of a log message.  It usually identifies
     *            the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     * @param tr  An exception to log
     */
    public static int e(String tag, String msg, Throwable tr) {
        writeToFile(mContext, "ERROR", Print.getScope(), msg + "\n" + Log.getStackTraceString(tr));
        return print.println(Log.ERROR, msg + "\n" + Log.getStackTraceString(tr));
    }

    /**
     * Send a {@link #ERROR} log message and log the exception.
     *
     * @param tag Used to identify the source of a log message.  It usually identifies
     *            the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     * @param tr  An exception to log
     */
    public static int e(Context context, String tag, String msg, Throwable tr) {
        writeToFile(context, "ERROR", Print.getScope(), msg + "\n" + Log.getStackTraceString(tr));
        return print.println(Log.ERROR, msg + "\n" + Log.getStackTraceString(tr));
    }

    public static boolean isDebugEnabled() {
        return CONFIG.minimumLogLevel <= Log.DEBUG;
    }

    public static boolean isVerboseEnabled() {
        return CONFIG.minimumLogLevel <= Log.VERBOSE;
    }

    public static Config getConfig() {

        return CONFIG;
    }

    public interface Config {
        int getLoggingLevel();

        void setLoggingLevel(int level);
    }

    public static class BaseConfig implements Config {
        protected int minimumLogLevel = Log.VERBOSE;
        protected String packageName = "";
        protected String scope = "";

        protected BaseConfig() {
        }

        public BaseConfig(Application context) {
            try {
                packageName = context.getPackageName();
                final int flags = context.getPackageManager().getApplicationInfo(packageName, 0).flags;
                minimumLogLevel = (flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0 ? Log.VERBOSE : Log.INFO;
                scope = packageName.toUpperCase(Locale.US);
                d("", "Configuring Logging, minimum log level is %s" + "\n" + logLevelToString(minimumLogLevel));
            } catch (Exception e) {
                Log.e(packageName, "Error configuring logger", e);
            }
        }

        @Override
        public int getLoggingLevel() {
            return minimumLogLevel;
        }

        @Override
        public void setLoggingLevel(int level) {
            minimumLogLevel = level;
        }
    }

    public static String logLevelToString(int loglevel) {
        switch (loglevel) {
            case Log.VERBOSE:
                return "VERBOSE";
            case Log.DEBUG:
                return "DEBUG";
            case Log.INFO:
                return "INFO";
            case Log.WARN:
                return "WARN";
            case Log.ERROR:
                return "ERROR";
            case Log.ASSERT:
                return "ASSERT";

            default:
                return "UNKNOWN";
        }

    }

    public static void setPrint(Print print) {
        Log.print = print;
    }

    /**
     * Default implementation logs to android.util.Log
     */
    public static class Print {
        private static final int DEFAULT_STACK_TRACE_LINE_COUNT = 5;

        public int println(int priority, String msg) {
            if (CONFIG.minimumLogLevel > priority) {
                return 0;
            }
            return android.util.Log.println(priority, getScope(), processMessage(msg));
        }

        protected String processMessage(String msg) {
            if (CONFIG.minimumLogLevel <= Log.DEBUG) {
                msg = String.format("%s %s %s", new SimpleDateFormat("HH:mm:ss.SSS", Locale.US).format(System.currentTimeMillis()), Thread.currentThread().getName(), msg);
            }
            return msg;
        }

        protected static String getScope() {
            if (CONFIG.minimumLogLevel <= Log.DEBUG) {
                final StackTraceElement trace = Thread.currentThread().getStackTrace()[DEFAULT_STACK_TRACE_LINE_COUNT];
                return CONFIG.scope + "/" + trace.getFileName() + ":" + trace.getLineNumber();
            }

            return CONFIG.scope;
        }

    }

    public static void writeToFile(Context context, String type, String tag, String msg) {
        if (context == null)return;
        FileOutputStream out = null;
        File InternalRoot = new File(context.getFilesDir().getAbsolutePath());
        if (InternalRoot.canWrite() && out == null) {
            InternalRoot.mkdirs();
            if (InternalRoot.isDirectory()) {
                InternalRoot = new File(InternalRoot.getAbsolutePath() + "/log");
                InternalRoot.mkdirs();
                if (InternalRoot.isDirectory()) {
                    try {
                        File LogFile = new File(InternalRoot, log_name);
                        out = new FileOutputStream(LogFile, true);
                    } catch (Exception e) {
                        if (DEBUGGING) e("", "Log", e);
                    }
                }
            }
        }
        try {
            Calendar cal = new GregorianCalendar(TimeZone.getTimeZone("GMT+8"));
            String data = "Logged at " + String.valueOf(cal.get(Calendar.HOUR_OF_DAY) + ":" + cal.get(Calendar.MINUTE) + ":"
                    + cal.get(Calendar.SECOND) + " " + String.valueOf(cal.get(Calendar.MONTH))
                    + "-" + String.valueOf(cal.get(Calendar.DAY_OF_MONTH))
                    + "-" + String.valueOf(cal.get(Calendar.YEAR)) + "\n"
                    + type + "@" + tag + "\t" + msg + "\n");
            out.write(data.getBytes());
            out.close();
        } catch (Exception e) {
            if (DEBUGGING) e("", "Log", e);
        }
    }
}
