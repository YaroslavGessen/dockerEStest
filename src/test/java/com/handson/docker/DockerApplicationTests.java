package com.handson.docker;

import com.handson.docker.util.DockerUtil;
import com.handson.docker.util.ElasticsearchUtil;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.io.IOException;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(classes = DockerApplication.class)
@ActiveProfiles({"test"})
@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
class DockerApplicationTests {
	@Autowired
	DockerUtil dockerController;
	@Autowired
	ElasticsearchUtil elasticsearchUtil;
	String esContainerId;

	@Test
	void indexTest1() throws Exception{
		assertTrue(elasticsearchUtil.crateIndex("0000"));
		assertTrue(elasticsearchUtil.deleteIndex("0000"));
	}

	@Test
	void indexTest2() throws Exception{
		assertTrue(elasticsearchUtil.crateIndex("index9999"));
		assertTrue(elasticsearchUtil.deleteIndex("index9999"));
	}
	@Test
	void indexTest3() throws Exception{
		assertFalse(elasticsearchUtil.crateIndex("asd*12"));
		assertFalse(elasticsearchUtil.crateIndex("asd*12"));
	}

	@BeforeAll
	void startDocker() throws IOException, InterruptedException {
		esContainerId = dockerController.startContainer();
		Thread.sleep(20000);
	}
	@AfterAll
	void stopAndRemoveDockerContainer() {
		dockerController.stopDockerContainer(esContainerId);
	}
}
