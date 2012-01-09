package net.ceg.bamboo;

import com.atlassian.bamboo.build.CustomPostBuildCompletedAction;
import com.atlassian.bamboo.build.logger.BuildLogger;
import com.atlassian.bamboo.task.TaskContext;
import com.atlassian.bamboo.task.TaskException;
import com.atlassian.bamboo.task.TaskResult;
import com.atlassian.bamboo.task.TaskResultBuilder;
import com.atlassian.bamboo.task.TaskType;
import com.atlassian.bamboo.v2.build.BuildContext;
import org.jetbrains.annotations.NotNull;

public class ZooKeeperPublisherTask implements CustomPostBuildCompletedAction
{

    @Override
    public void init(@NotNull BuildContext buildContext) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @NotNull
    @Override
    public BuildContext call() throws InterruptedException, Exception {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}