package com.handson.docker.controller;


import com.handson.docker.util.DockerUtil;
import com.handson.docker.util.ElasticsearchUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;


@RestController
@RequestMapping(value = "")
public class AppController {
    @Autowired
    ElasticsearchUtil elasticsearchUtil;
    @Autowired
    DockerUtil dockerService;

    @RequestMapping(value = "/putIndex", method = RequestMethod.GET)
    public String putIndex(@RequestParam String indexId) throws IOException {
        elasticsearchUtil.crateIndex(indexId);
        return "index added";
    }
    @RequestMapping(value = "/deleteIndex", method = RequestMethod.GET)
    public String deleteIndex(@RequestParam String indexId) throws IOException {
        elasticsearchUtil.deleteIndex(indexId);
        return "index deleted";
    }
    @RequestMapping(value = "/start", method = RequestMethod.GET)
    public String startES() throws IOException {
        return dockerService.startContainer();
    }
    @RequestMapping(value = "/stop", method = RequestMethod.GET)
    public String stopES(@RequestParam String esContainerId) throws IOException {
        dockerService.stopDockerContainer(esContainerId);
        return "ok";
    }
}