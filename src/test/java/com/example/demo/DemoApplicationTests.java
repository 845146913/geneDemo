package com.example.demo;

import com.example.demo.dto.DemoDTO;
import com.example.demo.model.Demo;
import com.example.demo.service.IDemoService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DemoApplicationTests {

	@Resource
	IDemoService demoService;
	@Test
	public void contextLoads() {
		List<Demo> enties = new ArrayList<>();
		enties.add(new Demo(1, "aaa", "aaa"));
		enties.add(new Demo(2, "bbb", "bbb"));
//		demoService.batchSave(enties);
		List<Demo> all = demoService.findAll();
		System.out.println("result: " + all);
		DemoDTO dto = new DemoDTO();
		dto.setId(3);
		dto.setName("cccc");
		DemoDTO res = demoService.create(dto);
		System.out.println("dto: " + res);

		String entityName = demoService.getEntityName();
		System.out.println("entityName: " + entityName);

	}

}
