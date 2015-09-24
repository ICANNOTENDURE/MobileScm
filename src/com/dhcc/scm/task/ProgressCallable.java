package com.dhcc.scm.task;



public interface ProgressCallable<T> {
    public T call(final IProgressListener pProgressListener) throws Exception;
}
