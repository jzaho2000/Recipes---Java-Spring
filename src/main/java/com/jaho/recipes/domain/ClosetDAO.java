package com.jaho.recipes.domain;

import java.util.List;

public interface ClosetDAO {
	
	public String save(long user_id, long material);
	
	public String save(String username, long material);
	
	public String save(long user_id, long username, long material);
	
	public String delete(long user_id, long material);
	
	public String delete(String username, long material);
	
	public List<Materials> getClosetContent(long user_id);
	
	public List<Materials> getClosetContent(String username);
	
	public List<Long> getSuggestedRecipes(long user_id);
	
	public List<Long> getSuggestedRecipes(String username);
	
	
	
}
