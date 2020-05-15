package org.iitbact.cc.beans;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class Page<T> {
	private List<T> elements = new ArrayList<>();
	private PageInfo meta;
}
