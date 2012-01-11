package net.ceg.bamboo;

import com.atlassian.bamboo.build.logger.BuildLogger;
import com.atlassian.bamboo.plan.artifact.ArtifactSubscriptionContext;
import com.atlassian.bamboo.task.*;
import org.apache.zookeeper.ZooKeeper;
import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class ZooKeeperPublisherTask implements TaskType {
    @NotNull
    @Override
    public TaskResult execute(@NotNull TaskContext taskContext) throws TaskException {

        final BuildLogger logger = taskContext.getBuildLogger();

        logger.addBuildLogEntry("Executing ZooKeeperPublisherTask");

        final String connection = taskContext.getConfigurationMap().get("connection");

        try {
            ZooKeeper zk = new ZooKeeper(connection, 3000, null);
            logger.addBuildLogEntry("Zookeeper Connected to " + connection);
        } catch (IOException ioe) {
            logger.addBuildLogEntry(ioe.getMessage());
//            oh no!
        }

        final String zkPath = taskContext.getConfigurationMap().get("path");
        final String zkData = taskContext.getConfigurationMap().get("data");

        for (ArtifactSubscriptionContext asc : taskContext.getBuildContext().getArtifactContext().getSubscriptionContexts()) {
            try {
                String artifact = null;
                String version = null;

                XPath xpath = XPathFactory.newInstance().newXPath();
                NodeList nodes = ((Node) xpath.evaluate("/", new InputSource(new FileInputStream(new File(asc.getEffectiveDestinationPath()))), XPathConstants.NODE)).getFirstChild().getChildNodes();
                for (int i = 0; i < nodes.getLength(); i++) {
                    Node n = nodes.item(i);
                    if (n.getNodeName().equalsIgnoreCase("artifactId")) {
                        artifact = n.getFirstChild().getTextContent();
                    }
                    if (n.getNodeName().equalsIgnoreCase("version")) {
                        version = n.getFirstChild().getTextContent();
                    }
                }
                logger.addBuildLogEntry("Artifact name: " + artifact);
                logger.addBuildLogEntry("Artifact version: " + version);
                logger.addBuildLogEntry("Publishing subscription artifact " + asc.getEffectiveDestinationPath() + "  to " + zkPath + " with data " + zkData);
            } catch (IOException pe) {
                logger.addErrorLogEntry("Error reading " + asc.getEffectiveDestinationPath());
            } catch (XPathExpressionException e) {
                logger.addErrorLogEntry("Error xpathing " + asc.getEffectiveDestinationPath());
                e.printStackTrace();
            }
        }

        return TaskResultBuilder.create(taskContext).success().build();
    }
}