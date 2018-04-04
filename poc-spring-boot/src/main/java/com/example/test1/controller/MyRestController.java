package com.example.test1.controller;

import org.springframework.web.bind.annotation.RestController;

import com.example.test1.payload.RestFlat;
import com.example.test1.payload.RestNested;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;


@RestController
public class MyRestController {
	
	private static final Logger log = LoggerFactory.getLogger(MyRestController.class);
	
	private final SimpleJdbcCall call;

	@Autowired
	public MyRestController(JdbcTemplate jdbcTemplate) {
		this.call = new SimpleJdbcCall(jdbcTemplate).withProcedureName("read_actor");
	}
	
	/*
	 create or replace procedure read_actor (
       in_id          in  number
      ,in2_id         in  number
      ,out_first_name out varchar2
      ,out_last_name  out varchar2
      ,out_birth_date out date)
     is
     begin
       out_first_name := 'test test test = ' || in_id;
       out_last_name := 'test2 test 2 test2= ' || in2_id;
       out_birth_date := sysdate;
     end;
	 */
	@GetMapping("/")
	String home() {
		SqlParameterSource in = new MapSqlParameterSource()
                				.addValue("IN_ID", 1)
                				.addValue("IN2_ID", 3);
        Map<String, Object> out = this.call.execute(in);
        String v1 = (String) out.get("OUT_FIRST_NAME");
        String v2 = (String) out.get("OUT_LAST_NAME");
        Date v3 = (Date) out.get("OUT_BIRTH_DATE");
		
		return "Hello World! <br>" + v1 + "<br>" + v2 + "<br>" + v3;
	}
	
	@GetMapping("/flat")
	RestFlat flat() {		
		return new RestFlat("my name", 99, new Date());
	}
	
	@PostMapping("/flat-bi-directional")
	RestFlat flatBiDirectional(@RequestBody RestFlat request, @RequestHeader HttpHeaders headers) {		
		
		String header = headers.getFirst("PPAUTH");
		
		log.info("PPAUTH = " + header);
		
		return new RestFlat(request.getName()+" response", request.getAge()+100,
				new Date(request.getDate().getTime()+1000*60*60*24*36)
				);
	}

	@PostMapping("/nested-bi-directional")
	RestNested nestedBiDirectional(@RequestBody RestNested request) {	
		List<String> listString = request.getListString();
		listString.add("response string");
		
		List<RestFlat> listFlat  = request.getListFlat();
		listFlat.add(new RestFlat("my response flat", 125, new Date()));
		
		List<RestNested> listRestNested  = request.getListRestNested();
		RestNested rn = new RestNested("name nested response", 87,new Date(), 
				                        Arrays.asList("response 1", "response 2"),
				                        Arrays.asList(new RestFlat("my response flat 1", 125, new Date()), 
				                        			  new RestFlat("my response flat 2", 126, new Date())),
				                        Arrays.asList(new RestNested("L2 nested response",33, new Date(),
				                        							 Arrays.asList("response 1", "response 2"),
				                        							 Arrays.asList(new RestFlat("my response nested 1", 127, new Date()), 
				                        							               new RestFlat("my response nested 2", 128, new Date())),
				                        							 Arrays.asList(new RestNested("nested nested 1",1234,new Date(), null, null, null),
				                        									       new RestNested("nested nested 2",1234,new Date(), null, null, null))))
				                        );
				
		listRestNested.add(rn);
		
		return new RestNested(request.getName()+" response nested", request.getAge()+77,
				new Date(request.getDate().getTime()+1000*60*60*24*36),
				listString,
				listFlat,
				listRestNested
				);
	}
}
