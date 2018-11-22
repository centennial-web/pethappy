package ca.pethappy.pethappy.android.utils.task;

public interface TaskProgress<T> {
    void onProgress(T progress);
}