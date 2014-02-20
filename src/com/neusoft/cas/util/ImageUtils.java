package com.neusoft.cas.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Environment;
import android.util.Base64;

public class ImageUtils {

	/** 默认下载文件地址. */
	private static String downPathRootDir = File.separator + "download"
			+ File.separator;
	/** 默认下载图片文件地址. */
	private static String downPathImageDir = downPathRootDir + "cache_images"
			+ File.separator;

	/** 图片处理：裁剪. */
	public static final int CUTIMG = 0;

	/** 图片处理：缩放. */
	public static final int SCALEIMG = 1;

	/** 图片处理：不处理. */
	public static final int ORIGINALIMG = 2;

	/**
	 * 描述：获取默认的图片保存全路径.
	 * 
	 * @return 完成的存储目录
	 */
	public static String getFullImageDownPathDir() {
		String pathDir = null;
		try {
			if (!isCanUseSD()) {
				return null;
			}
			// 初始化图片保存路径
			File fileRoot = Environment.getExternalStorageDirectory();
			File dirFile = new File(fileRoot.getAbsolutePath()
					+ ImageUtils.downPathImageDir);
			if (!dirFile.exists()) {
				dirFile.mkdirs();
			}
			pathDir = dirFile.getPath();
		} catch (Exception e) {
		}
		return pathDir;
	}

