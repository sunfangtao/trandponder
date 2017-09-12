package com.aioute.util;

import javax.servlet.http.HttpServletRequest;

public class PagingUtil {

	//private static Logger logger = Logger.getLogger(PagingUtil.class);

	public static int getCount(HttpServletRequest req) {
		String count = req.getParameter("count");
		int numCount = -1;
		try {
			numCount = Integer.parseInt(count);
			if (numCount <= 0) {
				numCount = 0;
			}
		} catch (Exception e) {
			//logger.info("没有count参数或格式错误");
		}
		return numCount;
	}

	public static int getPage(HttpServletRequest req) {
		String page = req.getParameter("page");
		int numPage = 1;
		try {
			numPage = Integer.parseInt(page);
			if (numPage <= 1) {
				numPage = 1;
			}
		} catch (Exception e) {
			//logger.info("没有page参数或格式错误");
		}
		return numPage;
	}

	public static int getPageSize(HttpServletRequest req) {
		String pageSize = req.getParameter("pageSize");
		int numPageSize = 10;
		try {
			numPageSize = Integer.parseInt(pageSize);
			if (numPageSize <= 0) {
				numPageSize = 10;
			}
		} catch (Exception e) {
			//logger.info("没有pageSize参数或格式错误");
		}
		return numPageSize;
	}
}
