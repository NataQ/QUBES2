/*
 * Copyright (c) 2010, Jeffrey F. Cole
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are met:
 * 
 * 	Redistributions of source code must retain the above copyright notice, this
 * 	list of conditions and the following disclaimer.
 * 
 * 	Redistributions in binary form must reproduce the above copyright notice, 
 * 	this list of conditions and the following disclaimer in the documentation 
 * 	and/or other materials provided with the distribution.
 * 
 * 	Neither the name of the technologichron.net nor the names of its contributors 
 * 	may be used to endorse or promote products derived from this software 
 * 	without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE 
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE 
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE 
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR 
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF 
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS 
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN 
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) 
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE 
 * POSSIBILITY OF SUCH DAMAGE.
 */

package id.co.qualitas.qubes.helper;

import android.content.Context;
import android.os.Handler;
import android.text.InputType;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

/**
 * A simple layout group that provides a numeric text area with two buttons to
 * increment or decrement the value in the text area. Holding either button
 * will auto increment the value up or down appropriately. 
 * 
 * @author Jeffrey F. Cole
 *
 */
public class NumberPicker extends RelativeLayout {

	private final long REPEAT_DELAY = 50;
	
	private int ELEMENT_HEIGHT = 50;
	private int ELEMENT_WIDTH = ELEMENT_HEIGHT;
	
	private int elementHeightEditText = 70;
	private int elementWidthEditText = ELEMENT_HEIGHT;
	
	private final int MINIMUM = 0;
	private final int MAXIMUM = 999;
	
	private int textSize = 10;
	
	public Integer value;
	
	private Button decrement;
	private Button increment;
	public EditText valueText;
	
	private Handler repeatUpdateHandler = new Handler();
	
	private boolean autoIncrement = false;
	private boolean autoDecrement = false;

	/**
	 * This little guy handles the auto part of the auto incrementing feature.
	 * In doing so it instantiates itself. There has to be a pattern name for
	 * that...
	 * 
	 * @author Jeffrey F. Cole
	 *
	 */
	class RepetetiveUpdater implements Runnable {
		public void run() {
			if( autoIncrement ){
				increment();
				repeatUpdateHandler.postDelayed( new RepetetiveUpdater(), REPEAT_DELAY );
			} else if( autoDecrement ){
				decrement();
				repeatUpdateHandler.postDelayed( new RepetetiveUpdater(), REPEAT_DELAY );
			}
		}
	}
	
	public NumberPicker( Context context, AttributeSet attributeSet ) {
		super(context, attributeSet);
		
		this.setLayoutParams( new LayoutParams( LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT ) );
		this.setGravity(Gravity.CENTER);
		int density= getResources().getDisplayMetrics().densityDpi;
		DisplayMetrics dm = context.getResources().getDisplayMetrics();
		int densityDpi = dm.densityDpi;
		if (densityDpi == 480 || densityDpi == 640){
			ELEMENT_HEIGHT = 100;
			ELEMENT_WIDTH = ELEMENT_HEIGHT;
			elementHeightEditText = 120;
			elementWidthEditText = ELEMENT_HEIGHT;
		}
		if (densityDpi == 320){
			ELEMENT_HEIGHT = 80;
			ELEMENT_WIDTH = ELEMENT_HEIGHT;
			elementHeightEditText = 100;
			elementWidthEditText = ELEMENT_HEIGHT;
		}
		LayoutParams elementParamsDecrement = new LayoutParams( ELEMENT_HEIGHT, ELEMENT_WIDTH );
		elementParamsDecrement.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
		
		LayoutParams elementParamsIncrement = new LayoutParams( ELEMENT_HEIGHT, ELEMENT_WIDTH );
		elementParamsIncrement.addRule(RelativeLayout.ALIGN_BASELINE, 100);
		elementParamsIncrement.addRule(RelativeLayout.ALIGN_TOP, 102);
		elementParamsIncrement.addRule(RelativeLayout.RIGHT_OF, 102);
		
		LayoutParams elementParamsEditText = new LayoutParams( elementHeightEditText, elementWidthEditText );
		elementParamsEditText.addRule(RelativeLayout.RIGHT_OF, 100);
		elementParamsEditText.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
		
		
		// init the individual elements
		initDecrementButton( context );
		initValueEditText( context );
		initIncrementButton( context );
		
		addView( decrement, elementParamsDecrement );
		addView( valueText, elementParamsEditText );
		addView( increment, elementParamsIncrement );
	}
	
