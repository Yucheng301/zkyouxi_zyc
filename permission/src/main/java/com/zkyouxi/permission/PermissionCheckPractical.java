package com.zkyouxi.permission;

import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.LocationManager;
import android.media.MediaRecorder;
import android.net.Uri;
import android.net.sip.SipManager;
import android.net.sip.SipProfile;
import android.os.Environment;
import android.provider.CalendarContract;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.provider.Telephony;
import android.provider.VoicemailContract;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.File;
import java.util.List;
import java.util.TimeZone;


/**
 * copied from AndPermission com.yanzhenjie.permission.checker
 * 实际操作判断是否有权限
 */
class PermissionCheckPractical {
    //     private static final String TAG ="PermissionCheckPractical";
    private ContentResolver mResolver;
    private Context mContext;

    PermissionCheckPractical(Context context) {
        this.mContext = context;
        mResolver = context.getContentResolver();
    }

    boolean hasPermission(String permission) {
        try {
            switch (permission) {
                case Permission.READ_CALENDAR:
                    return CheckReadCalendar();
                case Permission.WRITE_CALENDAR:
                    return CheckWriteCalendar();
                case Permission.CAMERA:
                    return CheckCamera();
                case Permission.READ_CONTACTS:
                    return CheckReadContracts();
                case Permission.WRITE_CONTACTS:
                    return CheckWriteContracts();
                case Permission.GET_ACCOUNTS:
                    return true;
                case Permission.ACCESS_COARSE_LOCATION:
                    return CheckCoarseLocation();
                case Permission.ACCESS_FINE_LOCATION:
                    return CheckFineLocation();
                case Permission.RECORD_AUDIO:
                    return checkRecordAudio();
                case Permission.READ_PHONE_STATE:
                    return checkReadPhoneState();
                case Permission.CALL_PHONE:
                    return true;
                case Permission.READ_CALL_LOG:
                    return CheckReadCallLog();
                case Permission.WRITE_CALL_LOG:
                    return CheckWriteCallLog();
                case Permission.ADD_VOICEMAIL:
                    return CheckAddVoiceMail();
                case Permission.USE_SIP:
                    return CheckSip();
                case Permission.PROCESS_OUTGOING_CALLS:
                    return true;
                case Permission.BODY_SENSORS:
                    return CheckSensor();
                case Permission.SEND_SMS:
                case Permission.RECEIVE_MMS:
                    return true;
                case Permission.READ_SMS:
                    return CheckReadSMS();
                case Permission.RECEIVE_WAP_PUSH:
                case Permission.RECEIVE_SMS:
                    return true;
                case Permission.READ_EXTERNAL_STORAGE:
                    return CheckReadStorage();
                case Permission.WRITE_EXTERNAL_STORAGE:
                    return CheckWriteStorage();
            }
        } catch (Throwable e) {
            return false;
        }
        return true;
    }

