package com.example.demo.model;

import java.util.List;

public record Track(String name, List<Talk> talks) {

	public record Talk(String time, String session, List<String> authors) {
	}

}
