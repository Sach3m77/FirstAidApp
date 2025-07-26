package com.example.firstaidapp;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.firstaidapp.fragments.BreathingAssessmentFragment;
import com.example.firstaidapp.fragments.CallingTheEmergencyNumberFragment;
import com.example.firstaidapp.fragments.ChestCompressionsAndRescueBreathsFragment;
import com.example.firstaidapp.fragments.ChestCompressionsFragment;
import com.example.firstaidapp.fragments.ConsciousnessAssessmentFragment;
import com.example.firstaidapp.fragments.Defibrillator1Fragment;
import com.example.firstaidapp.fragments.Defibrillator2Fragment;
import com.example.firstaidapp.fragments.Defibrillator3Fragment;
import com.example.firstaidapp.fragments.Defibrillator4Fragment;
import com.example.firstaidapp.fragments.Defibrillator5Fragment;
import com.example.firstaidapp.fragments.RecoveryPosition1Fragment;
import com.example.firstaidapp.fragments.RecoveryPosition2Fragment;
import com.example.firstaidapp.fragments.RecoveryPosition3Fragment;
import com.example.firstaidapp.fragments.RecoveryPosition4Fragment;
import com.example.firstaidapp.fragments.RescueBreathsFragment;
import com.example.firstaidapp.helpers.Utility;

import java.util.ArrayList;
import java.util.Locale;

public class FirstAidStepsActivity extends AppCompatActivity {

