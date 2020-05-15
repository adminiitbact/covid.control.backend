package org.iitbact.cc.response;

import org.iitbact.cc.beans.BaseBean;
import org.iitbact.cc.beans.Page;

import lombok.Data;

@Data
public class ListResponseType2<T> implements BaseBean<Page<T>> {
    private Page<T> page;
	@Override
	public void setEntity(Page<T> entity) {
		this.page=entity;
	}
}
