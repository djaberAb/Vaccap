package com.example.vaccap;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SignupFragment extends Fragment {

    public SignupFragment() {
        super(R.layout.fragment_login);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_signup, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        EditText usernameInput = view.findViewById(R.id.username);
        EditText emailInput = view.findViewById(R.id.email);
        EditText passwordInput = view.findViewById(R.id.password);
        Button signupButton = view.findViewById(R.id.signin_btn);
        TextView signinLink = view.findViewById(R.id.signup_link);

        signupButton.setOnClickListener(v -> registerUser());
        signinLink.setOnClickListener(v -> {
            Fragment loginFragment = new LoginFragment();
            FragmentManager fragmentManager = getParentFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.signupFragment, loginFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();

        });
    }

    private void registerUser() {
        EditText usernameInput = getView().findViewById(R.id.username);
        EditText emailInput = getView().findViewById(R.id.email);
        EditText passwordInput = getView().findViewById(R.id.password);
        String username = usernameInput.getText().toString().trim();
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();

        if (validateInput(username, email, password)) {
            //TODO Call the backend service to register the user
        }

    }

    private boolean validateInput(String username, String email, String password) {
        if (username.isEmpty()) {
            Toast.makeText(getActivity(), "Please enter your username.", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (email.isEmpty()) {
            Toast.makeText(getActivity(), "Please enter your email address.", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!isValidEmail(email)) {
            Toast.makeText(getActivity(), "Please enter a valid email address.", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (password.isEmpty()) {
            Toast.makeText(getActivity(), "Please enter a password.", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!isValidPassword(password)) {
            Toast.makeText(getActivity(), "Password must be at least 8 characters long.", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Add additional validation rules if necessary

        return true;
    }

    private boolean isValidEmail(String email) {
        // Add your email validation logic here
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean isValidPassword(String password) {
        // Add your password validation logic here
        return !TextUtils.isEmpty(password) && password.length() >= 8;
    }
}
