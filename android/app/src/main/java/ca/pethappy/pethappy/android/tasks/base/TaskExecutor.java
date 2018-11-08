package ca.pethappy.pethappy.android.tasks.base;

public interface TaskExecutor<Param, Result> {
    Result onExecute(Param param) throws Throwable;
}