	package org.iitbact.cc.response;

import java.util.ArrayList;
import java.util.List;

import org.iitbact.cc.beans.BaseBean;


public class ListResponse<T> implements BaseBean {
	private List<T> list=new ArrayList<>();
	private int pageNo;
	private boolean hasMore;

	public List<T> getList() {
		return list;
	}

	public void setList(List<T> list) {
		this.list = list;
	}

	public int getPageNo() {
		return pageNo;
	}

	public boolean getHasMore() {
		return hasMore;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	public void setHasMore(boolean hasMore) {
		this.hasMore = hasMore;
	}
	
	
}
