package ca.pethappy.pethappy.android.utils.tasks;

public interface TaskProgress<T> {
    void onProgress(T progress);
}