package com.forezp;

import com.alibaba.fastjson.JSON;
import com.google.common.io.Files;
import org.databene.contiperf.PerfTest;
import org.databene.contiperf.junit.ContiPerfRule;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ServiceHiApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableAutoConfiguration
public class ServiceHiApplicationTests {

    private static final Logger logger = LoggerFactory.getLogger(ServiceHiApplicationTests.class);

	@Autowired
	private TestRestTemplate testRestTemplate;

	@Test
	public void contextLoads() {
	}

    @Rule
    public ContiPerfRule i = new ContiPerfRule();

//	GET请求测试
	@Test
    @PerfTest(invocations = 200000000, threads = 16)
	public void get() throws Exception {
		Map<String,String> multiValueMap = new HashMap<>();
		multiValueMap.put("username","lake");//传值，但要在url上配置相应的参数
		ActResult result = testRestTemplate.getForObject("/test/get?username={username}",ActResult.class,multiValueMap);
		logger.error("==========================="+JSON.toJSONString(result));
//		Assert.assertEquals(result.getCode(),0);
	}

	@Test
	public void get1() throws Exception {
		Map<String,String> multiValueMap = new HashMap<>();
		multiValueMap.put("username","lake");//传值，但要在url上配置相应的参数
		ActResult result = testRestTemplate.getForObject("/test/get/{username}",ActResult.class,multiValueMap);
		logger.error(JSON.toJSONString(result));
		Assert.assertEquals(result.getCode(),0);
	}

//	POST请求测试

	@Test
	public void post() throws Exception {
		MultiValueMap multiValueMap = new LinkedMultiValueMap();
		multiValueMap.add("username","lake");
		ActResult result = testRestTemplate.postForObject("/test/post",multiValueMap,ActResult.class);
		logger.error(JSON.toJSONString(result));
	}

//	file文件上传测试

	@Test
	public void upload() throws Exception {
//		MockMultipartFile file = new MockMultipartFile("file", "text.txt", "multipart/form-data", "hello upload".getBytes("UTF-8"));
		Resource resource = new FileSystemResource("F:\\1\\发行.txt");
		MultiValueMap multiValueMap = new LinkedMultiValueMap();
		multiValueMap.add("username","lake");
		multiValueMap.add("file",resource);
		ActResult result = testRestTemplate.postForObject("/test/upload",multiValueMap,ActResult.class);
		logger.error(JSON.toJSONString(result));
	}


	/**
	 * 文件上传
	 */
	@Test
	public void whenUploadSuccess() throws Exception {
//		String result = mockMvc.perform(MockMvcRequestBuilders.fileUpload("/file")  //就是post
//				.file(new MockMultipartFile("file", "text.txt", "multipart/form-data", "hello upload".getBytes("UTF-8"))))   //模拟文件上传
//				.andExpect(MockMvcResultMatchers.status().isOk())
//				.andReturn().getResponse().getContentAsString()
//				;
//		System.out.println("result=" + result);


	}


//	file文件下载测试
	@Test
	public void download() throws Exception {
		HttpHeaders headers = new HttpHeaders();
		headers.set("token","xxxxxx");
		HttpEntity formEntity = new HttpEntity(headers);
		String[] urlVariables = new String[]{"admin"};
		ResponseEntity<byte[]> response = testRestTemplate.exchange("/test/download?username={1}",HttpMethod.GET,formEntity,byte[].class,urlVariables);
		if (response.getStatusCode() == HttpStatus.OK) {
			Files.write(response.getBody(),new File("/home/lake/github/file/test.gradle"));

		}
	}

//	header请求头信息传输测试

	@Test
	public void getHeader() throws Exception {
		HttpHeaders headers = new HttpHeaders();
		headers.set("token","xxxxxx");
		HttpEntity formEntity = new HttpEntity(headers);
		String[] urlVariables = new String[]{"admin"};
		ResponseEntity<ActResult> result = testRestTemplate.exchange("/test/getHeader?username={username}", HttpMethod.GET,formEntity,ActResult.class,urlVariables);
		Assert.assertEquals(result.getBody().getCode(),0);
	}

//	PUT信息修改
	@Test
	public void putHeader() throws Exception {
		HttpHeaders headers = new HttpHeaders();
		headers.set("token","xxxxxx");
		MultiValueMap multiValueMap = new LinkedMultiValueMap();
		multiValueMap.add("username","lake");
		HttpEntity formEntity = new HttpEntity(multiValueMap,headers);
		ResponseEntity<ActResult> result = testRestTemplate.exchange("/test/putHeader", HttpMethod.PUT,formEntity,ActResult.class);
		Assert.assertEquals(result.getBody().getCode(),0);
	}

//	DELETE删除信息
	@Test
	public void delete() throws Exception {
		HttpHeaders headers = new HttpHeaders();
		headers.set("token","xxxxx");
		MultiValueMap multiValueMap = new LinkedMultiValueMap();
		multiValueMap.add("username","lake");
		HttpEntity formEntity = new HttpEntity(multiValueMap,headers);
		String[] urlVariables = new String[]{"admin"};
		ResponseEntity<ActResult> result = testRestTemplate.exchange("/test/delete?username={username}", HttpMethod.DELETE,formEntity,ActResult.class,urlVariables);
		Assert.assertEquals(result.getBody().getCode(),0);
	}
}
