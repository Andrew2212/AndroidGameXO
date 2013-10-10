package org.hexlet.gamexo.gamefield;

import java.util.List;

import org.hexlet.gamexo.GameActivity;
import org.hexlet.gamexo.R;
import org.hexlet.gamexo.gamefield.players.Player;
import org.hexlet.gamexo.utils.Logger;
import org.hexlet.gamexo.utils.MyOwnMove;
import org.hexlet.gamexo.utils.Sounder;
import org.hexlet.gamexo.utils.Toaster;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * @author Andrew2212
 * 
 */
public class GameView extends View {

	private static final int X = 0; // coordinate 'x' = cellWin[0]
	private static final int Y = 1; // coordinate 'y' = cellWin[1]
	
	private static int cellNum;// Amount of the cells from 'Preferences'
	private static int numCheckedSigns;// Number signs to Win from 'Preferences'

	private final Rect rectPictSrc = new Rect();// area of the picture
	private final Rect rectPictDst = new Rect();// destination area of the view

	private Paint paintBmpPict;
	private Paint paintLineField;
	private Paint lineWinPaint;// ***NOT USED STILL

	private Bitmap bmpSignPlayer1;
	private Bitmap bmpSignPlayer2;
	private Bitmap bmpCellWin;
	private Drawable fieldBackground;// view fieldBackground

	private int viewSize;
	private int fieldSize;
	private int cellSize;// Calculated size of the cell for the current screen

	private Context context;
	private GameFieldController gameFieldController;
	
//	****************************************************
private Player player = new Player();
private int count = 0;
public int cellX = 0;
public int cellY = 0;
//	****************************************************

	public GameView(Context context, AttributeSet attrs) {
		super(context, attrs);

		this.context = context;
		setPrefsValue();
		requestFocus();// To give focus to a specific view

		// Initialize something
		init();

		// GameField Matrix
		GameField.initNewFieldMatrix(cellNum);
		// GameFieldController
		gameFieldController = new GameFieldController(cellNum, numCheckedSigns);

		// Set rectangle for sign pictures and cellwinBackground
		setRectSrc();

	}

	// -------------Overridden View methods------------------
	/**
	 * Keep the view squared
	 */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		// Get view size
		int width = MeasureSpec.getSize(widthMeasureSpec);
		int height = MeasureSpec.getSize(heightMeasureSpec);
		viewSize = width < height ? width : height;

		fieldSize = calculateFieldSize();
		Logger.v("Field size = " + fieldSize);
		setMeasuredDimension(fieldSize, fieldSize);

	}
	

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);		
//		****************************
		count += 1;
		Logger.i("onDraw(canvas)*************** " + count + " cellX = " + cellX + " cellY = " + cellY);		
		setRectDst((count + cellX) * cellSize, (count + cellY) * cellSize);
		canvas.drawBitmap(bmpCellWin, rectPictSrc, rectPictDst,
				paintBmpPict);
