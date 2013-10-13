package org.hexlet.gamexo.gamefield;

import java.util.List;

import org.hexlet.gamexo.GameActivity;
import org.hexlet.gamexo.R;
import org.hexlet.gamexo.utils.Logger;
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

	private static int fieldSizePrefs;// Amount of the cells from 'Preferences'
	private static int fieldSizeCalculated;// Field Size - calculated number of
											// cells
	private static int numCheckedSignsPrefs;// NumberSignsWIN from Preferences
	private static GameFieldController gameFieldController;

	private final Rect rectPictSrc = new Rect();// area of the picture
	private final Rect rectPictDst = new Rect();// destination area of the view

	private Paint paintBmpPict;
	private Paint paintLineField;
	private Paint lineWinPaint;// ***NOT USED STILL

	private Bitmap bmpSignPlayerX;
	private Bitmap bmpSignPlayerO;
	private Bitmap bmpCellWin;
	private Drawable fieldBackground;// view fieldBackground

	// size of the current 'view'
	private int viewSizeDip;
	// size of the field in 'dip'
	private int fieldSizeDip;
	// Calculated size of the cell for the current screen
	private int cellSizeDip;

	// It's that will returned by HumanLocalPlayer method 'doMove()'
	private static int[] resultOnTouch = new int[2];

	private Context context;

	public GameView(Context context, AttributeSet attrs) {
		super(context, attrs);

		this.context = context;
		setPrefsValue();

		requestFocus();// To give focus to a specific view

		// Initialize something
		init();

		// Set rectangle for sign pictures and cellwinBackground
		setRectSrc();

	}

	// ---------Getters and Setters----------------------------

	/**
	 * 
	 * @return fieldSizeCalculated <br>
	 *         It's size of field that is calculated in accordance with
	 *         Preferences value and size of this View (display size)</br>
	 */
	public static int getFieldSizeCalculated() {
		// Logger.v("fieldSizeCalculated = " + fieldSizeCalculated);
		return fieldSizeCalculated;
	}

	/**
	 * 
	 * @return numCheckedSignsPrefs <br>
	 *         It's corrected in accordance with some 'game restrictions' into
	 *         method 'setPrefsValue()'
	 */
	public static int getNumCheckedSigns() {
		return numCheckedSignsPrefs;
	}

	/**
	 * 
	 * @param gameFieldController
	 *            - controller that is set into GameActivity::initNewGame()
	 *            AFTER create 'new Game(enemy)'
	 */
	public void setGameFieldController(GameFieldController gameFieldController) {
		GameView.gameFieldController = gameFieldController;
	}

	/**
	 * 
	 * @return resultOnTouch for method PlayerHumanLocal 'doMove()'
	 */
	public static int[] getResultOnTouch() {
		return resultOnTouch;
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

		viewSizeDip = width < height ? width : height;
		fieldSizeDip = calculateFieldSizeDip();
		setMeasuredDimension(fieldSizeDip, fieldSizeDip);

		// Logger.v("width = " + width + " height = " + height);
		// Logger.v("viewSizeDip = " + viewSizeDip);
		// Logger.v("calculate::fieldSizeDip = " + fieldSizeDip);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		Logger.v();

		/**
		 * Initialize 'Game' with calculated 'fieldSize' - in accordance with
		 * conditions into 'initInfoGame()' it's called one time only
		 */
		GameActivity.initInfoGame();

		// Draw vertical lines
		int startVertX = cellSizeDip;
		int startVertY = 0;
		// Logger.v("fieldSizeCalculated = " + fieldSizeCalculated);

		for (int i = 0; i < fieldSizeCalculated - 1; i++) {
			canvas.drawLine(startVertX, startVertY, startVertX, fieldSizeDip,
					paintLineField);
			startVertX += cellSizeDip;

		}

		// Draw horizontal lines
		int startHorX = 0;
		int startHorY = cellSizeDip;
		for (int i = 0; i < fieldSizeCalculated - 1; i++) {
			canvas.drawLine(startHorX, startHorY, fieldSizeDip, startHorY,
					paintLineField);
			startHorY += cellSizeDip;
		}

		// Draws win cell
		if (gameFieldController != null && gameFieldController.getIsGameOver()) {
			Logger.v();

			List<int[]> listCellWin = gameFieldController.getListCellWin();
			for (int[] cellWin : listCellWin) {
				Logger.v("cellWin[X] = " + cellWin[X] + ", cellWin[Y] = "
						+ cellWin[Y]);

				setRectDst(cellWin[X] * cellSizeDip, cellWin[Y] * cellSizeDip);
				canvas.drawBitmap(bmpCellWin, rectPictSrc, rectPictDst,
						paintBmpPict);
			}

			// Increase count of WIN one of the Players
			GameActivity.increaseScoreWin();
		}

		// Draws sign within gameField in accordance with GameField::fieldMatrix
		for (int i = 0; i < fieldSizeCalculated; i++) {
			for (int j = 0; j < fieldSizeCalculated; j++) {

				Character sign = GameField.getCellValue(i, j);

				setRectDst(i * cellSizeDip, j * cellSizeDip);

				switch (sign) {

				case GameField.VALUE_X:
					canvas.drawBitmap(bmpSignPlayerX, rectPictSrc, rectPictDst,
							paintBmpPict);
					break;

				case GameField.VALUE_O:
					canvas.drawBitmap(bmpSignPlayerO, rectPictSrc, rectPictDst,
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
		if (!GameActivity.isGameStart)
			return false;

		if (gameFieldController.getIsGameOver()) {
			Toaster.doToastShort(context, "GameOver!");
			return false;
		}

		int action = event.getAction();

		if (action == MotionEvent.ACTION_DOWN) {
			return true;
		} else if (action == MotionEvent.ACTION_UP) {
			// Get coordinates of touch /dip/
			int x = (int) event.getX();
			int y = (int) event.getY();
			// Coordinates of touched cell /numero/ i.e. 'fieldMatrix[x][y]'
			int cellX = (int) Math.floor(x / cellSizeDip);
			int cellY = (int) Math.floor(y / cellSizeDip);

			// It's that will returned by HumanLocalPlayer method 'doMove()'
			// resultOnTouch = new int[] { cellX, cellY };
			resultOnTouch[X] = cellX;
			resultOnTouch[Y] = cellY;
			Logger.v("resultOnTouch[X] = " + resultOnTouch[X]
					+ ", resultOnTouch[Y] = " + resultOnTouch[Y]);

			// ***********************************************************************

			int[] move = GameActivity.getCurrentPlayer().doMove();

			// ***********************************************************************
			// Set value into fieldMatrix
			if (GameField.setSignToCell(move[X], move[Y])) {

				if (gameFieldController.checkGameOver(move[X], move[Y])) {
					Toaster.doToastShort(context, "GameOver!");
				}

				Logger.v("move[X] = " + move[Y] + ", move[Y] = " + move[Y]);
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
		int right = x + cellSizeDip;
		int bottom = y + cellSizeDip;

		rectPictDst.set(left, top, right, bottom);
	}

	@SuppressWarnings("deprecation")
	private void init() {

		// Set Background
		fieldBackground = getResources().getDrawable(
				R.drawable.field_background);
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

		// Picture for PlayerHumanLocal's Sign and cellWin
		bmpSignPlayerX = getResBitmap(R.drawable.lib_cross);
		bmpSignPlayerO = getResBitmap(R.drawable.lib_circle);
		bmpCellWin = getResBitmap(R.drawable.cell_win_background);
	}

	private int calculateFieldSizeDip() {
		fieldSizeDip = calculateCellSize() * fieldSizeCalculated;

		// Logger.v("calculateFieldSize()::fieldSizeCalculated = "
		// + fieldSizeCalculated);
		// Logger.v("calculateFieldSize()::cellSizeDip = " + cellSizeDip);
		// Logger.v("calculateFieldSize()::fieldSizeDip = " + fieldSizeDip);

		return fieldSizeDip;
	}

	private int calculateCellSize() {

		int cellSizeMin = getResources().getInteger(R.integer.cell_size_min);
		int cellSizeCalc = (int) Math.floor(viewSizeDip / fieldSizePrefs);

		if (cellSizeMin < cellSizeCalc) {
			cellSizeDip = cellSizeCalc;
			fieldSizeCalculated = fieldSizePrefs;

		} else {
			cellSizeDip = cellSizeMin;
			fieldSizeCalculated = (int) Math.floor(viewSizeDip / cellSizeMin);
		}

		// Logger.v("cellSizeDip = " + cellSizeDip);
		return cellSizeDip;
	}

	/**
	 * Sets value from 'Preferences' such as 'fieldSizeDip' and
	 * 'numCheckedSignsPrefs' to SOMEWHERE
	 */
	private void setPrefsValue() {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(context);

		// Get default value from 'Preferences' (defaultValue = 3)
		int defaultValue = Integer.valueOf(prefs.getString(getResources()
				.getString(R.string.pref_field_size_value), getResources()
				.getString(R.string.pref_field_size_value)));

		// Get amount of the cells from 'Preferences'
		fieldSizePrefs = Integer.valueOf(prefs.getString(getResources()
				.getString(R.string.pref_field_size_key), getResources()
				.getString(R.string.pref_field_size_value)));
		if (fieldSizePrefs < defaultValue)
			fieldSizePrefs = defaultValue;

		// Get number signs to Win from 'Preferences'
		numCheckedSignsPrefs = Integer.valueOf(prefs.getString(getResources()
				.getString(R.string.pref_num_check_signs_key), getResources()
				.getString(R.string.pref_num_check_signs_value)));

		// Game balance restrictions
		if (fieldSizePrefs < 3) {
			fieldSizePrefs = defaultValue;
		}

		if (fieldSizePrefs < numCheckedSignsPrefs) {
			numCheckedSignsPrefs = defaultValue;
		}

		if ((3 < fieldSizePrefs) && (numCheckedSignsPrefs < 4)) {
			numCheckedSignsPrefs = 4;
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
			drawable.setBounds(0, 0, cellSizeDip, cellSizeDip);
			drawable.draw(canvas);
		}

		return bmp;
	}

}
