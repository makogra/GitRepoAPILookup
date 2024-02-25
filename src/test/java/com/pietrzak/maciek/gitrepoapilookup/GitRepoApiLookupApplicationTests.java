package com.pietrzak.maciek.gitrepoapilookup;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class GitRepoApiLookupApplicationTests {

	@Test
	void contextLoads() {
		SyntheticData syntheticData = new SyntheticData("makogra");
		syntheticData.printFile();
	}

}
