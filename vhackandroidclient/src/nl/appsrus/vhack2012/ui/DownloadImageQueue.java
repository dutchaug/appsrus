package nl.appsrus.vhack2012.ui;

import java.net.URI;
import java.util.ArrayList;
import java.util.Stack;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.SparseArray;
import android.view.View;

public class DownloadImageQueue extends Thread {

	public static class DownloadInfo {
		private static Stack<DownloadInfo> sStackItems = new Stack<DownloadInfo>();
		
		private URI mUrl;
		private RemoteImageView mRemoteImageView;
		private int mViewId;

		private DownloadInfo(RemoteImageView remoteImageView, URI imageUrl, int viewId) {
			initialize(remoteImageView, imageUrl, viewId);
		}
		
		private void initialize(RemoteImageView remoteImageView, URI imageUrl, int id) {
			mRemoteImageView = remoteImageView;
			mUrl = imageUrl;
			mViewId = id;
		}

		public URI getUrl() {
			return mUrl;
		}

		public void assignBitmap(Bitmap b) {
			mRemoteImageView.assignBitmap(mUrl, b);
		}

		public View getImageView() {
			return mRemoteImageView;
		}
		
		public int getViewId() {
			return mViewId;
		}

		public static DownloadInfo newInstance(RemoteImageView remoteImageView,	URI imageUrl, int id) {
			if (sStackItems.isEmpty()) {
				return new DownloadInfo(remoteImageView, imageUrl, id);
			}
			DownloadInfo ret = sStackItems.pop();
			ret.initialize(remoteImageView, imageUrl, id);
			return ret;
		}
		
		public static void release (DownloadInfo info) {
			info.initialize(null, null, 0);
			sStackItems.push(info);
		}

	}

	private static final boolean USE_SINGLE_QUEUE = false;

	private static SparseArray<DownloadImageQueue> sInstances;
	private static DownloadImageQueue sSingleInstance;

	// We'll do this with a priority queue
	private ArrayList<DownloadInfo> mLoading = new ArrayList<DownloadInfo>();
	private ArrayList<DownloadInfo> mPreLoading = new ArrayList<DownloadInfo>();

	private Object mLock = new Object();

	@Override
	public void run () {
		while (true) {
			DownloadInfo info = null;
			while (true) {
				synchronized (mLoading) {
					info = getInfo(mLoading);
				}
				if (info == null) {
					break;
				}
				Bitmap b = performDownload(info);
				info.assignBitmap (b);
				DownloadInfo.release(info);
			}
			// Take a max of one of these before checking the first queue
			synchronized (mPreLoading) {
				info = getInfo(mPreLoading);
			}
			if (info != null) {
				performDownload(info);
				DownloadInfo.release(info);
			}
			else {
				// Both lists are empty, wait for new data
				synchronized (mLock) {					
					try {
						mLock.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	private DownloadInfo getInfo(ArrayList<DownloadInfo> list) {
		if (!list.isEmpty()) {
			return list.remove(0);						
		}
		return null;
	}


	private Bitmap performDownload(DownloadInfo info) {
		try {			
			HttpGet request = new HttpGet(info.getUrl());
			// Execute HTTP Request
			HttpClient httpClient = new DefaultHttpClient();
			// TODO: Should we add some more restrictive timeout here?
			HttpResponse response = httpClient.execute(request);
			// Decode only if 200 (not checking it will slow us down and throw an exception anyway)
//			if (response.getStatusLine().getStatusCode() == HttpURLConnection.HTTP_OK) {
				// Decode it into a bitmap
				Bitmap b = BitmapFactory.decodeStream(response.getEntity().getContent());
			    RemoteImageView.updateImage(info.getUrl(), b);
				return b;
//			}
		} 
		catch (Exception e) {
			// Decoding exception, IO exception and so on, they are all treated as a network error
			// The result is that we return a null bitmap
		}
		catch (OutOfMemoryError error) {
			// Try to recover properly from this errors
			// We consider it a network failure and return null as well
			// But we also empty the cache
			RemoteImageView.clearCache();
		}
		return null;
	}

	public static void addToLoadingQueue(DownloadInfo info) {
		getInstance(info.getViewId()).addToLoading(info);
	}

	public static void addToPreloadingQueue(DownloadInfo info) {
		getInstance(info.getViewId()).addToPreloading(info);
	}

	private void addToPreloading(DownloadInfo info) {
		// The view that is requesting this will be removed from all queues
		removeFromPreloadQueueByUrl(info.getUrl());
		mPreLoading.add(info);
		// notify
		synchronized (mLock) {					
			mLock.notify();
		}
	}

	private void addToLoading(DownloadInfo info) {
		// The view that is requesting this will be removed from all queues		
		removeFromPreloadQueueByUrl(info.getUrl());
		mLoading.add(info);
		// notify
		synchronized (mLock) {					
			mLock.notify();
		}
	}

	public static void removeFromLoadingQueueByRequester(View imageView) {
		getInstance(imageView.getId()).removeFromLoadingQueue(imageView);
	}

	private void removeFromLoadingQueue(View imageView) {
		synchronized(mLoading) {
			int count = mLoading.size();
			for (int i=0; i<count; i++) {
				DownloadInfo di = mLoading.get(i);
				if (di.getImageView().equals(imageView)){
					mLoading.remove(i);
					break;
				}
			}
		}		
	}

	private void removeFromPreloadQueueByUrl(URI imageUrl) {
		synchronized(mPreLoading) {
			int count = mPreLoading.size();
			for (int i=0; i<count; i++) {
				DownloadInfo di = mPreLoading.get(i);
				if (di.getUrl().equals(imageUrl)){
					mPreLoading.remove(i);
					break;
				}
			}
		}
	}

	private static DownloadImageQueue getInstance(int id) {
		DownloadImageQueue currentQueue;
		if (USE_SINGLE_QUEUE) {
			if (sSingleInstance == null) {
				sSingleInstance = new DownloadImageQueue();
				sSingleInstance.start();
			}
			currentQueue = sSingleInstance;
		}
		else {
			if (sInstances == null) {
				sInstances = new SparseArray<DownloadImageQueue>();
			}
			currentQueue = sInstances.get(id);
			if (currentQueue == null) {
				currentQueue = new DownloadImageQueue();
				currentQueue.start();
				sInstances.put(id, currentQueue);
			}
		}
		return currentQueue;
	}
}
