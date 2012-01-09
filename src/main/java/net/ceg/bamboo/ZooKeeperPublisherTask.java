package net.ceg.bamboo;

import com.atlassian.bamboo.build.CustomPostBuildCompletedAction;
import com.atlassian.bamboo.build.logger.BuildLogger;
import com.atlassian.bamboo.plan.artifact.ArtifactContext;
import com.atlassian.bamboo.plan.artifact.ArtifactDefinitionContext;
import com.atlassian.bamboo.task.TaskContext;
import com.atlassian.bamboo.task.TaskException;
import com.atlassian.bamboo.task.TaskResult;
import com.atlassian.bamboo.task.TaskResultBuilder;
import com.atlassian.bamboo.task.TaskType;
import com.atlassian.bamboo.v2.build.BuildContext;
import org.jetbrains.annotations.NotNull;

public class ZooKeeperPublisherTask implements TaskType
{
    @NotNull
    @Override
    public TaskResult execute(@NotNull TaskContext taskContext) throws TaskException {

        final BuildLogger logger = taskContext.getBuildLogger();

        final String zkPath = taskContext.getConfigurationMap().get("path");
        final String zkData = taskContext.getConfigurationMap().get("data");

        for(ArtifactDefinitionContext adc: taskContext.getBuildContext().getArtifactContext().getDefinitionContexts()) {
            logger.addBuildLogEntry("Publishing artifact " + adc.getName() + "  to " + zkPath + " with data " + zkData);
        }
        return TaskResultBuilder.create(taskContext).success().build();
    }
}