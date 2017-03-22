package com.rupp.spring.domain;

/**
 * map to table activities
 * @author Nguonchhay <a href='mailto:tnguonchhay@gmail.com'> Touch Nguonchhay </a>
 * @version $id$ - $Revision$
 * @date 2017
 */

public class Activity extends AbstractEntity {
	
	public static final String TABLE = "activities";
	
	private Long parentId;
	private String name;
	private String icon;
	private int score;
	private String note;
	private boolean isActivated;
	
	public Activity() {}
	public Activity(Long id, Long parentId, String name, String icon, int score, String note, boolean isActivated) {
		super.setId(id);
		setParentId(parentId);
		setName(name);
		setIcon(icon);
		setScore(score);
		setNote(note);
		setActivated(isActivated);
	}
	
	public Long getParentId() {
		return parentId;
	}
	
	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	
	public int getScore() {
		return score;
	}
	
	public void setScore(int score) {
		this.score = score;
	}
	
	public String getNote() {
		return note;
	}
	
	public void setNote(String note) {
		this.note = note;
	}
	
	public boolean isActivated() {
		return isActivated;
	}
	
	public void setActivated(boolean isActivated) {
		this.isActivated = isActivated;
	}
}
