package com.counter.webservice;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.counter.utility.CounterUtility;

@RestController
@RequestMapping("/search")
public class SearchWordService {
	
	CounterUtility counterService= new CounterUtility();
	
	@RequestMapping(value ="/{word}", method = RequestMethod.POST,headers="Accept=application/json")
	public String searchWord(@PathVariable String word,HttpServletResponse response,HttpServletRequest request) throws IOException {
		String counter = counterService.getWordInfoJson(word, response, request);
		return counter;
	}
	
}
