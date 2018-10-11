package ca.petsuppliesathome.android.task;

public interface TaskProgress<T> {
    void onProgress(T progress);
}
