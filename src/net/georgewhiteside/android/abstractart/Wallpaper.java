package net.georgewhiteside.android.abstractart;

import org.jf.GLWallpaper.GLWallpaperService;
//import net.rbgrn.android.glwallpaperservice.GLWallpaperService;

import android.app.WallpaperManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.SurfaceHolder;

public class Wallpaper extends GLWallpaperService 
{
	public static final String SHARED_PREFS_NAME = "AbstractArtSettings";
	public static AbstractArtEngine engine;
	
	SharedPreferences preferences;// = getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
	/*private GestureDetector gestureDetector;*/
	
	public Wallpaper()
	{
		super();
	}
	
	@Override
	public void onCreate()
	{
		super.onCreate();
		
		//PreferenceManager.setDefaultValues(this, R.xml.settings, false);
		//PreferenceManager.setDefaultValues(this, SHARED_PREFS_NAME, Context.MODE_PRIVATE, R.xml.settings, false);
		preferences = getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
	}
	
	@Override
	public Engine onCreateEngine()
	{
		engine = new AbstractArtEngine(this);
		return engine;
	}
	
	public class AbstractArtEngine extends GLEngine
	{
		public net.georgewhiteside.android.abstractart.Renderer renderer;
		
		private long lastTap = 0; 
        private static final long TAP_THRESHOLD = 500;
		
		AbstractArtEngine(GLWallpaperService glws)
		{
			super();
			renderer = new net.georgewhiteside.android.abstractart.Renderer(glws);
			
			setEGLContextClientVersion(2);
			setRenderer(renderer);
			setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
		}
		
		@Override
	    public void onCreate(SurfaceHolder surfaceHolder)
	    {
	        super.onCreate(surfaceHolder);
	        
	        //android.os.Debug.waitForDebugger();
	        setTouchEventsEnabled(true);
	        
	        /*gestureDetector = new GestureDetector(new GestureDetector.SimpleOnGestureListener()
	        {
	            @Override
	            public boolean onSingleTapConfirmed(MotionEvent e)
	            {
	            	Log.e("tapdebug", e.toString());
	            	return true;
	            }
	        });*/
	    }
		
		/*@Override
		public void onTouchEvent(MotionEvent event)
		{
			gestureDetector.onTouchEvent(event);         
        }*/
		
		@Override
        public Bundle onCommand(final String action, int x, int y, int z, final Bundle extras, final boolean resultRequested)
        {
	            if (action.equals(WallpaperManager.COMMAND_TAP))
	            {
	            	long thisTap = System.currentTimeMillis();
	            	if(thisTap - lastTap < TAP_THRESHOLD)
	            	{
	            		queueEvent( new Runnable()
	            		{
	            			public void run()
	            			{
	            				renderer.setRandomBackground();
	            			}
	            		});
	            		
	            		lastTap = 0;
	            	}
	            	else
	            	{
	            		lastTap = thisTap;
	            	}
	            } 
	
	            return super.onCommand(action, x, y, z, extras, resultRequested);
        }
	}
}
