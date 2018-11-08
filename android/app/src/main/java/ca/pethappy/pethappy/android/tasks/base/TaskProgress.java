package ca.pethappy.pethappy.android.tasks.base;

public interface TaskProgress<T> {
    void onProgress(T progress);
}