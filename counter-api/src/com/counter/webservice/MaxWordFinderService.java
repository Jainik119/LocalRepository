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
@RequestMapping("/top")
public class MaxWordFinderService {

	CounterUtility counterService = new CounterUtility();

	@RequestMapping(value = "/{number}", method = RequestMethod.GET, headers = "Accept=application/json")
	public void getTop(@PathVariable int number, HttpServletResponse response, HttpServletRequest request)
			throws IOException {
		counterService.getWordbyCount(number, response, request);
	}
}