	private void initIncrementButton( Context context){
		increment = new Button( context );
		increment.setId(101);
		increment.setTextSize( textSize );
		increment.setText( "+" );
		
		// Increment once for a click
		increment.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
            	increment();
            }
        });
		
		// Auto increment for a long click
		increment.setOnLongClickListener( 
				new OnLongClickListener(){
					public boolean onLongClick(View arg0) {
						autoIncrement = true;
						repeatUpdateHandler.post( new RepetetiveUpdater() );
						return false;
					}
				}
		);
		
		// When the button is released, if we're auto incrementing, stop
		increment.setOnTouchListener( new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				if( event.getAction() == MotionEvent.ACTION_UP && autoIncrement ){
					autoIncrement = false;
				}
				return false;
			}
		});
	}
	
	private void initValueEditText( Context context){
		
		value = new Integer( 0 );
		
		valueText = new EditText( context );
		valueText.setId(102);
		valueText.setTextSize( textSize );
		
		// Since we're a number that gets affected by the button, we need to be
		// ready to change the numeric value with a simple ++/--, so whenever
		// the value is changed with a keyboard, convert that text value to a
		// number. We can set the text area to only allow numeric input, but 
		// even so, a carriage return can get hacked through. To prevent this
		// little quirk from causing a crash, store the value of the internal
		// number before attempting to parse the changed value in the text area
		// so we can revert to that in case the text change causes an invalid
		// number
		valueText.setOnKeyListener(new OnKeyListener() {
			public boolean onKey(View v, int arg1, KeyEvent event) {
				int backupValue = value;
				try {
					value = Integer.parseInt( ((EditText)v).getText().toString() );
				} catch( NumberFormatException nfe ){
					value = backupValue;
				}
				return false;
			}
		});
		
		// Highlight the number when we get focus
		valueText.setOnFocusChangeListener(new OnFocusChangeListener() {
			public void onFocusChange(View v, boolean hasFocus) {
				if( hasFocus ){
					((EditText)v).selectAll();
				}
			}
		});
		valueText.setGravity( Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL );
		valueText.setText( value.toString() );
		valueText.setInputType( InputType.TYPE_CLASS_NUMBER );
	}
	
	private void initDecrementButton( Context context){
		decrement = new Button( context );
		decrement.setId(100);
		decrement.setTextSize( textSize );
		decrement.setText( "-" );
		

		// Decrement once for a click
		decrement.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
            	decrement();
            }
        });
		

		// Auto Decrement for a long click
		decrement.setOnLongClickListener( 
				new OnLongClickListener(){
					public boolean onLongClick(View arg0) {
						autoDecrement = true;
						repeatUpdateHandler.post( new RepetetiveUpdater() );
						return false;
					}
				}
		);
		
		// When the button is released, if we're auto decrementing, stop
		decrement.setOnTouchListener( new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				if( event.getAction() == MotionEvent.ACTION_UP && autoDecrement ){
					autoDecrement = false;
				}
				return false;
			}
		});
	}
	
	public void increment(){
		if( value < MAXIMUM ){
			value = value + 1;
			valueText.setText( value.toString() );
		}
	}

	public void decrement(){
		if( value > MINIMUM ){
			value = value - 1;
			valueText.setText( value.toString() );
		}
	}
	
	public int getValue(){
		return value;
	}
	
	public void setValue( int value ){
		if( value > MAXIMUM ) value = MAXIMUM;
		if( value >= 0 ){
			this.value = value;
			valueText.setText( this.value.toString() );
		}
	}

	public Button getDecrement() {
		return decrement;
	}

	public Button getIncrement() {
		return increment;
	}

}