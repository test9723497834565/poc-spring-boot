package com.example.test1;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.example.test1.payload.RestFlat;
import com.example.test1.payload.RestNested;
import com.fasterxml.jackson.databind.ObjectMapper;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class Test1ApplicationTests {
	// It is often desirable to call setWebApplicationType(WebApplicationType.NONE) when using SpringApplication within a JUnit test.
	private static final Logger log = LoggerFactory.getLogger(Test1ApplicationTests.class);
	
	@Rule
	public JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation();
	
	@Autowired
	private WebTestClient webClient;
	
	@Autowired
	private WebApplicationContext context;
    
	private MockMvc mockMvc;
	
	@Before
	public void setUp() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context)
				.apply(documentationConfiguration(this.restDocumentation)) 
				.build();
	}

	@Test
	public void documentTest() throws Exception {
		this.mockMvc.perform(get("/").accept(MediaType.APPLICATION_JSON)) 
		.andExpect(status().isOk()) 
		.andDo(document("index"));
	}

	@Test
	public void jdbcTest() {
		EntityExchangeResult<String> res = this.webClient.get()
				.uri("/")
				.exchange()
				.expectStatus()
				.isOk()
				.expectBody(String.class)
				.returnResult();
		log.info(res.toString());
		
	}
	
	@Test
	public void flatJsonTest() {
		EntityExchangeResult<String> res = this.webClient
				.get()
				.uri("/flat")
				.exchange()
				.expectStatus()
				.isOk()
				.expectBody(String.class)
				.returnResult();
		log.info(res.toString());
		
	}
	
	private static byte[] convertObjectToJsonBytes(Object object) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        //mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return mapper.writeValueAsBytes(object);
    }
	
	@Test
	public void flatBiDirectionalJsonDocumentationTest() throws Exception {
		RestFlat r = new RestFlat("my post", 88, new Date());
		this.mockMvc.perform(
					post("/flat-bi-directional")
					.contentType(MediaType.APPLICATION_JSON_UTF8)
					.accept(MediaType.APPLICATION_JSON_UTF8)
					.content(new ObjectMapper().writeValueAsString(r))
					.header("PPAUTH", "TEST_HEADER")
				)
				.andExpect(status().isOk())
				.andDo(document("flat-bi-dir"));
	}
	
	@Test
	public void flatBiDirectionalJsonTest() throws Exception {
			
		EntityExchangeResult<RestFlat> res = this.webClient
				.post()
				.uri("/flat-bi-directional")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.accept(MediaType.APPLICATION_JSON_UTF8)
				.syncBody(new RestFlat("my post", 88, new Date()))
				.header("PPAUTH","test-header")
				.exchange()
				.expectStatus()
				.isOk()
				.expectBody(RestFlat.class)
				.returnResult();
		
		log.info(res.toString());
		
		RestFlat r = res.getResponseBody();
		
		log.info("Name> " + r.getName());
		log.info("Age>  " + r.getAge());
		log.info("Date> " + r.getDate());		
	}
	
	@Test
	public void nestedBiDirectionalJsonTest() {
		
		List<String> listString = Arrays.asList("request string 1", "request string 2");
		
		List<RestFlat> listFlat  = Arrays.asList(new RestFlat("my request flat 1", 106, new Date()),
				                                 new RestFlat("my request flat 2", 107, new Date()));
		
		List<RestNested> listRestNested  = new ArrayList<>();
		RestNested rn1 = new RestNested("name nested request 1", 82 ,new Date(), 
				                        Arrays.asList("request 1-1", "request 1-2"),
				                        Arrays.asList(new RestFlat("my request flat 1-1", 125, new Date()), 
				                        			  new RestFlat("my request flat 1-2", 126, new Date())),
				                        Arrays.asList(new RestNested("L2 nested request 1-",33, new Date(),
				                        							 Arrays.asList("request 1-1", "request 1-2"),
				                        							 Arrays.asList(new RestFlat("my request nested 1-1", 127, new Date()), 
				                        							               new RestFlat("my request nested 1-2", 128, new Date())),
				                        							 Arrays.asList(new RestNested("nested nested request 1-1",1234,new Date(), null, null, null),
				                        									       new RestNested("nested nested  request 1-2",1234,new Date(), null, null, null))))
				                        );
		
		RestNested rn2 = new RestNested("name nested request 2", 82 ,new Date(), 
                Arrays.asList("request 2-1", "request 2-2"),
                Arrays.asList(new RestFlat("my request flat 2-1", 125, new Date()), 
                			  new RestFlat("my request flat 2-2", 126, new Date())),
                Arrays.asList(new RestNested("L2 nested request 2-",33, new Date(),
                							 Arrays.asList("request 2-1", "request 2-2"),
                							 Arrays.asList(new RestFlat("my request nested 2-1", 127, new Date()), 
                							               new RestFlat("my request nested 2-2", 128, new Date())),
                							 Arrays.asList(new RestNested("nested nested request 2-1",1234,new Date(), null, null, null),
                									       new RestNested("nested nested  request 2-2",1234,new Date(), null, null, null))))
                );
		listRestNested.add(rn1);
		listRestNested.add(rn2);
		
		RestNested restNested = new RestNested("my post nested", 55, new Date(), listString, listFlat, listRestNested);
		
		
		
		EntityExchangeResult<RestNested> res = this.webClient
				.post()
				.uri("/nested-bi-directional")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.accept(MediaType.APPLICATION_JSON_UTF8)
				.syncBody(restNested)
				.header("PPAUTH","test")
				.exchange()
				.expectStatus()
				.isOk()
				.expectBody(RestNested.class)
				.returnResult();
		
		log.info(res.toString());
		
		RestNested r = res.getResponseBody();
		
		log.info("Name> " + r.getName());
		log.info("Age>  " + r.getAge());
		log.info("Date> " + r.getDate());

		
	}
	
	
	/*
	 {"menu": {
       "id": "file",
       "value": "File",
       "popup": {
         "menuitem": [
           {"value": "New", "onclick": "CreateNewDoc()"},
           {"value": "Open", "onclick": "OpenDoc()"},
           {"value": "Close", "onclick": "CloseDoc()"}
         ]
       }
     }}
	 */

}
