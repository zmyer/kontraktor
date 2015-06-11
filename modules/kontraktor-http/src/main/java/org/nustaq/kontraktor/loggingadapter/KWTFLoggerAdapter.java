package org.nustaq.kontraktor.loggingadapter;

import org.nustaq.kontraktor.util.Log;
import org.slf4j.Logger;
import org.slf4j.Marker;

import java.util.Arrays;

/**
 * Created by ruedi on 11/06/15.
 */
public class KWTFLoggerAdapter implements Logger {

    String name;

    public KWTFLoggerAdapter(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean isTraceEnabled() {
        return false;
    }

    @Override
    public void trace(String s) {
        Log.Debug(s);
    }

    @Override
    public void trace(String s, Object o) {
        Log.Debug(s+" "+o);
    }

    @Override
    public void trace(String s, Object o, Object o1) {
        Log.Debug(s+" "+o+" "+o1); // wtf
    }

    @Override
    public void trace(String s, Object... objects) {
        Log.Debug(s+" "+ Arrays.toString(objects)); // wtf
    }

    @Override
    public void trace(String s, Throwable throwable) {
        Log.Info(s, throwable);
    }

    @Override
    public boolean isTraceEnabled(Marker marker) {
        return false;
    }

    @Override
    public void trace(Marker marker, String s) {

    }

    @Override
    public void trace(Marker marker, String s, Object o) {

    }

    @Override
    public void trace(Marker marker, String s, Object o, Object o1) {

    }

    @Override
    public void trace(Marker marker, String s, Object... objects) {

    }

    @Override
    public void trace(Marker marker, String s, Throwable throwable) {

    }

    @Override
    public boolean isDebugEnabled() {
        return Log.Lg.getSeverity() < Log.INFO;
    }

    @Override
    public void debug(String s) {
        Log.Debug(name, s);
    }

    @Override
    public void debug(String s, Object o) {
        Log.Debug(name, s+" "+o);
    }

    @Override
    public void debug(String s, Object o, Object o1) {
        Log.Debug(name, s+" "+o+" "+o1);
    }

    @Override
    public void debug(String s, Object... objects) {
        Log.Debug(name, s+" "+Arrays.toString(objects));
    }

    @Override
    public void debug(String s, Throwable throwable) {
        Log.Info(name,throwable,s);
    }

    @Override
    public boolean isDebugEnabled(Marker marker) {
        return false;
    }

    @Override
    public void debug(Marker marker, String s) {

    }

    @Override
    public void debug(Marker marker, String s, Object o) {

    }

    @Override
    public void debug(Marker marker, String s, Object o, Object o1) {

    }

    @Override
    public void debug(Marker marker, String s, Object... objects) {

    }

    @Override
    public void debug(Marker marker, String s, Throwable throwable) {

    }

    @Override
    public boolean isInfoEnabled() {
        return Log.Lg.getSeverity() <= Log.INFO;
    }

    @Override
    public void info(String s) {
        Log.Info(name,s);
    }

    @Override
    public void info(String s, Object o) {
        Log.Info(name,s+" "+o);
    }

    @Override
    public void info(String s, Object o, Object o1) {
        Log.Info(name,s+" "+o+", "+o1);
    }

    @Override
    public void info(String s, Object... objects) {
        Log.Info(name,s+" "+Arrays.toString(objects));
    }

    @Override
    public void info(String s, Throwable throwable) {
        Log.Info(name, throwable, s);
    }

    @Override
    public boolean isInfoEnabled(Marker marker) {
        return false;
    }

    @Override
    public void info(Marker marker, String s) {

    }

    @Override
    public void info(Marker marker, String s, Object o) {

    }

    @Override
    public void info(Marker marker, String s, Object o, Object o1) {

    }

    @Override
    public void info(Marker marker, String s, Object... objects) {

    }

    @Override
    public void info(Marker marker, String s, Throwable throwable) {

    }

    @Override
    public boolean isWarnEnabled() {
        return Log.Lg.getSeverity() <= Log.WARN;
    }

    @Override
    public void warn(String s) {
        Log.Warn(name,s);
    }

    @Override
    public void warn(String s, Object o) {
        Log.Warn(name,s+" "+o);
    }

    @Override
    public void warn(String s, Object... objects) {
        Log.Warn(name,s+" "+Arrays.toString(objects));
    }

    @Override
    public void warn(String s, Object o, Object o1) {
        Log.Warn(name,s+" "+o+", "+o1);
    }

    @Override
    public void warn(String s, Throwable throwable) {
        Log.Warn(name, throwable, s );
    }

    @Override
    public boolean isWarnEnabled(Marker marker) {
        return false;
    }

    @Override
    public void warn(Marker marker, String s) {

    }

    @Override
    public void warn(Marker marker, String s, Object o) {

    }

    @Override
    public void warn(Marker marker, String s, Object o, Object o1) {

    }

    @Override
    public void warn(Marker marker, String s, Object... objects) {

    }

    @Override
    public void warn(Marker marker, String s, Throwable throwable) {

    }

    @Override
    public boolean isErrorEnabled() {
        return Log.Lg.getSeverity() <= Log.ERROR;
    }

    @Override
    public void error(String s) {
        Log.Error( name, s );
    }

    @Override
    public void error(String s, Object o) {
        Log.Error( name, s +" "+o );
    }

    @Override
    public void error(String s, Object o, Object o1) {
        Log.Error( name, s +" "+o+", "+o1 );
    }

    @Override
    public void error(String s, Object... objects) {
        Log.Error( name, s +" "+Arrays.toString(objects) );
    }

    @Override
    public void error(String s, Throwable throwable) {
        Log.Error( name, throwable, s );
    }

    @Override
    public boolean isErrorEnabled(Marker marker) {
        return false;
    }

    @Override
    public void error(Marker marker, String s) {

    }

    @Override
    public void error(Marker marker, String s, Object o) {

    }

    @Override
    public void error(Marker marker, String s, Object o, Object o1) {

    }

    @Override
    public void error(Marker marker, String s, Object... objects) {

    }

    @Override
    public void error(Marker marker, String s, Throwable throwable) {

    }
}
