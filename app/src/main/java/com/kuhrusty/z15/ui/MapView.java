package com.kuhrusty.z15.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.support.v4.content.res.ResourcesCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.kuhrusty.z15.R;
import com.kuhrusty.z15.model.Map;
import com.kuhrusty.z15.model.Tile;

import java.util.HashMap;

public class MapView extends View {
    private static final String LOGBIT = "MapView";

    //  a little extra stuff we need about each tile.
    private static class TileRenderInfo {
        private final Tile tile;
        private boolean dimmed = true;
        private final int canvasRow;  //  0 is at the top, not the bottom
        private final int col;
        private Bitmap tileImage;
        private Bitmap scaledTileImage;  //  rebuild every time we do layout

        public TileRenderInfo(Tile tile, int col, int canvasRow, Bitmap rawImage) {
            this.tile = tile;
            this.col = col;
            this.canvasRow = canvasRow;
            this.tileImage = rawImage;
        }
    }

    public MapView(Context context) {
        super(context);
        initPaint(context);
    }

    public MapView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPaint(context);
    }

    private void initPaint(Context context) {
        mapBGPaint = new Paint();
        mapBGPaint.setColor(Color.GRAY);
        mapBGPaint.setAlpha(127);
        mapBGPaint.setStyle(Paint.Style.FILL);

        tileBGPaint = new Paint();
        tileBGPaint.setColor(0xff448844);
        tileBGPaint.setStyle(Paint.Style.FILL);

        dimPaint = new Paint();
        dimPaint.setColor(0x5f000000);
        dimPaint.setStyle(Paint.Style.FILL);

        Typeface font = ResourcesCompat.getFont(context, R.font.anton);
        tileIDPaint = new Paint();
        tileIDPaint.setTypeface(font);
        tileIDPaint.setColor(Color.WHITE);
        tileIDPaint.setTextAlign(Paint.Align.CENTER);
        tileIDPaint.setShadowLayer(4.0f, 0f, 0f, Color.BLACK);
        //  font size is set in layout()
        //probably should set shadow layer radius there, too

        roadPaint = new Paint();
        roadPaint.setColor(Color.YELLOW);
        roadPaint.setAlpha(191);
        roadPaint.setStyle(Paint.Style.STROKE);
        //  stroke width is set in layout()

        gridPaint = new Paint();
        gridPaint.setColor(Color.BLACK);
        gridPaint.setStrokeWidth(4.0f);  //  px ? dp ?
        gridPaint.setStyle(Paint.Style.STROKE);

        selectionPaint = new Paint();
        selectionPaint.setColor(Color.YELLOW);
        selectionPaint.setStrokeWidth(4.0f);  //  px ? dp ?
        selectionPaint.setStyle(Paint.Style.STROKE);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) return true;
        if (ev.getAction() != MotionEvent.ACTION_UP) return super.onTouchEvent(ev);

        Tile newSelection = null;
        int col = (int)((ev.getX() - halfTileSize) / tileSize);
        int maprow = (int)((ev.getY() - halfTileSize) / tileSize);
        int row = map.getHeight() - maprow - 1;
        if ((row >= 0) && (row < map.getHeight()) && (col >= 0) && (col < map.getWidth())) {
            newSelection = map.getTile(col, row);
            if ((newSelection != null) && (newSelection != selectedTile)) {
                selectedTile = newSelection;
                selectedTileX = col;
                selectedTileMapY = maprow;
                invalidate();
                return true;
            }
        }
        //  still here, so if we have a selection, clear it.
        if (selectedTile != null) {
            selectedTile = null;
            invalidate();
        }
        return true;
    }

    @Override
    public void onMeasure(int widthSpec, int heightSpec) {
        if (map == null) {
            super.onMeasure(widthSpec, heightSpec);
            return;
        }
        int wmode = MeasureSpec.getMode(widthSpec);
        int wsize = MeasureSpec.getSize(widthSpec);
        int hmode = MeasureSpec.getMode(heightSpec);
        int hsize = MeasureSpec.getSize(heightSpec);
        float xts = (float)wsize / (map.getWidth() + 1);
        float yts = (float)hsize / (map.getHeight() + 1);
        float ts = (xts < yts) ? xts : yts;
        //Log.d(LOGBIT, "onMeasure(" + MeasureSpec.toString(widthSpec) + ", " +
        //        MeasureSpec.toString(heightSpec) + ")");
        //Log.d(LOGBIT, "  returning " + ts + " * " + map.getWidth() + " = " +
        //        (int)(ts * map.getWidth()) + ", " + ts + " * " +
        //        map.getHeight() + " = " + (int)(ts * map.getHeight()));
        setMeasuredDimension((int)(ts * (map.getWidth() + 1)),
                             (int)(ts * (map.getHeight() + 1)));
    }

    @Override
    public void onLayout(boolean changed, int left, int top, int right, int bottom) {
        //Log.d(LOGBIT, "onLayout(" + changed + ", left " + left + ", top " +
        //        top + ", right " + right + ", bottom " + bottom + ") hit!");
        super.onLayout(changed, left, top, right, bottom);
        if (map == null) return;
        float xts = ((float)(right - left)) / (map.getWidth() + 1);
        float yts = ((float)(bottom - top)) / (map.getHeight() + 1);
        tileSize = (xts < yts) ? xts : yts;
        halfTileSize = tileSize / 2f;
        roadPaint.setStrokeWidth(tileSize / 5f);
        tileIDPaint.setTextSize(tileSize / 3f);
        //no idea whether I need to save this
        lastWidth = right - left;
        lastHeight = bottom - top;
    }

    public void dimAll() {
        boolean invalidate = false;
        for (TileRenderInfo tri : idToInfo.values()) {
            if (!tri.dimmed) {
                tri.dimmed = true;
                invalidate = true;
            }
        }
        if (selectedTile != null) {
            selectedTile = null;
            invalidate = true;
        }
        if (invalidate) invalidate();
    }

    public void undimAll() {
        boolean invalidate = false;
        for (TileRenderInfo tri : idToInfo.values()) {
            if (tri.dimmed) {
                tri.dimmed = false;
                invalidate = true;
            }
        }
        if (selectedTile != null) {
            selectedTile = null;
            invalidate = true;
        }
        if (invalidate) invalidate();
    }

    /**
     * Undim and select the given ID.  If null, the selection will be cleared.
     *
     * @param id
     * @param dimID if not null, this tile will be dimmed.
     */
    public void undimAndSelectTile(String id, String dimID) {
        if (id == null) {
            if (selectedTile != null) {
                selectedTile = null;
                invalidate();
            }
        } else {
            TileRenderInfo tri = idToInfo.get(id);
            if ((tri != null) /*&& (tri.tile != selectedTile) */) {
                tri.dimmed = false;
                selectedTile = tri.tile;
                selectedTileX = tri.col;
                selectedTileMapY = tri.canvasRow;  // horrible variable names
                invalidate();
            }
        }
        if (dimID != null) {
            TileRenderInfo tri = idToInfo.get(dimID);
            if (tri != null) {
                tri.dimmed = true;
                invalidate();
            }
        }
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawRect(0, 0, lastWidth, lastHeight, mapBGPaint);

        if (map == null) return;

        canvas.translate(halfTileSize, halfTileSize);
        canvas.save();
        for (int row = 0; row < map.getHeight(); ++row) {
            int mr = map.getHeight() - row - 1;
            if (row > 0) canvas.translate(0, tileSize);
            canvas.save();
            for (int col = 0; col < map.getWidth(); ++col) {
                if (col > 0) canvas.translate(tileSize, 0);
                Tile tt = map.getTile(col, mr);
                if ((tt != null) && (tt != selectedTile)) paintTile(canvas, tt);
            }
            canvas.restore();
        }
        canvas.restore();
        paintGridLines(canvas);

        if (selectedTile != null) {
            canvas.save();
            canvas.translate(selectedTileX * tileSize - halfTileSize,
                             selectedTileMapY * tileSize - halfTileSize);
            canvas.scale(2f, 2f);
            paintTile(canvas, selectedTile);
            canvas.drawRect(0, 0, tileSize, tileSize, selectionPaint);
            canvas.restore();
        }

    }

    private void paintTile(Canvas canvas, Tile tile) {
        TileRenderInfo tri = idToInfo.get(tile.getID());
        if (tri.tileImage != null) {
            if (tri.scaledTileImage == null) {
                boolean whatDoesFilterMean = false;
                tri.scaledTileImage = Bitmap.createScaledBitmap(tri.tileImage,
                        (int)tileSize, (int)tileSize, whatDoesFilterMean);
            }
            if (tile.getRotation() == Tile.Direction.East) {
                canvas.save();
                canvas.translate(tileSize, 0);
                canvas.rotate(90);
            } else if (tile.getRotation() == Tile.Direction.South) {
                canvas.save();
                canvas.translate(tileSize, tileSize);
                canvas.rotate(180);
            } else if (tile.getRotation() == Tile.Direction.West) {
                canvas.save();
                canvas.translate(0, tileSize);
                canvas.rotate(-90);
            } // else brain damage
            canvas.drawBitmap(tri.scaledTileImage, 0, 0, tileBGPaint);
            if (tile.getRotation() != Tile.Direction.North) canvas.restore();
        } else {
            canvas.drawRect(0, 0, tileSize, tileSize, tileBGPaint);
        }

        //  Not sure I could have written this more dumbly, but I want to know
        //  when to draw curves.
        if (tile.hasRoad(Tile.Direction.North)) {
            if (tile.hasRoad(Tile.Direction.East)) {
                if (tile.hasRoad(Tile.Direction.South)) {
                    if (tile.hasRoad(Tile.Direction.West)) {
                        //  north-east-south-west
                        canvas.drawLine(halfTileSize, 0, halfTileSize, tileSize, roadPaint);
                        canvas.drawLine(0, halfTileSize, tileSize, halfTileSize, roadPaint);
                    } else {  //  no west
                        //  north-east-south
                        canvas.drawLine(halfTileSize, 0, halfTileSize, tileSize, roadPaint);
                        canvas.drawLine(halfTileSize, halfTileSize, tileSize, halfTileSize, roadPaint);
                    }
                } else {  //  no south
                    if (tile.hasRoad(Tile.Direction.West)) {
                        //  north-east-west
                        canvas.drawLine(halfTileSize, 0, halfTileSize, halfTileSize, roadPaint);
                        canvas.drawLine(0, halfTileSize, tileSize, halfTileSize, roadPaint);
                    } else {  //  no west
                        //  north-east
                        canvas.drawArc(new RectF(halfTileSize, -halfTileSize,
                                tileSize + halfTileSize, halfTileSize), 90, 90,
                                false, roadPaint);
                    }
                }
            } else {  //  no east
                if (tile.hasRoad(Tile.Direction.South)) {
                    if (tile.hasRoad(Tile.Direction.West)) {
                        //  north-south-west
                        canvas.drawLine(halfTileSize, 0, halfTileSize, tileSize, roadPaint);
                        canvas.drawLine(0, halfTileSize, halfTileSize, halfTileSize, roadPaint);
                    } else {  //  no west
                        //  north-south
                        canvas.drawLine(halfTileSize, 0, halfTileSize, tileSize, roadPaint);
                    }
                } else {  //  no south
                    if (tile.hasRoad(Tile.Direction.West)) {
                        //  north-west
                        canvas.drawArc(new RectF(-halfTileSize, -halfTileSize,
                                halfTileSize, halfTileSize), 0, 90,
                                false, roadPaint);
                    } else {  //  no west
                        //  north
                        canvas.drawLine(halfTileSize, 0, halfTileSize, halfTileSize, roadPaint);
                    }
                }
            }
        } else {  //  no north
            if (tile.hasRoad(Tile.Direction.East)) {
                if (tile.hasRoad(Tile.Direction.South)) {
                    if (tile.hasRoad(Tile.Direction.West)) {
                        //  east-south-west
                        canvas.drawLine(halfTileSize, halfTileSize, halfTileSize, tileSize, roadPaint);
                        canvas.drawLine(0, halfTileSize, tileSize, halfTileSize, roadPaint);
                    } else {  //  no west
                        //  east-south
                        canvas.drawArc(new RectF(halfTileSize, halfTileSize,
                                halfTileSize + tileSize, halfTileSize + tileSize),
                                180, 90, false, roadPaint);
                    }
                } else {  //  no south
                    if (tile.hasRoad(Tile.Direction.West)) {
                        //  east-west
                        canvas.drawLine(0, halfTileSize, tileSize, halfTileSize, roadPaint);
                    } else {  //  no west
                        //  east
                        canvas.drawLine(halfTileSize, halfTileSize, tileSize, halfTileSize, roadPaint);
                    }
                }
            } else {  //  no east
                if (tile.hasRoad(Tile.Direction.South)) {
                    if (tile.hasRoad(Tile.Direction.West)) {
                        //  south-west
                        canvas.drawArc(new RectF(-halfTileSize, halfTileSize,
                                        halfTileSize, halfTileSize + tileSize),
                                270, 90, false, roadPaint);
                    } else {  //  no west
                        //  south
                        canvas.drawLine(halfTileSize, halfTileSize, halfTileSize, tileSize, roadPaint);
                    }
                } else {  //  no south
                    //  west
                    canvas.drawLine(0, halfTileSize, halfTileSize, halfTileSize, roadPaint);
                }
            }
        }

        canvas.drawText(tile.getID(), halfTileSize, tileSize * .65f, tileIDPaint);

        //  I was thinking it might be better to draw everything else using a
        //  Paint with 50% alpha instead of slapping this over the top, but
        //  this is easy...
        if ((tile != selectedTile) && idToInfo.get(tile.getID()).dimmed) {
            canvas.drawRect(0, 0, tileSize, tileSize, dimPaint);
        }
    }

    private void paintGridLines(Canvas canvas) {
        float len = map.getHeight() * tileSize;
        for (int ii = 0; ii <= map.getWidth(); ++ii) {
            float tx = ii * tileSize;
            canvas.drawLine(tx, 0, tx, len, gridPaint);
        }
        len = map.getWidth() * tileSize;
        for (int ii = 0; ii <= map.getHeight(); ++ii) {
            float ty = ii * tileSize;
            canvas.drawLine(0, ty, len, ty, gridPaint);
        }
    }

    public void setMap(Map map) {
        this.map = map;
        BitmapFactory.Options bfo = new BitmapFactory.Options();
        bfo.inScaled = false;
        idToInfo.clear();
        for (int row = 0; row < map.getHeight(); ++row) {
            int canvasRow = map.getHeight() - row - 1;
            for (int col = 0; col < map.getWidth(); ++col) {
                Tile tt = map.getTile(col, row);
                if (tt != null) {
                    Bitmap rawImage = null;
                    if (tt.getTileImageResID() != 0) {
                        rawImage = BitmapFactory.decodeResource(getResources(),
                                tt.getTileImageResID(), bfo);
                    }
                    idToInfo.put(tt.getID(),
                            new TileRenderInfo(tt, col, canvasRow, rawImage));
                }
            }
        }
        requestLayout();
    }

    private Paint mapBGPaint;
    private Paint tileBGPaint;
    private Paint tileIDPaint;
    private Paint roadPaint;
    private Paint gridPaint;
    private Paint selectionPaint;
    private Paint dimPaint;

    private Map map;
    private HashMap<String, TileRenderInfo> idToInfo = new HashMap<>();
    private Tile selectedTile;
    private int selectedTileX;
    private int selectedTileMapY;  //  0 is top row, not bottom
    private float tileSize;  //  in px
    private float halfTileSize;

    private int lastWidth;
    private int lastHeight;
}