    /**
     * 检测读取日历权限
     */
    @TargetApi(14)
    private Boolean CheckReadCalendar() throws Throwable {
        String[] projection = new String[]{CalendarContract.Calendars._ID, CalendarContract.Calendars.NAME};
        Cursor cursor = mResolver.query(CalendarContract.Calendars.CONTENT_URI, projection, null, null, null);
        if (cursor != null) {
            try {
                CursorTest.read(cursor);
            } finally {
                cursor.close();
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * 检测修改日历权限
     */
    @TargetApi(14)
    private boolean CheckWriteCalendar() throws Throwable {
        final String NAME = "PERMISSION";
        final String ACCOUNT = "permission@gmail.com";
        try {
            TimeZone timeZone = TimeZone.getDefault();
            ContentValues value = new ContentValues();
            value.put(NAME, NAME);
            value.put(CalendarContract.Calendars.ACCOUNT_NAME, ACCOUNT);
            value.put(CalendarContract.Calendars.ACCOUNT_TYPE, CalendarContract.ACCOUNT_TYPE_LOCAL);
            value.put(CalendarContract.Calendars.CALENDAR_DISPLAY_NAME, NAME);
            value.put(CalendarContract.Calendars.VISIBLE, 1);
            value.put(CalendarContract.Calendars.CALENDAR_COLOR, Color.BLUE);
            value.put(CalendarContract.Calendars.CALENDAR_ACCESS_LEVEL, CalendarContract.Calendars.CAL_ACCESS_OWNER);
            value.put(CalendarContract.Calendars.SYNC_EVENTS, 1);
            value.put(CalendarContract.Calendars.CALENDAR_TIME_ZONE, timeZone.getID());
            value.put(CalendarContract.Calendars.OWNER_ACCOUNT, NAME);
            value.put(CalendarContract.Calendars.CAN_ORGANIZER_RESPOND, 0);

            Uri insertUri = CalendarContract.Calendars.CONTENT_URI.buildUpon()
                    .appendQueryParameter(CalendarContract.CALLER_IS_SYNCADAPTER, "true")
                    .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_NAME, NAME)
                    .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_TYPE, CalendarContract.ACCOUNT_TYPE_LOCAL)
                    .build();
            Uri resourceUri = mResolver.insert(insertUri, value);
            return ContentUris.parseId(resourceUri) > 0;
        } finally {
            Uri deleteUri = CalendarContract.Calendars.CONTENT_URI.buildUpon().build();
            mResolver.delete(deleteUri, CalendarContract.Calendars.ACCOUNT_NAME + "=?", new String[]{ACCOUNT});
        }
    }

    /**
     * 检测相机权限
     */
    private boolean CheckCamera() throws Throwable {
        SurfaceView surfaceView = new SurfaceView(mContext);
        SurfaceHolder holder = surfaceView.getHolder();
        holder.addCallback(CALLBACK);

        Camera camera = null;
        try {
            camera = Camera.open();
            Camera.Parameters parameters = camera.getParameters();
            camera.setParameters(parameters);
            camera.setPreviewDisplay(holder);
            camera.setPreviewCallback(PREVIEW_CALLBACK);
            camera.startPreview();
            return true;
        } catch (Throwable e) {
            PackageManager packageManager = mContext.getPackageManager();
            return !packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA);
        } finally {
            if (camera != null) {
                camera.stopPreview();
                camera.setPreviewDisplay(null);
                camera.setPreviewCallback(null);
                camera.release();
            }
        }
    }

    private static final Camera.PreviewCallback PREVIEW_CALLBACK = new Camera.PreviewCallback() {
        @Override
        public void onPreviewFrame(byte[] data, Camera camera) {
        }
    };