	/**
	 * 描述：SD卡是否能用.
	 * 
	 * @return true 可用,false不可用
	 */
	public static boolean isCanUseSD() {
		try {
			return Environment.getExternalStorageState().equals(
					Environment.MEDIA_MOUNTED);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 描述：通过文件的本地地址从SD卡读取图片.
	 * 
	 * @param file
	 *            the file
	 * @param type
	 *            图片的处理类型（剪切或者缩放到指定大小，参考AbConstant类） 如果设置为原图，则后边参数无效，得到原图
	 * @param newWidth
	 *            新图片的宽
	 * @param newHeight
	 *            新图片的高
	 * @return Bitmap 新图片
	 */
	public static Bitmap getBitmapFromSD(File file, int type, int newWidth,
			int newHeight) {
		Bitmap bit = null;
		try {
			// SD卡是否存在
			if (!isCanUseSD()) {
				return null;
			}

			if (type != ORIGINALIMG && (newWidth <= 0 || newHeight <= 0)) {
				throw new IllegalArgumentException("缩放和裁剪图片的宽高设置不能小于0");
			}

			// 文件是否存在
			if (!file.exists()) {
				return null;
			}

			// 文件存在
			if (type == CUTIMG) {
				bit = cutImg(file, newWidth, newHeight);
			} else if (type == SCALEIMG) {
				bit = scaleImg(file, newWidth, newHeight);
			} else {
				bit = originalImg(file);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bit;
	}

	/**
	 * 描述：裁剪图片.
	 * 
	 * @param file
	 *            File对象
	 * @param newWidth
	 *            新图片的宽
	 * @param newHeight
	 *            新图片的高
	 * @return Bitmap 新图片
	 */
	public static Bitmap cutImg(File file, int newWidth, int newHeight) {
		Bitmap resizeBmp = null;
		if (newWidth <= 0 || newHeight <= 0) {
			throw new IllegalArgumentException("裁剪图片的宽高设置不能小于0");
		}

		BitmapFactory.Options opts = new BitmapFactory.Options();
		// 设置为true,decodeFile先不创建内存 只获取一些解码边界信息即图片大小信息
		opts.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(file.getPath(), opts);
		// inSampleSize=2表示图片宽高都为原来的二分之一，即图片为原来的四分之一
		// 缩放可以将像素点打薄,裁剪前将图片缩放到目标图2倍大小
		int srcWidth = opts.outWidth; // 获取图片的原始宽度
		int srcHeight = opts.outHeight;// 获取图片原始高度
		int destWidth = 0;
		int destHeight = 0;

		int cutSrcWidth = newWidth * 2;
		int cutSrcHeight = newHeight * 2;

		// 缩放的比例,为了大图的缩小到2倍被裁剪的大小在裁剪
		double ratio = 0.0;
		// 任意一个不够长就不缩放
		if (srcWidth < cutSrcWidth || srcHeight < cutSrcHeight) {
			ratio = 0.0;
			destWidth = srcWidth;
			destHeight = srcHeight;
		} else if (srcWidth > cutSrcWidth) {
			ratio = (double) srcWidth / cutSrcWidth;
			destWidth = cutSrcWidth;
			destHeight = (int) (srcHeight / ratio);
		} else if (srcHeight > cutSrcHeight) {
			ratio = (double) srcHeight / cutSrcHeight;
			destHeight = cutSrcHeight;
			destWidth = (int) (srcWidth / ratio);
		}

		// 默认为ARGB_8888.
		opts.inPreferredConfig = Bitmap.Config.RGB_565;
		// 以下两个字段需一起使用：
		// 产生的位图将得到像素空间，如果系统gc，那么将被清空。当像素再次被访问，如果Bitmap已经decode，那么将被自动重新解码
		opts.inPurgeable = true;
		// 位图可以共享一个参考输入数据(inputstream、阵列等)
		opts.inInputShareable = true;
		// 缩放的比例，缩放是很难按准备的比例进行缩放的，通过inSampleSize来进行缩放，其值表明缩放的倍数，SDK中建议其值是2的指数值
		if (ratio > 1) {
			opts.inSampleSize = (int) ratio;
		} else {
			opts.inSampleSize = 1;
		}
		// 设置大小
		opts.outHeight = destHeight;
		opts.outWidth = destWidth;
		// 创建内存
		opts.inJustDecodeBounds = false;
		// 使图片不抖动
		opts.inDither = false;
		Bitmap bitmap = BitmapFactory.decodeFile(file.getPath(), opts);
		if (bitmap != null) {
			resizeBmp = cutImg(bitmap, newWidth, newHeight);
		}
		return resizeBmp;
	}

	/**
	 * 描述：裁剪图片.
	 * 
	 * @param bitmap
	 *            the bitmap
	 * @param newWidth
	 *            新图片的宽
	 * @param newHeight
	 *            新图片的高
	 * @return Bitmap 新图片
	 */
	public static Bitmap cutImg(Bitmap bitmap, int newWidth, int newHeight) {
		if (bitmap == null) {
			return null;
		}

		if (newWidth <= 0 || newHeight <= 0) {
			throw new IllegalArgumentException("裁剪图片的宽高设置不能小于0");
		}

		Bitmap resizeBmp = null;

		try {
			int width = bitmap.getWidth();
			int height = bitmap.getHeight();

			if (width <= 0 || height <= 0) {
				return null;
			}
			int offsetX = 0;
			int offsetY = 0;

			if (width > newWidth) {
				offsetX = (width - newWidth) / 2;
			} else {
				newWidth = width;
			}

			if (height > newHeight) {
				offsetY = (height - newHeight) / 2;
			} else {
				newHeight = height;
			}

			resizeBmp = Bitmap.createBitmap(bitmap, offsetX, offsetY, newWidth,
					newHeight);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (resizeBmp != bitmap) {
				bitmap.recycle();
			}
		}
		return resizeBmp;
	}

	/**
	 * 描述：缩放图片.压缩
	 * 
	 * @param file
	 *            File对象
	 * @param newWidth
	 *            新图片的宽
	 * @param newHeight
	 *            新图片的高
	 * @return Bitmap 新图片
	 */
	public static Bitmap scaleImg(File file, int newWidth, int newHeight) {
		Bitmap resizeBmp = null;
		if (newWidth <= 0 || newHeight <= 0) {
			throw new IllegalArgumentException("缩放图片的宽高设置不能小于0");
		}
		BitmapFactory.Options opts = new BitmapFactory.Options();
		// 设置为true,decodeFile先不创建内存 只获取一些解码边界信息即图片大小信息
		opts.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(file.getPath(), opts);
		// inSampleSize=2表示图片宽高都为原来的二分之一，即图片为原来的四分之一
		// 缩放可以将像素点打薄
		// 获取图片的原始宽度高度
		int srcWidth = opts.outWidth;
		int srcHeight = opts.outHeight;

		int destWidth = srcWidth;
		int destHeight = srcHeight;

		// 缩放的比例
		float scale = 0;
		// 计算缩放比例
		float scaleWidth = (float) newWidth / srcWidth;
		float scaleHeight = (float) newHeight / srcHeight;
		if (scaleWidth > scaleHeight) {
			scale = scaleWidth;
		} else {
			scale = scaleHeight;
		}
		if (scale != 0) {
			destWidth = (int) (destWidth / scale);
			destHeight = (int) (destHeight / scale);
		}

		// 默认为ARGB_8888.
		opts.inPreferredConfig = Bitmap.Config.RGB_565;
		// 以下两个字段需一起使用：
		// 产生的位图将得到像素空间，如果系统gc，那么将被清空。当像素再次被访问，如果Bitmap已经decode，那么将被自动重新解码
		opts.inPurgeable = true;
		// 位图可以共享一个参考输入数据(inputstream、阵列等)
		opts.inInputShareable = true;

		// 缩放的比例，缩放是很难按准备的比例进行缩放的，通过inSampleSize来进行缩放，其值表明缩放的倍数，SDK中建议其值是2的指数值
		if (scale > 1) {
			// 缩小
			opts.inSampleSize = (int) scale;
		} else {
			// 放大
			opts.inSampleSize = 1;
		}

		// 设置大小
		opts.outHeight = destHeight;
		opts.outWidth = destWidth;
		// 创建内存
		opts.inJustDecodeBounds = false;
		// 使图片不抖动
		opts.inDither = false;
		// if(D) Log.d(TAG, "将缩放图片:"+file.getPath());
		resizeBmp = BitmapFactory.decodeFile(file.getPath(), opts);
		// 缩小或者放大
		if (resizeBmp != null && scale != 1) {
			resizeBmp = scaleImg(resizeBmp, scale);
		}
		// if(D) Log.d(TAG, "缩放图片结果:"+resizeBmp);
		return resizeBmp;
	}

	/**
	 * 描述：缩放图片,不压缩的缩放.
	 * 
	 * @param bitmap
	 *            the bitmap
	 * @param newWidth
	 *            新图片的宽
	 * @param newHeight
	 *            新图片的高
	 * @return Bitmap 新图片
	 */
	public static Bitmap scaleImg(Bitmap bitmap, int newWidth, int newHeight) {

		Bitmap resizeBmp = null;
		if (bitmap == null) {
			return null;
		}
		if (newWidth <= 0 || newHeight <= 0) {
			throw new IllegalArgumentException("缩放图片的宽高设置不能小于0");
		}
		// 获得图片的宽高
		int srcWidth = bitmap.getWidth();
		int srcHeight = bitmap.getHeight();

		if (srcWidth <= 0 || srcHeight <= 0) {
			return null;
		}
		// 缩放的比例
		float scale = 0;
		// 计算缩放比例
		float scaleWidth = (float) newWidth / srcWidth;
		float scaleHeight = (float) newHeight / srcHeight;
		if (scaleWidth > scaleHeight) {
			scale = scaleWidth;
		} else {
			scale = scaleHeight;
		}
		// 缩小或者放大
		if (bitmap != null && scale != 1) {
			resizeBmp = scaleImg(bitmap, scale);
		}
		return resizeBmp;
	}

	/**
	 * 描述：获取原图
	 * 
	 * @param file
	 *            File对象
	 * @return Bitmap 图片
	 */
	public static Bitmap originalImg(File file) {
		Bitmap resizeBmp = null;
		try {
			resizeBmp = BitmapFactory.decodeFile(file.getPath());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resizeBmp;
	}

	/**
	 * 描述：根据等比例缩放图片.
	 * 
	 * @param bitmap
	 *            the bitmap
	 * @param scale
	 *            比例
	 * @return Bitmap 新图片
	 */
	public static Bitmap scaleImg(Bitmap bitmap, float scale) {
		Bitmap resizeBmp = null;
		try {
			// 获取Bitmap资源的宽和高
			int bmpW = bitmap.getWidth();
			int bmpH = bitmap.getHeight();
			// 注意这个Matirx是android.graphics底下的那个
			Matrix mt = new Matrix();
			// 设置缩放系数，分别为原来的0.8和0.8
			mt.postScale(scale, scale);
			resizeBmp = Bitmap.createBitmap(bitmap, 0, 0, bmpW, bmpH, mt, true);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (resizeBmp != bitmap) {
				bitmap.recycle();
			}
		}
		return resizeBmp;
	}
	
	/**
	 * bitmap转为base64
	 * @param bitmap
	 * @return
	 */
	public static String bitmapToBase64(Bitmap bitmap) {

		String result = null;
		ByteArrayOutputStream baos = null;
		try {
			if (bitmap != null) {
				baos = new ByteArrayOutputStream();
				bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);

				baos.flush();
				baos.close();

				byte[] bitmapBytes = baos.toByteArray();
				result = Base64.encodeToString(bitmapBytes, Base64.DEFAULT);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (baos != null) {
					baos.flush();
					baos.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * base64转为bitmap
	 * @param base64Data
	 * @return
	 */
	public static Bitmap base64ToBitmap(String base64Data) {
		byte[] bytes = Base64.decode(base64Data, Base64.DEFAULT);
		return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
	}
}
