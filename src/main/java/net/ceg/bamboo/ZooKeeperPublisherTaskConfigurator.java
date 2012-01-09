package net.ceg.bamboo;

import com.atlassian.bamboo.collections.ActionParametersMap;
import com.atlassian.bamboo.task.AbstractTaskConfigurator;
import com.atlassian.bamboo.task.TaskDefinition;
import com.atlassian.bamboo.utils.error.ErrorCollection;
import com.opensymphony.xwork.TextProvider;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class ZooKeeperPublisherTaskConfigurator extends AbstractTaskConfigurator
{
    private TextProvider textProvider;

    @NotNull
    @Override
    public Map<String, String> generateTaskConfigMap(@NotNull final ActionParametersMap params, @Nullable final TaskDefinition previousTaskDefinition)
    {
        final Map<String, String> config = super.generateTaskConfigMap(params, previousTaskDefinition);
        config.put("path", params.getString("path"));
        return config;
    }

    @Override
    public void populateContextForCreate(@NotNull final Map<String, Object> context)
    {
        super.populateContextForCreate(context);

        context.put("path", "/foo/bar/release-");
        context.put("data", "app={} version={} environment={} cluster={} datacentre={}");
    }

    @Override
    public void populateContextForEdit(@NotNull final Map<String, Object> context, @NotNull final TaskDefinition taskDefinition)
    {
        super.populateContextForEdit(context, taskDefinition);

        context.put("path", taskDefinition.getConfiguration().get("path"));
        context.put("data", taskDefinition.getConfiguration().get("data"));
    }

    @Override
    public void populateContextForView(@NotNull final Map<String, Object> context, @NotNull final TaskDefinition taskDefinition)
    {
        super.populateContextForView(context, taskDefinition);
        context.put("path", taskDefinition.getConfiguration().get("path"));
        context.put("data", taskDefinition.getConfiguration().get("data"));
    }

    @Override
    public void validate(@NotNull final ActionParametersMap params, @NotNull final ErrorCollection errorCollection)
    {
        super.validate(params, errorCollection);

        final String pathValue = params.getString("path");
        if (StringUtils.isEmpty(pathValue))
        {
            errorCollection.addError("path", textProvider.getText("net.ceg.bamboo.config.path.error"));
        }
        final String dataValue = params.getString("data");
        if (StringUtils.isEmpty(dataValue))
        {
            errorCollection.addError("data", textProvider.getText("net.ceg.bamboo.config.data.error"));
        }
    }

    public void setTextProvider(final TextProvider textProvider)
    {
        this.textProvider = textProvider;
    }
}