    private static final SurfaceHolder.Callback CALLBACK = new SurfaceHolder.Callback() {
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
        }
    };

    /**
     * 检测能否读取联系人
     */
    private boolean CheckReadContracts() throws Throwable {
        String[] projection = new String[]{ContactsContract.Data._ID, ContactsContract.Data.DATA1};
        Cursor cursor = mResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, projection, null, null, null);
        if (cursor != null) {
            try {
                CursorTest.read(cursor);
            } finally {
                cursor.close();
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * 检测能否修改联系人
     */
    private final String DISPLAY_NAME = "PERMISSION";

    private boolean CheckWriteContracts() throws Throwable {
        Cursor cursor = mResolver.query(ContactsContract.Data.CONTENT_URI,
                new String[]{ContactsContract.Data.RAW_CONTACT_ID},
                ContactsContract.Data.MIMETYPE + "=? and " + ContactsContract.Data.DATA1 + "=?",
                new String[]{ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE, DISPLAY_NAME},
                null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                long rawContactId = cursor.getLong(0);
                cursor.close();
                return update(rawContactId);
            } else {
                cursor.close();
                return insert();
            }
        }
        return false;
    }

    private boolean insert() {
        ContentValues values = new ContentValues();
        Uri rawContractUri = mResolver.insert(ContactsContract.RawContacts.CONTENT_URI, values);
        long rawContactId = ContentUris.parseId(rawContractUri);

        values.put(ContactsContract.Data.RAW_CONTACT_ID, rawContactId);
        values.put(ContactsContract.Data.DATA1, DISPLAY_NAME);
        values.put(ContactsContract.Data.DATA2, DISPLAY_NAME);
        values.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE);
        Uri dataUri = mResolver.insert(ContactsContract.Data.CONTENT_URI, values);
        return ContentUris.parseId(dataUri) > 0;
    }

    private void delete(long contactId, long dataId) {
        mResolver.delete(ContactsContract.RawContacts.CONTENT_URI, ContactsContract.RawContacts._ID + "=?", new String[]{Long.toString(contactId)});
        mResolver.delete(ContactsContract.Data.CONTENT_URI, ContactsContract.Data._ID + "=?", new String[]{Long.toString(dataId)});
    }

    private boolean update(long rawContactId) {
        ContentValues values = new ContentValues();
        values.put(ContactsContract.Data.RAW_CONTACT_ID, rawContactId);
        values.put(ContactsContract.Data.DATA1, DISPLAY_NAME);
        values.put(ContactsContract.Data.DATA2, DISPLAY_NAME);
        values.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE);
        Uri dataUri = mResolver.insert(ContactsContract.Data.CONTENT_URI, values);
        return ContentUris.parseId(dataUri) > 0;
    }

    /**
     * 检测能否获得粗略未知
     */
    private boolean CheckCoarseLocation() throws Throwable {
        LocationManager locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        List<String> providers = locationManager.getProviders(true);
        boolean networkProvider = providers.contains(LocationManager.NETWORK_PROVIDER);
        if (networkProvider) {
            return true;
        }

        PackageManager packageManager = mContext.getPackageManager();
        boolean networkHardware = packageManager.hasSystemFeature(PackageManager.FEATURE_LOCATION_NETWORK);
        if (!networkHardware) return true;

        return !locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    /**
     * 能否较好的获得位置
     */
    private boolean CheckFineLocation() throws Throwable {
        LocationManager locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        List<String> providers = locationManager.getProviders(true);
        boolean gpsProvider = providers.contains(LocationManager.GPS_PROVIDER);
        boolean passiveProvider = providers.contains(LocationManager.PASSIVE_PROVIDER);
        if (gpsProvider || passiveProvider) {
            return true;
        }

        PackageManager packageManager = mContext.getPackageManager();
        boolean gpsHardware = packageManager.hasSystemFeature(PackageManager.FEATURE_LOCATION_GPS);
        if (!gpsHardware) return true;

        return !locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    /**
     * 檢測能否獲得音頻權限
     */
    private boolean checkRecordAudio() throws Throwable {
        File mTempFile = null;
        MediaRecorder mediaRecorder = new MediaRecorder();

        try {
            mTempFile = File.createTempFile("permission", "test");

            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.AMR_NB);
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mediaRecorder.setOutputFile(mTempFile.getAbsolutePath());
            mediaRecorder.prepare();
            mediaRecorder.start();
            return true;
        } catch (Throwable e) {
            PackageManager packageManager = mContext.getPackageManager();
            return !packageManager.hasSystemFeature(PackageManager.FEATURE_MICROPHONE);
        } finally {
            try {
                mediaRecorder.stop();
            } catch (Exception ignored) {
            }
            try {
                mediaRecorder.release();
            } catch (Exception ignored) {
            }

            if (mTempFile != null && mTempFile.exists()) {
                mTempFile.delete();
            }
        }
    }

    /**
     * 檢測能否獲得手機狀態
     */
    @TargetApi(23)
    private boolean checkReadPhoneState() throws SecurityException {
        PackageManager packageManager = mContext.getPackageManager();
        if (!packageManager.hasSystemFeature(PackageManager.FEATURE_TELEPHONY)) return true;

        TelephonyManager telephonyManager = (TelephonyManager)mContext.getSystemService(Context.TELEPHONY_SERVICE);
        return telephonyManager.getPhoneType() == TelephonyManager.PHONE_TYPE_NONE ||
                !TextUtils.isEmpty(telephonyManager.getDeviceId()) ||
                !TextUtils.isEmpty(telephonyManager.getSubscriberId());

    }

    /**
     * 檢測能否獲得手機通話記錄
     */
    private boolean CheckReadCallLog() throws Throwable {
        String[] projection = new String[]{CallLog.Calls._ID, CallLog.Calls.NUMBER, CallLog.Calls.TYPE};
        Cursor cursor = mResolver.query(CallLog.Calls.CONTENT_URI, projection, null, null, null);
        if (cursor != null) {
            try {
                CursorTest.read(cursor);
            } finally {
                cursor.close();
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * 檢測能否寫通話記錄
     */
    private boolean CheckWriteCallLog() throws Throwable {
        try {
            ContentValues content = new ContentValues();
            content.put(CallLog.Calls.TYPE, CallLog.Calls.INCOMING_TYPE);
            content.put(CallLog.Calls.NUMBER, "1");
            content.put(CallLog.Calls.DATE, 20080808);
            content.put(CallLog.Calls.NEW, "0");
            Uri resourceUri = mResolver.insert(CallLog.Calls.CONTENT_URI, content);
            return ContentUris.parseId(resourceUri) > 0;
        } finally {
            mResolver.delete(CallLog.Calls.CONTENT_URI, CallLog.Calls.NUMBER + "=?", new String[]{"1"});
        }
    }

    /***
     * 檢測能否獲得語言信息權限
     */
    @TargetApi(14)
    private boolean CheckAddVoiceMail() throws Throwable {
        try {
            Uri mBaseUri = VoicemailContract.Voicemails.CONTENT_URI;
            ContentValues contentValues = new ContentValues();
            contentValues.put(VoicemailContract.Voicemails.DATE, System.currentTimeMillis());
            contentValues.put(VoicemailContract.Voicemails.NUMBER, "1");
            contentValues.put(VoicemailContract.Voicemails.DURATION, 1);
            contentValues.put(VoicemailContract.Voicemails.SOURCE_PACKAGE, "permission");
            contentValues.put(VoicemailContract.Voicemails.SOURCE_DATA, "permission");
            contentValues.put(VoicemailContract.Voicemails.IS_READ, 0);
            Uri newVoicemailUri = mResolver.insert(mBaseUri, contentValues);
            long id = ContentUris.parseId(newVoicemailUri);
            int count = mResolver.delete(mBaseUri, VoicemailContract.Voicemails._ID + "=?", new String[]{Long.toString(id)});
            return count > 0;
        } catch (Exception e) {
            String message = e.getMessage();
            if (!TextUtils.isEmpty(message)) {
                message = message.toLowerCase();
                return !message.contains("add_voicemail");
            }
            return false;
        }
    }

    /**
     * 檢測能否獲得ip信息
     */
    private boolean CheckSip() throws Throwable {
        if (!SipManager.isApiSupported(mContext)) {
            return true;
        }
        SipManager manager = SipManager.newInstance(mContext);
        if (manager == null) {
            return true;
        }
        SipProfile.Builder builder = new SipProfile.Builder("Permission", "127.0.0.1");
        builder.setPassword("password");
        SipProfile profile = builder.build();
        manager.open(profile);
        manager.close(profile.getUriString());
        return true;
    }

    /**
     * 檢測是否有傳感器權限
     */
    private boolean CheckSensor() throws Throwable {
        SensorManager sensorManager = (SensorManager) mContext.getSystemService(Context.SENSOR_SERVICE);
        try {
            Sensor heartRateSensor = sensorManager.getDefaultSensor(Sensor.TYPE_HEART_RATE);
            sensorManager.registerListener(SENSOR_EVENT_LISTENER, heartRateSensor, 3);
            sensorManager.unregisterListener(SENSOR_EVENT_LISTENER, heartRateSensor);
        } catch (Throwable e) {
            PackageManager packageManager = mContext.getPackageManager();
            return !packageManager.hasSystemFeature(PackageManager.FEATURE_SENSOR_HEART_RATE);
        }
        return true;
    }

    private static final SensorEventListener SENSOR_EVENT_LISTENER = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };


    /**
     * 檢測能否發送信息
     */
    @TargetApi(19)
    private boolean CheckReadSMS() throws Throwable {
        String[] projection = new String[]{Telephony.Sms._ID, Telephony.Sms.ADDRESS, Telephony.Sms.PERSON, Telephony.Sms.BODY};
        Cursor cursor = mResolver.query(Telephony.Sms.CONTENT_URI, projection, null, null, null);
        if (cursor != null) {
            try {
                CursorTest.read(cursor);
            } finally {
                cursor.close();
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * 檢測能否讀sdCARD
     */
    private boolean CheckReadStorage() throws Throwable {
        File directory = Environment.getExternalStorageDirectory();
        if (directory.exists() && directory.canRead()) {
            long modified = directory.lastModified();
            String[] pathList = directory.list();
            return modified > 0 && pathList != null;
        }
        return false;
    }

    /**
     * 檢測能否寫sdCard
     */
    private boolean CheckWriteStorage() throws Throwable {
        File directory = Environment.getExternalStorageDirectory();
        if (!directory.exists() || !directory.canWrite()) return false;
        File parent = new File(directory, "Android");
        if (parent.exists() && parent.isFile()) if (!parent.delete()) return false;
        if (!parent.exists()) if (!parent.mkdirs()) return false;
        File file = new File(parent, "ANDROID.PERMISSION.TEST");
        if (file.exists()) return file.delete();
        else return file.createNewFile();
    }


    private static class CursorTest {
        private static void read(Cursor cursor) {
            int count = cursor.getCount();
            if (count > 0) {
                cursor.moveToFirst();
                int type = cursor.getType(0);
                switch (type) {
                    case Cursor.FIELD_TYPE_BLOB:
                    case Cursor.FIELD_TYPE_NULL: {
                        break;
                    }
                    case Cursor.FIELD_TYPE_INTEGER:
                    case Cursor.FIELD_TYPE_FLOAT:
                    case Cursor.FIELD_TYPE_STRING:
                    default: {
                        cursor.getString(0);
                        break;
                    }
                }
            }
        }
    }
}
