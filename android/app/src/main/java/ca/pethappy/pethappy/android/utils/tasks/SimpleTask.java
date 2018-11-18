package ca.pethappy.pethappy.android.utils.tasks;

import android.os.AsyncTask;

public class SimpleTask<Param, Result> extends AsyncTask<Param, Void, Result> {
    private TaskExecutor<Param, Result> taskExecutor;
    private TaskResult<Result> taskResult;
    private TaskError taskError;
    private Throwable throwable = null;

    public SimpleTask(TaskExecutor<Param, Result> taskExecutor,
                      TaskResult<Result> taskResult,
                      TaskError taskError) {
        this.taskExecutor = taskExecutor;
        this.taskResult = taskResult;
        this.taskError = taskError;
    }

    @Override
    protected Result doInBackground(Param[] params) {
        Result res = null;
        try {
            res = this.taskExecutor.onExecute(params[0]);
        } catch (Throwable throwable) {
            this.throwable = throwable;
        }
        return res;
    }

    @Override
    protected void onPostExecute(Result result) {
        // If we have an error
        if (this.throwable != null) {
            this.taskError.onError(this.throwable);
            return;
        }

        // All good
        this.taskResult.onResult(result);
    }
}