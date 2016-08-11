package com.android.util;

/**
 * 需要异步网络操作的时候，异步操作的组建要实现本接口。
 * @author robin@apexera.com
 */
public interface WebRequestHandler {
	public void handleResponse(String type, byte[] buffer);
}
