package com.ace.lifecounter;

import java.util.Random;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends Activity {

	private GestureDetector counterSwipeP1;
	private GestureDetector counterSwipeP2;
	private TextView player1life;
	private TextView player2life;
    private PowerManager.WakeLock wakeLock;
    private PowerManager pm;
    private int saveP1Life;
    private int saveP2Life;
    private int lifeDelta1;
    private int lifeDelta2;
    private Handler mHandler = new Handler();
    private TextView p1log;
    private TextView p2log;
    private int p1life;
    private int p2life;
    private int p1LifeLog;
    private int p2LifeLog;
    private boolean gameStarted;
   	private Random random = new Random();
   	private String result;
   	private String title;
   	private LinearLayout log;
    
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //outState.putString("p1Life", player1life.getText().toString());
        //outState.putString("p2Life", player2life.getText().toString());
        outState.putBoolean("gameStarted", gameStarted);
        outState.putInt("log_visibility", log.getVisibility());

        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        //player1life.setText(savedInstanceState.getString("p1Life"));
        //player2life.setText(savedInstanceState.getString("p2Life"));
        gameStarted=savedInstanceState.getBoolean("gameStarted");
        //noinspection ResourceType
        log.setVisibility(savedInstanceState.getInt("log_visibility"));
        
    }

	@Override
	protected void onResume() 
	{
	    super.onResume();
	    updatePrefs();
	    wakeLock.acquire();
	    Log.v("PowerAce", String.valueOf(gameStarted));
	    
    }
	
	private void reset() 
	{
		gameStarted=false;
		updatePrefs();
		mHandler.removeCallbacks(setStackP1);
		mHandler.removeCallbacks(setStackP2);
	}
	
	private Runnable setStackP1 = new Runnable() {
		   public void run() {
			   	int p1life=Integer.valueOf(player1life.getText().toString()).intValue();
	        	p1LifeLog=p1LifeLog+p1life;
	        	String text = p1log.getText().toString();
	        	String[] array = text.split(", ");
	        	String lastLife = array[array.length-1];
	        	if (!lastLife.equals(String.valueOf(p1LifeLog))) {
	        	p1log.setText(text + ", " + String.valueOf(p1LifeLog));
	        	}
	        	array=null;
	        	p1LifeLog=0;
	    		mHandler.removeCallbacks(setStackP1);
		   }
		}; 
		private Runnable setStackP2 = new Runnable() {
			   public void run() {
				   	int p2life=Integer.valueOf(player2life.getText().toString()).intValue();
		        	p2LifeLog=p2LifeLog+p2life;
		        	String text = p2log.getText().toString();
		        	String[] array = text.split(", ");
		        	String lastLife = array[array.length-1];
		        	if (!lastLife.equals(String.valueOf(p2LifeLog))) {
		        	p2log.setText(text + ", " + String.valueOf(p2LifeLog));
		        	}
		        	array=null;
		        	p2LifeLog=0;
		    		mHandler.removeCallbacks(setStackP2);
			   }
			}; 
		
	public void setActivityBackgroundColor(String color) {
	    View view = this.getWindow().getDecorView();
	    view.setBackgroundColor(Color.parseColor(color));
	}
	
	private void updatePrefs(){
        player1life = (TextView)findViewById(R.id.player1);
        player2life = (TextView)findViewById(R.id.player2);
        p1log = (TextView)findViewById(R.id.p1log);
        p2log = (TextView)findViewById(R.id.p2log);
        
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
     
        int life = prefs.getInt(PreferencesActivity.STARTING_HP, 1);
        int colorName = prefs.getInt(PreferencesActivity.COLOR, 3);

        String[] colorValues = getResources().getStringArray(R.array.color_values);
        String[] hps = getResources().getStringArray(R.array.max_hp_options);
        
       player1life.setTextColor(Color.parseColor(colorValues[colorName]));        
        
       player2life.setTextColor(Color.parseColor(colorValues[colorName]));
        if(gameStarted!=true) {
        player1life.setText(String.valueOf(hps[life]));
        player2life.setText(String.valueOf(hps[life]));
        if (hps[life]!=null) {
        p1log.setText(String.valueOf(hps[life]));
        p2log.setText(String.valueOf(hps[life]));
        }
        }
	}
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        player1life = (TextView)findViewById(R.id.player1);
        player2life = (TextView)findViewById(R.id.player2);
        p1log = (TextView)findViewById(R.id.p1log);
        p2log = (TextView)findViewById(R.id.p2log);
        log = (LinearLayout)findViewById(R.id.log);
      
        gameStarted=false;
        reset();
        //updatePrefs();
        
        player1life.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                // TODO Auto-generated method stub
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {

            	mHandler.postDelayed(setStackP1, 3000);
            }
        });
        
        player2life.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                // TODO Auto-generated method stub
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {

          	  	mHandler.postDelayed(setStackP2, 3000);
            }
        });
        
        counterSwipeP1 = new GestureDetector(this, new SimpleOnGestureListener()
        {
        @Override
        public boolean onFling(MotionEvent start, MotionEvent finish, float velocityX, float velocityY)
        {
        	int p1life=Integer.valueOf(player1life.getText().toString()).intValue();
        	  if (start.getRawX() < finish.getRawX()) {
              	p1life++;
                player1life.setText(String.valueOf(p1life));
                } else {
                  	p1life--;
                    player1life.setText(String.valueOf(p1life));
                    }
        	return false;
        }	
        });
        
        counterSwipeP2 = new GestureDetector(this, new SimpleOnGestureListener()
        {
        @Override
        public boolean onFling(MotionEvent start, MotionEvent finish, float velocityX, float velocityY)
        {
        	int p2life=Integer.valueOf(player2life.getText().toString()).intValue();
        	  if (start.getRawX() < finish.getRawX()) {
              	p2life++;
                player2life.setText(String.valueOf(p2life));
                } else {
                  	p2life--;
                    player2life.setText(String.valueOf(p2life));
                    }
        	return false;
        }	
        });
        

        player1life.setOnTouchListener(new View.OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				 counterSwipeP1.onTouchEvent(event);
  //               mHandler.postDelayed(setStackP1, 5000);
                 gameStarted=true;
				return true;
			}
		});
        player2life.setOnTouchListener(new View.OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				 counterSwipeP2.onTouchEvent(event);
   //              mHandler.postDelayed(setStackP2, 5000);
                 gameStarted=true;
				return true;
			}
		});
        
        pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wakeLock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "LifeCounter wakelock");
	    Log.v("PowerAce", "::wakeLock NOT aquired onCreate::");


    }
    
    @Override
    public void onPause(){
    	super.onPause();
    	wakeLock.release();
	    Log.v("PowerAce", String.valueOf(gameStarted));
    }
    


   

        public void onClick(View v) {
            //Switch statement so you don't have to use a lot of click listeners
            //TextView player1life;
            //player1life = (TextView)findViewById(R.id.player1);
            //TextView player2life;
            //player2life = (TextView)findViewById(R.id.player2);

        	p1life=Integer.valueOf(player1life.getText().toString()).intValue();
        	p2life=Integer.valueOf(player2life.getText().toString()).intValue();
            gameStarted=true;
            switch (v.getId()) {
                case R.id.button1plus_player1:
                	
                	p1life++;
                    player1life.setText(String.valueOf(p1life));
  //                  mHandler.postDelayed(setStackP1, 5000);
                    break;
                case R.id.button1minus_player1:
                	p1life--;
                    player1life.setText(String.valueOf(p1life));
  //                  mHandler.postDelayed(setStackP1, 5000);
                    break;
                case R.id.button5plus_player1:
                	p1life=p1life+5;
                    player1life.setText(String.valueOf(p1life));
  //                  mHandler.postDelayed(setStackP1, 5000);
                    break;
                case R.id.button5minus_player1:
                	p1life=p1life-5;
                    player1life.setText(String.valueOf(p1life));
  //                  mHandler.postDelayed(setStackP1, 5000);
                    break;
                case R.id.button1plus_player2:
                	p2life++;
                    player2life.setText(String.valueOf(p2life));
  //                  mHandler.postDelayed(setStackP2, 5000);
                    break;
                case R.id.button1minus_player2:
                	p2life--;
                    player2life.setText(String.valueOf(p2life));
   //                 mHandler.postDelayed(setStackP2, 5000);
                    break;
                case R.id.button5plus_player2:
                	p2life=p2life+5;
                  player2life.setText(String.valueOf(p2life));
  //                mHandler.postDelayed(setStackP2, 5000);
                    break;
                case R.id.button5minus_player2:
                	p2life=p2life-5;
                    player2life.setText(String.valueOf(p2life));
   //                 mHandler.postDelayed(setStackP2, 5000);
                    break;
                default:
                	player1life.setText("Error");
                	player2life.setText("Error");
            }
        };
  
    
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    
    private static final int SHOW_PREFERENCES = 1;
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
         
        switch (item.getItemId())
        {
        case R.id.action_settings:
        {	
        	Intent i = new Intent(this, PreferencesActivity.class);
        	startActivityForResult(i, SHOW_PREFERENCES);
        	return true;
        }
        case R.id.new_game:
        {
        	AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.MyDialog));
        	
        	builder.setTitle("Are you sure?");        	
        	//builder.setMessage("This will clear log and reset life.");
        	builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
        	           public void onClick(DialogInterface dialog, int id) {
        	        	   reset();
        	           }
        	       });
        	builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
        	           public void onClick(DialogInterface dialog, int id) {
        	           }
        	       });
        	AlertDialog dialog = builder.create();
        	dialog.show();
        	return true;
        }
        case R.id.log_visibility:
        {
        	if (log.getVisibility()==View.GONE) {
        		
        		log.setVisibility(View.VISIBLE);
        	} else {
        		log.setVisibility(View.GONE);
        	}
  
        	return true;
        }
        case R.id.die_roll:
        {
        	AlertDialog.Builder menuAlert = new AlertDialog.Builder(this);
        	final String[] menuList = { "Flip a coin", "Roll d6", "Roll d10", "Roll d20" };
        	menuAlert.setTitle("Select roll:");
        	menuAlert.setItems(menuList,new DialogInterface.OnClickListener() {
        	 public void onClick(DialogInterface dialog, int item) {
        	  switch (item) {
        	  case 0:
        	  {		
        		  	title="Coin flip:";
                 	if (random.nextBoolean() == true) {
                   		result="Heads";
                   	} else {
                   		result="Tails";
                   	}
        	  break;
        	  }
        	  case 1:
        	  {
      		  	title="D6 roll:";
               	result=String.valueOf(random.nextInt(5)+1);  
        	  
        	  break;
        	  }
        	  case 2:
        	  {
      		  	title="D10 roll:";
               	result=String.valueOf(random.nextInt(9)+1);  
        	  
        	  break;
        	  }
        	  case 3:
        	  {
      		  	title="D20 roll:";
               	result=String.valueOf(random.nextInt(19)+1);  
        	  
        	  break;
        	  }
        	  }
        	  
      		final  AlertDialog.Builder coinBuilder = new AlertDialog.Builder(new ContextThemeWrapper(MainActivity.this, R.style.MyDialog));

          	coinBuilder.setTitle(title+" "+result);        	
          	coinBuilder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
          	           public void onClick(DialogInterface dialog, int id) {
          	           }
          	       });
          	AlertDialog coinDialog = coinBuilder.create();
          	coinDialog.show();
        	 }
        	});
        	AlertDialog menuDrop = menuAlert.create();
        	menuDrop.show();
        }
        
        
    }
    return false;
}
}