//		***************************

		// Draw vertical lines
		int startVertX = cellSize;
		int startVertY = 0;
		for (int i = 0; i < cellNum - 1; i++) {
			canvas.drawLine(startVertX, startVertY, startVertX, fieldSize,
					paintLineField);
			startVertX += cellSize;

		}

		// Draw horizontal lines
		int startHorX = 0;
		int startHorY = cellSize;
		for (int i = 0; i < cellNum - 1; i++) {
			canvas.drawLine(startHorX, startHorY, fieldSize, startHorY,
					paintLineField);
			startHorY += cellSize;
		}

		// Draws win cell
		if (gameFieldController.getIsGameOver()) {

			Logger.v();

			List<int[]> listCellWin = gameFieldController.getListCellWin();
			for (int[] cellWin : listCellWin) {
				Logger.v("cellWin[X] = " + cellWin[X] + ", cellWin[Y] = "
						+ cellWin[Y]);

				setRectDst(cellWin[X] * cellSize, cellWin[Y] * cellSize);
				canvas.drawBitmap(bmpCellWin, rectPictSrc, rectPictDst,
						paintBmpPict);
			}

		}

		// Draws sign within gameField in accordance with GameField::fieldMatrix
		for (int i = 0; i < cellNum; i++) {
			for (int j = 0; j < cellNum; j++) {

				char sign = GameField.getCellValue(i, j);

				setRectDst(i * cellSize, j * cellSize);

				switch (sign) {

				case GameField.VALUE_X:
					canvas.drawBitmap(bmpSignPlayer1, rectPictSrc, rectPictDst,
							paintBmpPict);
					break;

				case GameField.VALUE_O:
					canvas.drawBitmap(bmpSignPlayer2, rectPictSrc, rectPictDst,
							paintBmpPict);
					break;

				default:
					break;
				}
			}
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {

		Logger.v();

		if (gameFieldController.getIsGameOver()) {
			Toaster.doToastShort(context, "GameOver!");
			return false;
		}

		int action = event.getAction();

		if (action == MotionEvent.ACTION_DOWN) {
			return true;
		} else if (action == MotionEvent.ACTION_UP) {
			// // Get coordinates of touch
			int x = (int) event.getX();
			int y = (int) event.getY();
			int cellX = 0;
			int cellY = 0;

			// Calculate fieldMatrix[x][y]
			int cellNumeroX = (int) Math.floor(x / cellSize);
			int cellNumeroY = (int) Math.floor(y / cellSize);
			// Calculate coordinate touched cell
			cellX = cellSize * cellNumeroX;
			cellY = cellSize * cellNumeroY;
//	**************************************************************		
			int[] move = new int[]{cellNumeroX,cellNumeroY};
			if(move != null){
//				public static method into GameActivity
				GameActivity.setOnTouchResult("" + move[0]);//tvUserCount.setText(result);
			}
			
			
//  **************************************************************

			// Set value into fieldMatrix
			if (GameField.setSignToCell(cellNumeroX, cellNumeroY)) {

				if (gameFieldController.checkGameOver(cellNumeroX, cellNumeroY)) {
					Toaster.doToastShort(context, "GameOver!");
				}

				Logger.v("cellX = " + cellX + ", cellY = " + cellY);
				GameActivity.switchPlayer();

				Sounder.doSound(context, R.raw.beep);
				invalidate();
				return true;
			}
			Sounder.doSound(context, R.raw.wilhelm_scream);
			return false;
		}

		return false;
	}

	// -------Private Methods---------------------------

	/**
	 * WHAT we are drawing (rectangle for sign picture)</br> All pictures for
	 * the cell ('X' and 'O' and other) must have the SAME SIZE! (as
	 * 'standart_cell_size')
	 */
	private void setRectSrc() {
		Drawable d = getResources().getDrawable(R.drawable.standart_cell_size);
		int w = d.getIntrinsicWidth();
		int h = d.getIntrinsicHeight();
		rectPictSrc.set(0, 0, w, h);
	}

	/**
	 * WHERE we are drawing (rectangle for picture destination on the field; 'x
	 * & y' - top-left corner))
	 * 
	 * @param x
	 * @param y
	 */
	private void setRectDst(int x, int y) {
		int left = x;
		int top = y;
		int right = x + cellSize;
		int bottom = y + cellSize;

		rectPictDst.set(left, top, right, bottom);
	}

	@SuppressWarnings("deprecation")
	private void init() {

		// Set Background
		fieldBackground = getResources().getDrawable(R.drawable.field_background);
		setBackgroundDrawable(fieldBackground);

		paintBmpPict = new Paint(Paint.ANTI_ALIAS_FLAG);

		// Line field Paint
		paintLineField = new Paint();
		int lineFieldColor = getResources().getColor(R.color.color_line_field);
		paintLineField.setColor(lineFieldColor);
		int lineFieldWidth = getResources().getInteger(
				R.integer.line_field_stroke_width);
		paintLineField.setStrokeWidth(lineFieldWidth);
		paintLineField.setStyle(Style.STROKE);

		// Line win Paint***NOT USED STILL
		lineWinPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		int lineWinColor = getResources().getColor(R.color.color_line_win);
		lineWinPaint.setColor(lineWinColor);
		int lineWinWidth = getResources().getInteger(
				R.integer.line_win_stroke_width);
		lineWinPaint.setStrokeWidth(lineWinWidth);
		lineWinPaint.setStyle(Style.STROKE);

		// Picture for Player's Sign and cellWin
		bmpSignPlayer1 = getResBitmap(R.drawable.lib_cross);
		bmpSignPlayer2 = getResBitmap(R.drawable.lib_circle);
		bmpCellWin = getResBitmap(R.drawable.cell_win_background);
	}

	private int calculateFieldSize() {

		fieldSize = calculateCellSize() * cellNum;
		// Logger.v("fieldSize = " + fieldSize);
		return fieldSize;
	}

	private int calculateCellSize() {

		int cellSizeMin = getResources().getInteger(R.integer.cell_size_min);
		int cellSizeCalc = (int) Math.floor(viewSize / cellNum);

		if (cellSizeMin < cellSizeCalc) {
			cellSize = cellSizeCalc;

		} else {
			cellSize = cellSizeMin;
			cellNum = (int) Math.floor(viewSize / cellSizeMin);
		}

		// Logger.v("cellSize = " + cellSize);
		return cellSize;
	}

	/**
	 * Sets value from 'Preferences' such as 'fieldSize' and 'numCheckedSigns' to SOMEWHERE
	 */
	private void setPrefsValue() {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(context);

		// Get default value from 'Preferences' (defaultValue = 3)
		int defaultValue = Integer.valueOf(prefs.getString(getResources()
				.getString(R.string.pref_field_size_value), getResources()
				.getString(R.string.pref_field_size_value)));

		// Get amount of the cells from 'Preferences'
		cellNum = Integer.valueOf(prefs.getString(
				getResources().getString(R.string.pref_field_size_key),
				getResources().getString(R.string.pref_field_size_value)));
		if (cellNum < defaultValue)
			cellNum = defaultValue;

		// Get number signs to Win from 'Preferences'
		numCheckedSigns = Integer.valueOf(prefs.getString(getResources()
				.getString(R.string.pref_num_check_signs_key), getResources()
				.getString(R.string.pref_num_check_signs_value)));
		if (cellNum < numCheckedSigns) {
			numCheckedSigns = defaultValue;
		}

	}

	/**
	 * @param bmpResId
	 * @return Bitmap bmp from resources with some options
	 */
	private Bitmap getResBitmap(int bmpResId) {

		Options opts = new Options();

		// If dither is true, the decoder will attempt to dither the decoded
		// image.
		opts.inDither = false;

		Resources res = getResources();
		Bitmap bmp = BitmapFactory.decodeResource(res, bmpResId, opts);

		if (bmp == null && isInEditMode()) {

			Drawable drawable = res.getDrawable(bmpResId);
			int width = drawable.getIntrinsicWidth();
			int height = drawable.getIntrinsicHeight();

			bmp = Bitmap.createBitmap(width, height, Config.ARGB_8888);

			Canvas canvas = new Canvas(bmp);
			drawable.setBounds(0, 0, cellSize, cellSize);
			drawable.draw(canvas);
		}

		return bmp;
	}

}
