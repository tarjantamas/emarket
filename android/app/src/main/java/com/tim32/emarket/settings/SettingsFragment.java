package com.tim32.emarket.settings;

import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import com.tim32.emarket.R;
import com.tim32.emarket.data.entity.Setting;
import com.tim32.emarket.service.SettingService;
import com.tim32.emarket.service.SyncObserver;
import org.androidannotations.annotations.*;


/**
 * A simple {@link Fragment} subclass.
 */
@EFragment(R.layout.fragment_user_settings)
public class SettingsFragment extends Fragment implements SyncObserver {

    private Setting setting;

    @ViewById(R.id.saveChangesButton)
    Button saveChangesButton;

    @ViewById(R.id.sync_enabled)
    SwitchCompat sync_enabled;

    @ViewById(R.id.location_tracking_enabled)
    SwitchCompat location_tracking_enabled;

    @ViewById(R.id.email_notification_enabled)
    SwitchCompat email_notification_enabled;

    @ViewById(R.id.search_radius)
    EditText search_radius;

    @Bean
    SettingService settingService;

    @AfterViews
    void afterViews() {
        getActivity().setTitle(R.string.settings_fragment_title);
        loadUserSettings();
        settingService.subscribe(this);
    }

    @Click(R.id.saveChangesButton)
    void saveChangesButtonClicked() {
        updateUserSettings();
    }

    @Background
    void updateUserSettings() {
        Setting settingsDto = new Setting();
        settingsDto.setSyncEnabled(sync_enabled.isChecked());
        settingsDto.setLocationTrackingAllowed(location_tracking_enabled.isChecked());
        settingsDto.setEmailsEnabled(email_notification_enabled.isChecked());
        settingsDto.setSearchRadius(Double.parseDouble(String.valueOf(search_radius.getText())));
        try {
            settingService.updateSetting(settingsDto);
            updateSuccessToasts();
        } catch (Exception e) {
            e.printStackTrace();
            updateFailToasts();
        }
    }

    @UiThread
    void updateSuccessToasts() {
        Toast.makeText(getContext(), "Settings successfully updated", Toast.LENGTH_SHORT).show();
    }

    @UiThread
    void updateFailToasts() {
        Toast.makeText(getContext(), "Settings update fail", Toast.LENGTH_SHORT).show();
    }

    @Background
    void loadUserSettings() {
        setting = settingService.getSetting();
        loadUserSettings(setting);
    }

    @UiThread
    void loadUserSettings(Setting setting) {
        sync_enabled.setChecked(setting.getSyncEnabled());
        location_tracking_enabled.setChecked(setting.getLocationTrackingAllowed());
        email_notification_enabled.setChecked(setting.getEmailsEnabled());
        search_radius.setText(String.valueOf(setting.getSearchRadius()));
    }

    @Override
    public void onSync() {
        loadUserSettings();
    }

    @Override
    public void onDestroy() {
        settingService.unsubscribe(this);
        super.onDestroy();
    }
}
