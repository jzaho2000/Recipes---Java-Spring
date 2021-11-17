package com.jaho.recipes.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "measurements")
public class Measurements {

	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long measure_id;
	
	@Column(name="measure")
	private String measure;
	
	@Column(name="short_text")
	private String short_text;

	public Measurements() {
		super();
	}

	public Measurements(Long measure_id, String measure, String short_text) {
		super();
		this.measure_id = measure_id;
		this.measure = measure;
		this.short_text = short_text;
	}
	
	public Measurements(String measure, String short_text) {
		super();
		this.measure = measure;
		this.short_text = short_text;
	}

	public Long getMeasure_id() {
		return measure_id;
	}

	public void setMeasure_id(Long measure_id) {
		this.measure_id = measure_id;
	}

	public String getMeasure() {
		return measure;
	}

	public void setMeasure(String measure) {
		this.measure = measure;
	}

	public String getShort_text() {
		return short_text;
	}

	public void setShort_text(String short_text) {
		this.short_text = short_text;
	}

	@Override
	public String toString() {
		return "Measurements [measure_id=" + measure_id + ", measure=" + measure + ", short_text=" + short_text + "]";
	}
	
	
	
	
	
	
}
