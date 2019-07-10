package com.haita.precise;

import com.haita.precise.resource.GitDiffResource;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PreciseApplicationTests {
	@Autowired
	GitDiffResource resource;

	@Test
	public void contextLoads() {
		resource.diffTag("dev_20190911_01", "test");
	}

}
