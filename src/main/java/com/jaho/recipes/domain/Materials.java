package com.jaho.recipes.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "materials")
public class Materials {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="material_id")
	private Long material_id;
	
	
	
	@Column(name="name", unique=true, nullable=false,length=50)
	private String name;



	public Materials() {
		super();
	}



	public Materials(String name) {
		super();
		this.name = name;
	}



	public Materials(Long material_id, String name) {
		super();
		this.material_id = material_id;
		this.name = name;
	}



	public Long getMaterial_id() {
		return material_id;
	}



	public void setMaterial_id(Long material_id) {
		this.material_id = material_id;
	}



	public String getName() {
		return name;
	}



	public void setName(String name) {
		this.name = name;
	}



	@Override
	public String toString() {
		return "Materials [material_id=" + material_id + ", name=" + name + "]";
	}
	
	

	
	
}
