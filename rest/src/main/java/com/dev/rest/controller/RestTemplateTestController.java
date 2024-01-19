package com.dev.rest.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dev.rest.common.client.SelfRestClient;
import com.dev.rest.domain.EventVO;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/rest")
public class RestTemplateTestController {

    private final SelfRestClient selfRestClient;
    
    public RestTemplateTestController(SelfRestClient selfRestClient) {
	this.selfRestClient = selfRestClient;
    }

    @GetMapping("/return-get-value/{number}")
    public ResponseEntity<EventVO> returnGetValue(@PathVariable("number") int number) {

	EventVO event = new EventVO();
	event.setName("GetTest");
	event.setNumber(number);
	return ResponseEntity.ok(event);
    }

    @PostMapping("/return-post-value")
    public ResponseEntity<EventVO> returnPostValue(@RequestBody EventVO event) {

	event.setWage(1000000);
	return ResponseEntity.ok(event);
    }

    @PostMapping("/call-get-value")
    public ResponseEntity<EventVO> callGetValue(@RequestBody EventVO event) {

	return selfRestClient.get("/api/rest/return-get-value/11", null, EventVO.class);
    }

    @PostMapping("/call-post-value")
    public ResponseEntity<EventVO> callPostValue(@RequestBody EventVO event) {

	return selfRestClient.post("/api/rest/return-post-value", event, EventVO.class);
    }

}
