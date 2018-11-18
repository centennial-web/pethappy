package ca.pethappy.pethappy.android.utils.tasks;

public interface TaskExecutor<Param, Result> {
    Result onExecute(Param param) throws Throwable;
}