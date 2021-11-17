package com.jaho.recipes.domain;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;

public interface MeasurementsRepository extends CrudRepository<Measurements,Long> {

	public List<Measurements> findAllWithCustomOrderBy(Sort by);

	public Measurements findByMeasure(String measure);
	
	
	
}