package ca.petsuppliesathome.android.task;

public interface TaskResult<Result> {
    void onResult(Result result);
}
