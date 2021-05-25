package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class BaseActivity extends AppCompatActivity {

    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private UserService userService = UserService.getInstance();
    private MaterialToolbar topAppBar;

    protected FirebaseUser getLoggedInUser() {
        return auth.getCurrentUser();
    }

    protected Task<AppUser> getLoggedInAppUser() {
        if (getLoggedInUser() != null) {
            return userService.getAppUser(getLoggedInUser().getEmail());
        } else {
            return Tasks.forResult(null);
        }
    }

    public void onLogout() {
        auth.signOut();
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onStart() {
        super.onStart();

        topAppBar = findViewById(R.id.top_app_bar);
        topAppBar.setOnMenuItemClickListener(this::onMenuItemClick);

        checkUserLoggedIn();
    }

    protected void setAppBarTitle(String title) {
        topAppBar.setTitle(title);
    }

    private void checkUserLoggedIn() {
        if (getLoggedInUser() == null) {
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        }
    }

    private void onClickReceivedMenuItem() {
        Intent intent = getClearTopIntent(MainActivity.class);
        intent.putExtra(MainActivity.MESSAGE_TYPE_KEY, MainActivity.MessageType.RECEIVED);
        startActivity(intent);
    }

    private void onClickSentMenuItem() {
        Intent intent = getClearTopIntent(MainActivity.class);
        intent.putExtra(MainActivity.MESSAGE_TYPE_KEY, MainActivity.MessageType.SENT);
        startActivity(intent);
    }

    private void onClickContactsMenuItem() {
        startActivity(getClearTopIntent(ContactsActivity.class));
    }

    private boolean onMenuItemClick(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.menu_received:
                onClickReceivedMenuItem();
                return true;
            case R.id.menu_sent:
                onClickSentMenuItem();
                return true;
            case R.id.menu_contacts:
                onClickContactsMenuItem();
                return true;
            case R.id.menu_logout:
                onLogout();
                return true;
            default:
                return false;
        }
    }

    private Intent getClearTopIntent(Class clazz) {
        Intent intent = new Intent(this, clazz);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        return intent;
    }
}
