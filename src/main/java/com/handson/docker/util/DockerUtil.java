package com.handson.docker.util;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.PullImageResultCallback;
import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.Ports;
import com.github.dockerjava.core.DockerClientBuilder;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.IOException;

@Service
public class DockerUtil {
    @Resource
    private Environment environment;
    DockerClient docker;

    @PostConstruct
    public void setupDockerClient() throws IOException {
        docker = DockerClientBuilder.getInstance().build();
        try {
            docker.pullImageCmd("elasticsearch:7.9.3")
                    .exec(new PullImageResultCallback()).awaitCompletion();
        } catch (InterruptedException e) {
            throw new IllegalStateException("timeout");
        }
    }

    public String startContainer(){
        ExposedPort tcp9200 = ExposedPort.tcp(9200);
        ExposedPort tcp9300 = ExposedPort.tcp(9300);
        Ports portBindings = new Ports();
        portBindings.bind(tcp9200, new Ports.Binding(environment.getProperty("elasticsearch.host"), "9200"));
        portBindings.bind(tcp9300, new Ports.Binding(environment.getProperty("elasticsearch.host"), "9300"));
        CreateContainerResponse containerResponse = docker
                .createContainerCmd("elasticsearch:7.9.3")
                .withEnv("discovery.type=single-node")
                .withExposedPorts(tcp9200, tcp9300)
                .withPortBindings(portBindings)
                .exec();
        String esContainerId = containerResponse.getId();
        docker.startContainerCmd(esContainerId)
                .exec();
        return esContainerId;
    }
    public void stopDockerContainer(String esContainerId) {
        docker.stopContainerCmd(esContainerId).exec();
        docker.removeContainerCmd(esContainerId).exec();
    }
}
