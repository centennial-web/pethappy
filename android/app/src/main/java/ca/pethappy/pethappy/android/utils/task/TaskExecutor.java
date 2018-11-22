package ca.pethappy.pethappy.android.utils.task;

public interface TaskExecutor<Param, Result> {
    Result onExecute(Param param) throws Throwable;
}