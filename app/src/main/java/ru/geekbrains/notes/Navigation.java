package ru.geekbrains.notes;

import android.content.res.Resources;
import android.view.SurfaceControl;
import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class Navigation {

    private final FragmentManager fragmentManager;
    private final Resources resources;

    public Navigation(FragmentManager fragmentManager, Resources resources) {
        this.fragmentManager = fragmentManager;
        this.resources = resources;
    }

    public void addFragment(Fragment fragment, int containerId, boolean addToBackStack) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(containerId, fragment);
        if (addToBackStack) {
            fragmentTransaction.addToBackStack(null);
        }
        fragmentTransaction.commit();
    }

    public void addFragment(Fragment fragment, boolean addToBackStack) {
        boolean isLandscape = resources.getBoolean(R.bool.isLandscape);

        FragmentTransaction transaction = fragmentManager.beginTransaction();

        if (!isLandscape) {
            transaction.replace(R.id.fragment_container, fragment);
            if (addToBackStack) {
                transaction.addToBackStack(null);
            }
            transaction.commit();

        } else {
            transaction.replace(R.id.detail_container, fragment);
            if (addToBackStack) {
                transaction.addToBackStack(null);
            }
            transaction.commit();
        }
    }

    public void addFragment(Fragment fragment) {
        addFragment(fragment, true);
    }

}
