package nl.appsrus.vhack2012.ui;

import java.net.URI;

import nl.appsrus.vhack2012.R;
import nl.appsrus.vhack2012.ui.DownloadImageQueue.DownloadInfo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v4.util.LruCache;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class RemoteImageView extends ImageView {

	private Animation mAnimation;

	private static LruCache<URI, Bitmap> sCache;
	
	public static void initialize (Context context) {
		// Will cache 5 items
		final int cacheSize = 10;
		// Use 1/10th of the available memory for this memory cache.
		sCache = new LruCache<URI, Bitmap>(cacheSize) {
			@Override
			protected int sizeOf(URI key, Bitmap bitmap) {
				return 1;
			}
		};
	}
	
	private Runnable mAssignBitmapRunnable = new Runnable() {		
		@Override
		public void run() {
			if (mDrawable != null) {
				// At this point we animate it (reuse the previous animation if required
				mAnimation.cancel();
				startAnimation(mAnimation);
				setImageBitmap(mDrawable);
			}
			else {
				setImageDrawable(sErrorImages.get(getId()));
			}
		}
	};

	private static SparseArray<Drawable> sDefaultImages = new SparseArray<Drawable>();
	private static SparseArray<Drawable> sErrorImages = new SparseArray<Drawable>();

	private Bitmap mDrawable;
	private URI mLastUrl;

	public RemoteImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mAnimation = AnimationUtils.loadAnimation(getContext(), R.animator.fade_in);		
	}

	public RemoteImageView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public RemoteImageView(Context context) {
		this(context, null);
	}

	public synchronized void configure (int defaultResId, int networkErrorResId) {
	    Context c = getContext();
		// These 2 items are very important, we are going to have them in a hashMap by viewId
		// Only update them the first time
		if (sDefaultImages.get(getId()) == null) {
			sDefaultImages.put(getId(), c.getResources().getDrawable(defaultResId));
		}
		if (sErrorImages.get(getId()) == null) {
			sErrorImages.put(getId(), c.getResources().getDrawable(networkErrorResId));
		}
	}

	public synchronized void loadURI(URI imageUrl) {
		DownloadImageQueue.removeFromLoadingQueueByRequester(this);
		Bitmap b = getFromLocalCache(imageUrl);
		if (b != null) {
			mLastUrl = null;
			setImageBitmap(b);
		}
		else {
			mLastUrl = imageUrl;
			setImageDrawable(sDefaultImages.get(getId()));
			DownloadImageQueue.addToLoadingQueue(DownloadInfo.newInstance(this, imageUrl, getId()));
		}
	}

	private Bitmap getFromLocalCache(URI imageUrl) {
		return sCache.get(imageUrl);
	}
	
	public void preloadImage(URI imageUrl) {
		DownloadImageQueue.addToPreloadingQueue(DownloadInfo.newInstance(null, imageUrl, getId()));
	}

	public synchronized void assignBitmap(URI url, Bitmap drawable) {
		if (!url.equals(mLastUrl)) {
			return;
		}
		mLastUrl  = null;
		mDrawable = drawable;
		post(mAssignBitmapRunnable);
	}

    public static void clearCache() {
        sCache.evictAll();        
    }

    public static void updateImage(URI url, Bitmap b) {
        if (sCache.get(url) == null) {
            sCache.put(url, b);
        }        
    }

}
