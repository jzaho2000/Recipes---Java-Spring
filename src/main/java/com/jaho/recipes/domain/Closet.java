package com.jaho.recipes.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;

@Entity
public class Closet {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long closet_id;
	
	private Long user_id;
	
	@JoinColumn(name="material_id",nullable=false)
	private Long material_id;

	public Closet() {
		super();
	}

	public Closet(Long user_id, Long material_id) {
		super();
		this.user_id = user_id;
		this.material_id = material_id;
	}

	public Closet(Long closet_id, Long user_id, Long material_id) {
		super();
		this.closet_id = closet_id;
		this.user_id = user_id;
		this.material_id = material_id;
	}

	public Long getCloset_id() {
		return closet_id;
	}

	public void setCloset_id(Long closet_id) {
		this.closet_id = closet_id;
	}

	public Long getUser_id() {
		return user_id;
	}

	public void setUser_id(Long user_id) {
		this.user_id = user_id;
	}

	public Long getMaterial_id() {
		return material_id;
	}

	public void setMaterial_id(Long material_id) {
		this.material_id = material_id;
	}

	@Override
	public String toString() {
		return "Closet [closet_id=" + closet_id + ", user_id=" + user_id + ", material_id=" + material_id + "]";
	}
	
	
}
