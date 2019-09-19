package com.zos.generate.common;


import java.io.Serializable;
import java.util.List;

/**
 * 分页查询的返回类.
 */
public class Page<T> implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int rowCount = 0;

    private int pageCount = 0;

    private List<T> pageData;

    //public Page() {
    //    this.pageData = Lists.newArrayList();
    //}

    public int getRowCount() {
        return rowCount;
    }

    public void setRowCount(int rowCount) {
        this.rowCount = rowCount;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public List<T> getPageData() {
        return pageData;
    }

    public void setPageData(List<T> pageData) {
        this.pageData = pageData;
    }

    /**
     * 根据每页记录数计算页数.
     *
     * @param pageSize 每页记录数
     */
    public void generatePageCount(int pageSize) {

        if (rowCount == 0 || pageSize == 0) {
            pageCount = 0;
        }
        if ((rowCount % pageSize) > 0) {
            pageCount = (rowCount / pageSize) + 1;
        } else {
            pageCount = rowCount / pageSize;
        }
    }

}
