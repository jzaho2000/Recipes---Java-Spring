package com.jaho.recipes.domain;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;

public interface MaterialsRepository extends CrudRepository<Materials,Long> {

	public List<Materials> findAllWithCustomOrderBy(Sort sort);
	
	
	public Materials findByName(String name);
	
}
