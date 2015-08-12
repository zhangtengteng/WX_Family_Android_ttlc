package com.xweisoft.wx.family.util;

import java.io.File;
import java.util.Arrays;
import java.util.Comparator;

import com.xweisoft.wx.family.logic.global.GlobalConstant;

import android.text.TextUtils;
/**
 * 文件缓存管理类
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  gac
 * @version  [版本号, 2013-11-7]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class FileCacheManager {

	/**
	 * 文件缓存目录的大小
	 */
	private static final long MAX_SIZE = 30 * 1024 * 1024; // 30MB

	/**
	 * 获取图片的本地存储路径。
	 * 
	 * @param imageUrl
	 *            图片的URL地址。
	 * @return String 图片的本地存储路径。
	 */
	public static String getImagePath(String imageUrl) {
		String imageName = imageUrl.replaceAll("/", ".").replaceAll(":", "")
				.replace("?", ".");
		String imageDir = GlobalConstant.FILE_CACHE_DIR;
		Util.makeDirs(imageDir);
		String imagePath = imageDir + imageName;
		return imagePath;
	}

	/**
	 * 根据设置的最大缓存数清理不常用的缓存文件
	 * 
	 * @param path
	 *            清理的缓存文件夹目录
	 */
	public static void cleanDir(String path) {
		if (TextUtils.isEmpty(path)) {
			return;
		}
		File dir = new File(path);
		if (dir == null || dir.isFile()) {
			return;
		}
		long size = 0;
		File[] files = dir.listFiles();
		if (null == files)
		{
		    return;
		}
		for (File file : files) {
			if (file.isFile()) {
				size += file.length();
			}
		}
		if (size <= MAX_SIZE) {
			return;
		}
		if (files.length > 1) {
			Arrays.sort(files, new ComparatorByLastModifiedTime());
		}
		for (File file : files) {
			file.delete();
			if ((size -= file.length()) <= MAX_SIZE) {
				break;
			}
		}
	}

	/**
	 * 按照文件时间排序，旧文件排在最前面
	 */
	private static class ComparatorByLastModifiedTime implements
			Comparator<File> {

		@Override
		public int compare(File lhs, File rhs) {
			long diff = lhs.lastModified() - rhs.lastModified();

			return diff > 0 ? 1 : diff < 0 ? -1 : 0;
		}

	}
}
