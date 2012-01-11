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
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class ZooKeeperPublisherTaskConfigurator extends AbstractTaskConfigurator
{
    private TextProvider textProvider;

    @NotNull
    @Override
    public Map<String, String> generateTaskConfigMap(@NotNull final ActionParametersMap params, @Nullable final TaskDefinition previousTaskDefinition)
    {
        final Map<String, String> config = super.generateTaskConfigMap(params, previousTaskDefinition);
        config.put("connection", params.getString("connection"));
        config.put("artifact", params.getString("artifact"));
        config.put("artifactRegex", params.getString("artifactRegex"));
        config.put("path", params.getString("path"));
        config.put("data", params.getString("data"));
        return config;
    }

    @Override
    public void populateContextForCreate(@NotNull final Map<String, Object> context)
    {
        super.populateContextForCreate(context);
        context.put("connection", "");
        context.put("artifact", true);
        context.put("artifactRegex", "*.rpm");
        context.put("path", "/foo/bar/release-");
        context.put("data", "app={} version={} environment={} cluster={} datacentre={}");
    }

    @Override
    public void populateContextForEdit(@NotNull final Map<String, Object> context, @NotNull final TaskDefinition taskDefinition)
    {
        super.populateContextForEdit(context, taskDefinition);

        context.put("connection", taskDefinition.getConfiguration().get("connection"));
        context.put("artifact", taskDefinition.getConfiguration().get("artifact"));
        context.put("artifactRegex", taskDefinition.getConfiguration().get("artifactRegex"));
        context.put("path", taskDefinition.getConfiguration().get("path"));
        context.put("data", taskDefinition.getConfiguration().get("data"));
    }

    @Override
    public void populateContextForView(@NotNull final Map<String, Object> context, @NotNull final TaskDefinition taskDefinition)
    {
        super.populateContextForView(context, taskDefinition);
        context.put("connection", taskDefinition.getConfiguration().get("connection"));
        context.put("artifact", taskDefinition.getConfiguration().get("artifact"));
        context.put("artifactRegex", taskDefinition.getConfiguration().get("artifactRegex"));
        context.put("path", taskDefinition.getConfiguration().get("path"));
        context.put("data", taskDefinition.getConfiguration().get("data"));
    }

    @Override
    public void validate(@NotNull final ActionParametersMap params, @NotNull final ErrorCollection errorCollection)
    {
        super.validate(params, errorCollection);

        final String connectionValue = params.getString("connection");
        if (StringUtils.isEmpty(connectionValue))
        {
            errorCollection.addError("connection", textProvider.getText("net.ceg.bamboo.config.connection.error"));
        }

        final Boolean artifactValue = params.getBoolean("artifact");
        final String artifactRegex = params.getString("artifactRegex");
        if (artifactValue)
        {
            if(StringUtils.isEmpty(artifactRegex) || !isValidRegex(artifactRegex)) {
                errorCollection.addError("artifactRegex", textProvider.getText("net.ceg.bamboo.config.artifactRegex.error"));
            }
        }

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

    private boolean isValidRegex(String regex) {
        try {
            Pattern.compile(regex);
            return true;
        } catch (PatternSyntaxException pse) { }
        return false;
    }

    public void setTextProvider(final TextProvider textProvider)
    {
        this.textProvider = textProvider;
    }
}
