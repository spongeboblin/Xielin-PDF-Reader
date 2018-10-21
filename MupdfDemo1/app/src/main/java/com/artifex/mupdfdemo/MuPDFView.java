package com.artifex.mupdfdemo;

import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.telecom.Call;
import android.view.KeyEvent;

enum Hit {Nothing, Widget, Annotation};

public interface MuPDFView{
	public void setPage(int page, PointF size);
	public void setScale(float scale);
	public int getPage();
	public void blank(int page);
	public Hit passClickEvent(float x, float y);
	public LinkInfo hitLink(float x, float y);
	public void selectText(float x0, float y0, float x1, float y1);
	public void deselectText();
	public boolean copySelection();
	public boolean markupSelection(CallBack callback,Annotation.Type type);
	public boolean markupSelectionbymsg(int page, PointF points[], Annotation.Type type);
	public void deleteSelectedAnnotation();
	public void setSearchBoxes(RectF searchBoxes[]);
	public void setLinkHighlighting(boolean f);
	public void deselectAnnotation();
	public void startDraw(float x, float y);
	public void continueDraw(float x, float y);
	public void cancelDraw();
	public boolean saveDraw(CallBack callBack);
	public boolean saveDrawbymsg(int page,PointF path[][]);
	public void setChangeReporter(Runnable reporter);
	public void update();
	public void updateHq(boolean update);
	public void removeHq();
	public void releaseResources();
	public void releaseBitmaps();
}

