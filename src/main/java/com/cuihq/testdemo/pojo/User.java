package com.cuihq.testdemo.pojo;

import com.cuihq.testdemo.annotation.CommentTarget;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User extends BaseEntity{
	@CommentTarget("自定义id")
	private Long id;
	@CommentTarget("自定义name")
	private String name;

}
