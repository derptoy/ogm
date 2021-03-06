package com.uwsoft.editor.renderer.actor;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.uwsoft.editor.renderer.IResource;
import com.uwsoft.editor.renderer.data.ButtonVO;
import com.uwsoft.editor.renderer.utils.CustomVariables;

public class TextButtonItem extends TextButton  implements IBaseItem {

	public ButtonVO dataVO;	
	public IResource rm;
	public float mulX = 1f;
	public float mulY = 1f;
	
	protected int layerIndex = 0;
	private boolean isLockedByLayer = false;
	private CompositeItem parentItem = null;

    private CustomVariables customVariables = new CustomVariables();

	public TextButtonItem(ButtonVO vo, IResource rm,CompositeItem parent) {
		this(vo, rm);
		setParentItem(parent);
	}
	
	public TextButtonItem(ButtonVO vo, IResource rm) {
		super(vo.text, rm.getSkin());
		dataVO = vo;
		
		this.rm = rm;
		setX(dataVO.x);
		setY(dataVO.y);
//		setScaleX(dataVO.scaleX);
//		setScaleY(dataVO.scaleY);
        customVariables.loadFromString(dataVO.customVars);
		getLabel().setFontScale(dataVO.scaleX, dataVO.scaleY);
		this.setRotation(dataVO.rotation); 
		
		if(dataVO.zIndex < 0) dataVO.zIndex = 0;
				
		if(dataVO.tint == null) {			
			setTint(new Color(1, 1, 1, 1));	
		} else {
			setTint(new Color(dataVO.tint[0], dataVO.tint[1], dataVO.tint[2], dataVO.tint[3]));
		}
		pack(); layout();
	}
	
	public void setTint(Color tint) {
		float[] clr = new float[4]; 
		clr[0] = tint.r;
		clr[1] = tint.g;
		clr[2] = tint.b;
		clr[3] = tint.a;
		this.getDataVO().tint = clr;
		this.setColor(tint);
	}
	
	public ButtonVO getDataVO() {
		//updateDataVO();
		return dataVO;
	}
	
	public void updateDataVO() {
		dataVO.x = getX();
		dataVO.y = getY();

        dataVO.customVars = customVariables.saveAsString();
	}
	
	@Override
	public void renew() {
		setText(dataVO.text);
        customVariables.loadFromString(dataVO.customVars);
		
		setX(dataVO.x*this.mulX);
		setY(dataVO.y*this.mulY);
//		setScaleX(dataVO.scaleX*this.mulX);
//		setScaleY(dataVO.scaleY*this.mulY);
		getLabel().setFontScale(dataVO.scaleX*this.mulX, dataVO.scaleY*this.mulY);
		setRotation(dataVO.rotation); 
		pack(); layout();
	}

	@Override
	public boolean isLockedByLayer() {
		return isLockedByLayer;
	}

	@Override
	public void setLockByLayer(boolean isLocked) {
		isLockedByLayer = isLocked;
	}

	@Override
	public boolean isComposite() {
		return false;
	}

	public void applyResolution(float mulX, float mulY) {
		this.mulX = mulX;
		this.mulY = mulY;
		setX(dataVO.x*this.mulX);
		setY(dataVO.y*this.mulY);
		getLabel().setFontScale(dataVO.scaleX*this.mulX, dataVO.scaleY*this.mulY);
		updateDataVO();			
		pack(); layout();
	}

	@Override
	public int getLayerIndex() {
		return layerIndex;
	}

	@Override
	public void setLayerIndex(int index) {
		layerIndex = index;
	}
	
	public CompositeItem getParentItem() {
		return parentItem;
	}
	
	public void setParentItem(CompositeItem parentItem) {
		this.parentItem = parentItem;
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

    public CustomVariables getCustomVariables() {
        return customVariables;
    }
}
