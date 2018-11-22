package ca.pethappy.pethappy.android.utils.task;

import android.os.AsyncTask;

public class TaskWithProgress<Param, Progress, Result> extends AsyncTask<Param, Progress, Result> {
    private TaskExecutor<Param, Result> taskExecutor;
    private TaskProgress<Progress> taskProgress;
    private TaskResult<Result> taskResult;
    private TaskError taskError;
    private Throwable throwable = null;

    public TaskWithProgress(TaskExecutor<Param, Result> taskExecutor,
                            TaskProgress<Progress> taskProgress,
                            TaskResult<Result> taskResult,
                            TaskError taskError) {
        this.taskExecutor = taskExecutor;
        this.taskProgress = taskProgress;
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

    @Override
    protected void onProgressUpdate(Progress[] progress) {
        if (this.taskProgress != null) {
            this.taskProgress.onProgress(progress[0]);
        }
    }
}