package com.loorain.live.base;

/**
 * @desc MVP中Presenter的基础接口
 */

public interface BasePresenter {
	/**
	 * @description: Presenter的开始处理方法
	 */
	void start();

	/**
	 * @desc 处理一些销毁工作，会在界面结束时候调用
	 */
	void finish();
}