    private LinearLayout twoChoiceMenu;
    private LinearLayout callMenu;
    private LinearLayout nextMenu;
    private LinearLayout aedMenu;
    private LinearLayout respondsMenu;
    private TextView menuTitle;
    private String alertTextView;
    private SpeechRecognizer mSpeechRecognizer;
    private Intent mSpeechIntent;
    private AudioManager mAudioManager;
    private int mStreamVolume = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_aid_steps);

        twoChoiceMenu = findViewById(R.id.two_choice_menu);
        callMenu = findViewById(R.id.call_menu);
        nextMenu = findViewById(R.id.next_menu);
        aedMenu = findViewById(R.id.aed_menu);
        menuTitle = findViewById(R.id.menu_title);
        respondsMenu = findViewById(R.id.responds_menu);

        Button negativeButton = findViewById(R.id.negative_button);
        Button positiveButton = findViewById(R.id.positive_button);
        Button phoneButton = findViewById(R.id.phone_button);
        Button nextButton = findViewById(R.id.next_next_button);
        Button callNextButton = findViewById(R.id.call_next_button);
        LinearLayout aedButton = findViewById(R.id.aed_button);
        Button aedNextButton = findViewById(R.id.aed_next_button);
        LinearLayout respondsButton = findViewById(R.id.responds_button);
        Button respondsNextButton = findViewById(R.id.responds_next_button);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, 1);
        } else {
            restartSpeechRecognizer();
        }

        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        mAudioManager.adjustStreamVolume(AudioManager.STREAM_NOTIFICATION, AudioManager.ADJUST_MUTE, 0);


        getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                updateBottomMenu();
            }
        });

        loadFragment(new ConsciousnessAssessmentFragment());

        negativeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchFragment("negative");
            }
        });

        positiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchFragment("positive");
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchFragment(null);
            }
        });

        callNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchFragment(null);
            }
        });

        aedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchFragment("aed");
            }
        });

        aedNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchFragment("aedNext");
            }
        });

        respondsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchFragment("responds");
            }
        });

        respondsNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchFragment("respondsNext");
            }
        });

        phoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utility.showAlertDialog(FirstAidStepsActivity.this, "112");
            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mSpeechRecognizer != null) {
            mSpeechRecognizer.destroy();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAudioManager.adjustStreamVolume(AudioManager.STREAM_NOTIFICATION, AudioManager.ADJUST_UNMUTE, 0);
    }


    @Override
    protected void onResume() {
        super.onResume();
        restartSpeechRecognizer();
    }

    private void restartSpeechRecognizer() {
        if (mSpeechRecognizer != null) {
            mSpeechRecognizer.destroy();
        }

        mSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        mSpeechIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        mSpeechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "pl-PL");

        mSpeechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle bundle) {}

            @Override
            public void onBeginningOfSpeech() {}

            @Override
            public void onRmsChanged(float v) {}

            @Override
            public void onBufferReceived(byte[] bytes) {}

            @Override
            public void onEndOfSpeech() {}

            @Override
            public void onError(int i) {
                if (i == SpeechRecognizer.ERROR_NO_MATCH) {
                    restartSpeechRecognizer();
                }
            }

            @Override
            public void onResults(Bundle bundle) {

                ArrayList<String> voiceResults = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                if (voiceResults != null && !voiceResults.isEmpty()) {
                    String spokenText = voiceResults.get(voiceResults.size() - 1);

                    if (spokenText.length() > 0) {

                        if (spokenText.contains("nie")) {
                            Toast.makeText(getApplicationContext(), "Rozpoznano: nie", Toast.LENGTH_SHORT).show();
                            switchFragment("negative");
                        } else if (spokenText.contains("tak")) {
                            Toast.makeText(getApplicationContext(), "Rozpoznano: tak", Toast.LENGTH_SHORT).show();
                            switchFragment("positive");
                        } else if (spokenText.contains("dalej")) {
                            Toast.makeText(getApplicationContext(), "Rozpoznano: dalej", Toast.LENGTH_SHORT).show();
                            switchFragment(null);
                        }
                    }

                }
                restartSpeechRecognizer();
            }

            @Override
            public void onPartialResults(Bundle bundle) {}

            @Override
            public void onEvent(int i, Bundle bundle) {}

        });
        mSpeechRecognizer.startListening(mSpeechIntent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Przyznano uprawnienia", Toast.LENGTH_SHORT).show();
                restartSpeechRecognizer();
            } else {
                Toast.makeText(this, "Odmówiono uprawnienia", Toast.LENGTH_SHORT).show();
            }
        }

    }

    private void loadFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.first_aid_steps_fragment_container, fragment);
        if (!(fragment instanceof ConsciousnessAssessmentFragment))
            fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void switchFragment(@Nullable String sender) {

        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.first_aid_steps_fragment_container);
        CallingTheEmergencyNumberFragment callingTheEmergencyNumberFragment = new CallingTheEmergencyNumberFragment();

        if (currentFragment instanceof ConsciousnessAssessmentFragment) {

            if (sender == "negative") {

                loadFragment(new BreathingAssessmentFragment());

            } else if (sender == "positive") {

                alertTextView = "Pozostaw poszkodowanego w pozycji zastanej";
                callingTheEmergencyNumberFragment.setText(alertTextView);
                loadFragment(callingTheEmergencyNumberFragment);
            }

        } else if (currentFragment instanceof BreathingAssessmentFragment) {

            if (sender == "negative") {

                alertTextView = "Wyznacz osobę do pomocy i przyniesienia defibrylatora (AED)";
                callingTheEmergencyNumberFragment.setText(alertTextView);
                loadFragment(callingTheEmergencyNumberFragment);

            } else if (sender == "positive") {

                alertTextView = "Ułóż poszkodowanego w pozycji bezpiecznej";
                callingTheEmergencyNumberFragment.setText(alertTextView);
                loadFragment(callingTheEmergencyNumberFragment);

            }

        } else if (currentFragment instanceof CallingTheEmergencyNumberFragment) {

            if (alertTextView == "Ułóż poszkodowanego w pozycji bezpiecznej") {
                loadFragment(new RecoveryPosition1Fragment());
            } else if (alertTextView == "Wyznacz osobę do pomocy i przyniesienia defibrylatora (AED)") {
                loadFragment(new ChestCompressionsFragment());
            } else {
                goToHomeFragment();
            }

        } else if (sender == null){

            if (currentFragment instanceof RecoveryPosition1Fragment) {
                loadFragment(new RecoveryPosition2Fragment());
            } else if (currentFragment instanceof RecoveryPosition2Fragment) {
                loadFragment(new RecoveryPosition3Fragment());
            } else if (currentFragment instanceof RecoveryPosition3Fragment) {
                loadFragment(new RecoveryPosition4Fragment());
            } else if (currentFragment instanceof RecoveryPosition4Fragment) {
                goToHomeFragment();
            } else if (currentFragment instanceof ChestCompressionsFragment) {
                loadFragment(new RescueBreathsFragment());
            } else if (currentFragment instanceof RescueBreathsFragment) {
                loadFragment(new ChestCompressionsAndRescueBreathsFragment());
            } else if (currentFragment instanceof ChestCompressionsAndRescueBreathsFragment) {

                if (sender == "aed")
                    loadFragment(new Defibrillator1Fragment());
                else if (sender == "aedNext")
                    goToHomeFragment();

            } else if (currentFragment instanceof Defibrillator1Fragment) {
                loadFragment(new Defibrillator2Fragment());
            } else if (currentFragment instanceof Defibrillator2Fragment) {
                loadFragment(new Defibrillator3Fragment());
            } else if (currentFragment instanceof Defibrillator3Fragment) {
                loadFragment(new Defibrillator4Fragment());
            } else if (currentFragment instanceof Defibrillator4Fragment) {
                loadFragment(new Defibrillator5Fragment());
            } else if (currentFragment instanceof Defibrillator5Fragment) {

                if (sender == "responds")
                    loadFragment(new RecoveryPosition1Fragment());
                else if (sender == "respondsNext")
                    goToHomeFragment();

            }

        }

    }

    private void updateBottomMenu() {

        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.first_aid_steps_fragment_container);

        if (currentFragment instanceof ConsciousnessAssessmentFragment || currentFragment instanceof BreathingAssessmentFragment) {
            twoChoiceMenu.setVisibility(View.VISIBLE);
            callMenu.setVisibility(View.GONE);
            nextMenu.setVisibility(View.GONE);
            aedMenu.setVisibility(View.GONE);
            respondsMenu.setVisibility(View.GONE);

            if (currentFragment instanceof ConsciousnessAssessmentFragment)
                menuTitle.setText("POSZKODOWANY REAGUJE?");
            else
                menuTitle.setText("POSZKODOWANY ODDYCHA?");

        } else if (currentFragment instanceof CallingTheEmergencyNumberFragment) {
            callMenu.setVisibility(View.VISIBLE);
            twoChoiceMenu.setVisibility(View.GONE);
            nextMenu.setVisibility(View.GONE);
            aedMenu.setVisibility(View.GONE);
            respondsMenu.setVisibility(View.GONE);
        } else if (currentFragment instanceof ChestCompressionsAndRescueBreathsFragment) {
            aedMenu.setVisibility(View.VISIBLE);
            twoChoiceMenu.setVisibility(View.GONE);
            callMenu.setVisibility(View.GONE);
            nextMenu.setVisibility(View.GONE);
            respondsMenu.setVisibility(View.GONE);
        } else if (currentFragment instanceof Defibrillator5Fragment) {
            respondsMenu.setVisibility(View.VISIBLE);
            twoChoiceMenu.setVisibility(View.GONE);
            callMenu.setVisibility(View.GONE);
            nextMenu.setVisibility(View.GONE);
            aedMenu.setVisibility(View.GONE);
        } else {
            nextMenu.setVisibility(View.VISIBLE);
            twoChoiceMenu.setVisibility(View.GONE);
            callMenu.setVisibility(View.GONE);
            aedMenu.setVisibility(View.GONE);
            respondsMenu.setVisibility(View.GONE);
        }
    }

    private void goToHomeFragment() {
        Intent intent = new Intent(FirstAidStepsActivity.this, MainActivity.class);
        startActivity(intent);
    }

}
