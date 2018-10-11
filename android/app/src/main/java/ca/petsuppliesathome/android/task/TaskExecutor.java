package ca.petsuppliesathome.android.task;

public interface TaskExecutor<Param, Result> {
    Result onExecute(Param param) throws Throwable;
}
