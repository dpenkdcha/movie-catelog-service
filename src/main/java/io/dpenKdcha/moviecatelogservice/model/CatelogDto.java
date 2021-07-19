package io.dpenKdcha.moviecatelogservice.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CatelogDto {

	private String name;
	private String desc;
	private int rating;
}
